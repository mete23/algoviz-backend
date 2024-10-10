package de.algoviz.algoviz.parser.logfile_parser;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modification.changes.GraphChanges;
import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;
import de.algoviz.algoviz.model.log_file.LogFile;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.parser.graph_parser.DotFormatParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a log file parser.
 *
 * @author Tim
 * @version 1.0
 */
public class LogFileParser implements ILogFileParser {

    private static final String LINE_SEPARATOR = "###";
    private static final String LINE_SEPARATOR_NEW_LINE = "\\R";
    private static final String ERROR_SPLITTING = "A log file must consist of two parts seperated by an empty line.";

    public static final String PREFIX_ERROR_PARSING_ALGORITHM = "Error parsing algorithm: ";
    private static final String ERROR_UNKNOWN_COMMAND = "The command \"%s\" is unknown.";
    private static final String PREFIX_ERROR_LINE = "Error in line %s. ";
    private static final String ERROR_MESSAGE_NODE_WITH_ID_DOES_NOT_EXIST = PREFIX_ERROR_LINE + "The node with the id %d does not exist.";
    private static final String ERROR_MESSAGE_EDGE_WITH_ID_DOES_NOT_EXIST = PREFIX_ERROR_LINE + "The edge with the id %d does not exist.";

    private final Map<Pattern, NodeModificationCommand> nodeModificationCommandHashMap = new HashMap<>();
    private final Map<Pattern, EdgeModificationCommand> edgeModificationCommandHashMap = new HashMap<>();
    private final Map<Pattern, GraphModificationCommand> graphModificationCommandHashMap = new HashMap<>();

    public LogFileParser() {
        for (NodeModificationCommand command : NodeModificationCommand.values()) {
            nodeModificationCommandHashMap.put(Pattern.compile(NodeModificationCommand.generatePattern(command)), command);
        }
        for (EdgeModificationCommand command : EdgeModificationCommand.values()) {
            edgeModificationCommandHashMap.put(Pattern.compile(EdgeModificationCommand.generatePattern(command)), command);
        }
        for (GraphModificationCommand command : GraphModificationCommand.values()) {
            graphModificationCommandHashMap.put(Pattern.compile(GraphModificationCommand.generatePattern(command)), command);
        }
    }

    @Override
    public LogFile parse(String logFileString) throws ParseException {

        String[] logFileSections = logFileString.split(LINE_SEPARATOR, 2);

        if (logFileSections.length != 2) {
            throw new ParseException(ERROR_SPLITTING);
        }

        Graph graph = new DotFormatParser().parse(logFileSections[0]);
        List<ModificationStep> modificationStepList = getModificationStepList(logFileSections[1], graph.clone());

        return new LogFile(graph, modificationStepList);
    }

    private List<ModificationStep> getModificationStepList(String logFileSection, Graph graph) throws ParseException {
        List<ModificationStep> modificationStepList = new ArrayList<>();
        String[] modificationSteps = logFileSection.split(LINE_SEPARATOR);
        for (String step : modificationSteps) {
            modificationStepList.add(getModificationStep(step, graph));
        }
        return modificationStepList;
    }

    private ModificationStep getModificationStep(String step, Graph graph) throws ParseException {
        ModificationStep modificationStep = new ModificationStep();
        String[] lines = step.split(LINE_SEPARATOR_NEW_LINE);
        for (String line : lines) {
            if (line.isBlank())
                continue;
            try {
                addModification(modificationStep, line, graph);
            } catch (ParseException exception) {
                throw new ParseException(PREFIX_ERROR_PARSING_ALGORITHM + String.format(PREFIX_ERROR_LINE, line) + exception.getMessage());
            }
        }
        return modificationStep;
    }

    private void addModification(ModificationStep currentModificationStep, String line, Graph graph) throws ParseException {

        for (Map.Entry<Pattern, NodeModificationCommand> command : nodeModificationCommandHashMap.entrySet()) {
            Matcher matcher = command.getKey().matcher(line);
            if (matcher.matches()) {
                String[] parameters = new String[command.getValue().PARAMETERS_PATTERN.length];
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = matcher.group(i + 2);
                }
                INodeModification modification = command.getValue().generate(parameters);
                int nodeId = Integer.parseInt(matcher.group(1));
                Node node = graph.getNodesMap().get(nodeId);
                if (node == null) {
                    throw new ParseException(String.format(ERROR_MESSAGE_NODE_WITH_ID_DOES_NOT_EXIST, line, nodeId));
                }
                currentModificationStep.addModification(node.clone(), modification);
                modification.applyChange(node);
                return;
            }
        }

        for (Map.Entry<Pattern, EdgeModificationCommand> command : edgeModificationCommandHashMap.entrySet()) {
            Matcher matcher = command.getKey().matcher(line);
            if (matcher.matches()) {
                String[] parameters = new String[command.getValue().PARAMETERS_PATTERN.length];
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = matcher.group(i + 2);
                }
                IEdgeModification modification = command.getValue().generate(parameters);
                int edgeId = Integer.parseInt(matcher.group(1));
                Edge edge = graph.getEdgesMap().get(edgeId);
                if (edge == null) {
                    throw new ParseException(String.format(ERROR_MESSAGE_EDGE_WITH_ID_DOES_NOT_EXIST, line, edgeId));
                }
                currentModificationStep.addModification(edge.clone(), modification);
                modification.applyChange(edge);
                return;
            }
        }

        for (Map.Entry<Pattern, GraphModificationCommand> command : graphModificationCommandHashMap.entrySet()) {
            Matcher matcher = command.getKey().matcher(line);
            if (matcher.matches()) {
                String[] parameters = new String[command.getValue().PARAMETERS_PATTERN.length];
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = matcher.group(i + 1);
                }
                GraphChanges changes = new GraphChanges();
                changes.addChange(command.getValue().generate(graph, parameters));
                currentModificationStep.addModification(changes);
                changes.applyChanges(graph);
                return;
            }
        }
        throw new ParseException(String.format(ERROR_UNKNOWN_COMMAND, line));
    }
}
