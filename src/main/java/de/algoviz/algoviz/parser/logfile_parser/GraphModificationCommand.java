package de.algoviz.algoviz.parser.logfile_parser;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modifications.graph.*;
import de.algoviz.algoviz.parser.ParseException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This enum represents the commands to change the graph
 *
 * @author Tim
 */
public enum GraphModificationCommand {

    ADD_NODE("addNode", "\\d+", "\\S+", "\\S+") {

        private static final String ERROR_COORDINATES_OUT_OF_RANGE = "The coordinates of the node should be a value between 0 and 1.";

        @Override
        public IGraphModification generate(Graph graph, String[] parameters) throws ParseException {
            int id = Integer.parseInt(parameters[0]);
            if (graph.getNodesMap().containsKey(id)) {
                throw new ParseException(String.format(ERROR_INVALID_ID, id));
            }
            double[] coordinates = new double[2];
            try {
                coordinates[0] = Double.parseDouble(parameters[1]);
                coordinates[1] = Double.parseDouble(parameters[2]);
            } catch (NumberFormatException exception) {
                throw new ParseException(String.format(ERROR_NO_NODE_ID, parameters[1], parameters[2]));
            }

            if (coordinates[0] < 0 || coordinates[0] > 1 || coordinates[1] < 0 || coordinates[1] > 1) {
                throw new ParseException(ERROR_COORDINATES_OUT_OF_RANGE);
            }
            return new GraphAddNodeModification(new Node(id, new Coordinates(coordinates)));
        }
    },

    REMOVE_NODE("removeNode", "\\d+") {
        @Override
        public IGraphModification generate(Graph graph, String[] parameters) throws ParseException {

            int id = Integer.parseInt(parameters[0]);
            if (!graph.getNodesMap().containsKey(id)) {
                throw new ParseException(String.format(ERROR_INVALID_ID, id));
            }
            return new GraphRemoveNodeModification(graph.getNodesMap().get(id), new ArrayList<>());
        }
    },
    ADD_EDGE("addEdge", "\\d+", "\\d+", "\\d+") {
        private static final String ERROR_SAME_EDGES = "The edge with the id %d does not have two different nodes.";

        @Override
        public IGraphModification generate(Graph graph, String[] parameters) throws ParseException {

            int id = Integer.parseInt(parameters[0]);
            if (graph.getEdgesMap().containsKey(id)) {
                throw new ParseException(String.format(ERROR_INVALID_ID, id));
            }
            Node firstNode = graph.getNodesMap().get(Integer.parseInt(parameters[1]));
            Node secondNode = graph.getNodesMap().get(Integer.parseInt(parameters[2]));

            if (Objects.isNull(firstNode) || Objects.isNull(secondNode)) {
                throw new ParseException(String.format(ERROR_NO_NODE_ID, parameters[1], parameters[2]));
            }
            if (firstNode.getId() == secondNode.getId())
                throw new ParseException(String.format(ERROR_SAME_EDGES, id));
            return new GraphAddEdgeModification(new Edge(id, firstNode, secondNode));
        }
    }, REMOVE_EDGE("removeEdge", "\\d+") {
        @Override
        public IGraphModification generate(Graph graph, String[] parameters) throws ParseException {

            int id = Integer.parseInt(parameters[0]);
            if (!graph.getEdgesMap().containsKey(id)) {
                throw new ParseException(String.format(ERROR_INVALID_ID, id));
            }
            return new GraphRemoveEdgeModification(graph.getEdgesMap().get(id));
        }
    };

    public static String generatePattern(GraphModificationCommand command) {
        StringBuilder patternBuilder = new StringBuilder(command.COMMAND);
        for (String commandPattern : command.PARAMETERS_PATTERN) {
            patternBuilder.append(":(");
            patternBuilder.append(commandPattern);
            patternBuilder.append(")");
        }
        return patternBuilder.toString();
    }

    public abstract IGraphModification generate(Graph graph, String[] parameters) throws ParseException;

    public final String COMMAND;
    public final String[] PARAMETERS_PATTERN;

    private static final String ERROR_INVALID_ID = "The id %d is not valid.";
    private static final String ERROR_NO_NODE_ID = "%s and %s must be the id of a node.";

    GraphModificationCommand(String command, String... parametersPattern) {
        this.COMMAND = command;
        this.PARAMETERS_PATTERN = parametersPattern;
    }
}
