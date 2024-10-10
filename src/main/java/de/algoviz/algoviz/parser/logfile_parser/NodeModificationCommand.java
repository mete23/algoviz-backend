package de.algoviz.algoviz.parser.logfile_parser;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.modifications.node.ColorNodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.CoordinatesNodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.LabelNodeModification;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.util.external.ColorConverter;

import java.awt.*;

/**
 * This enum represents the commands to change a property of a node
 *
 * @author Tim
 */
public enum NodeModificationCommand {

    CHANGE_COLOR("color", "\\S+") {
        @Override
        public INodeModification generate(String[] parameters) throws ParseException {
            Color color = ColorConverter.getColor(parameters[0]);
            return new ColorNodeModification(color, color);
        }
    },

    CHANGE_LABEL("label", ".*") {
        @Override
        public INodeModification generate(String[] parameters) {
            String label = parameters[0];
            return new LabelNodeModification(label, label);
        }
    },

    CHANGE_COORDINATES("coordinates", "\\S+", "\\S+") {

        private static final String ERROR_COORDINATES_OUT_OF_RANGE = "If you want to change the coordinates of a node, the new coordinates must be a value between 0 and 1.";
        private static final String ERROR_NO_NUMBER = "\"%s\" and \"%s\" must be valid number";

        @Override
        public INodeModification generate(String[] parameters) throws ParseException {
            double[] coordinates = new double[2];
            try {
                coordinates[0] = Double.parseDouble(parameters[0]);
                coordinates[1] = Double.parseDouble(parameters[1]);
            } catch (NumberFormatException exception) {
                throw new ParseException(String.format(ERROR_NO_NUMBER, parameters[0], parameters[1]));
            }
            if (coordinates[0] < 0 || coordinates[0] > 1 || coordinates[1] < 0 || coordinates[1] > 1)
                throw new ParseException(String.format(ERROR_COORDINATES_OUT_OF_RANGE));
            Coordinates newCoordinates = new Coordinates(coordinates);
            return new CoordinatesNodeModification(newCoordinates, newCoordinates);
        }
    };

    public static String generatePattern(NodeModificationCommand command) {
        StringBuilder patternBuilder = new StringBuilder("node:(\\d+):");
        patternBuilder.append(command.COMMAND);
        for (String commandPattern : command.PARAMETERS_PATTERN) {
            patternBuilder.append(":(");
            patternBuilder.append(commandPattern);
            patternBuilder.append(")");
        }
        return patternBuilder.toString();
    }

    public abstract INodeModification generate(String[] parameters) throws ParseException;

    public final String COMMAND;
    public final String[] PARAMETERS_PATTERN;

    NodeModificationCommand(String command, String... parametersPattern) {
        this.COMMAND = command;
        this.PARAMETERS_PATTERN = parametersPattern;
    }
}
