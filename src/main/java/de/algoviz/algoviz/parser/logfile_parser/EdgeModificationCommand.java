package de.algoviz.algoviz.parser.logfile_parser;

import de.algoviz.algoviz.model.graph_general.modifications.edge.EdgeColorModification;
import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.util.external.ColorConverter;

import java.awt.*;

/**
 * This enum represents the commands to change a property of an edge
 *
 * @author Tim
 */
public enum EdgeModificationCommand {

    CHANGE_COLOR("color", "\\S+") {
        @Override
        public IEdgeModification generate(String[] parameters) throws ParseException {
            Color color = ColorConverter.getColor(parameters[0]);
            return new EdgeColorModification(color, color);
        }
    };

    public static String generatePattern(EdgeModificationCommand command) {
        StringBuilder patternBuilder = new StringBuilder("edge:(\\d+):");
        patternBuilder.append(command.COMMAND);
        for (String commandPattern : command.PARAMETERS_PATTERN) {
            patternBuilder.append(":(");
            patternBuilder.append(commandPattern);
            patternBuilder.append(")");
        }
        return patternBuilder.toString();
    }

    public abstract IEdgeModification generate(String[] parameters) throws ParseException;

    public final String COMMAND;
    public final String[] PARAMETERS_PATTERN;

    EdgeModificationCommand(String command, String... parametersPattern) {
        this.COMMAND = command;
        this.PARAMETERS_PATTERN = parametersPattern;
    }
}
