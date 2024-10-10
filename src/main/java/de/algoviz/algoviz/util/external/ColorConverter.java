package de.algoviz.algoviz.util.external;

import de.algoviz.algoviz.parser.ParseException;

import java.awt.*;
import java.util.Objects;

/**
 * Utility class to convert colors to hexadecimal and vice versa.
 *
 * @author Tim
 * @version 1.0
 */
public final class ColorConverter {

    public static final String RGB_TO_HEXADECIMAL = "#%02x%02x%02x";
    private static final String COLOR_SHOULD_NOT_BE_NULL_MESSAGE = "The color should not be null.";
    private static final String NO_VALID_COLOR_MESSAGE = "\"%s\" is not a valid color.";
    public static final String REGEX_COLOR_HEXADECIMAL = "#([a-fA-F0-9]{6})";

    private ColorConverter() {
    }

    /**
     * Converts a color as {@link Color} to a hexadecimal string.
     *
     * @param color color to convert as {@link Color}
     * @return hexadecimal string which represents the color
     */
    public static String getColorHexadecimal(Color color) {
        return String.format(RGB_TO_HEXADECIMAL, color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Converts a hexadecimal string to a color as {@link Color}.
     *
     * @param colorHexadecimal hexadecimal string to convert
     * @return corresponding color as {@link Color}
     */
    public static Color getColor(String colorHexadecimal) throws ParseException {
        if (Objects.isNull(colorHexadecimal)) {
            throw new ParseException(COLOR_SHOULD_NOT_BE_NULL_MESSAGE);
        }
        if (!colorHexadecimal.matches(REGEX_COLOR_HEXADECIMAL)) {
            throw new ParseException(String.format(NO_VALID_COLOR_MESSAGE, colorHexadecimal));
        }
        try {
            return Color.decode(colorHexadecimal);
        } catch (NumberFormatException exception) {
            throw new ParseException(String.format(NO_VALID_COLOR_MESSAGE, colorHexadecimal));
        }
    }
}
