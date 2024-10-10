package de.algoviz.algoviz.util.external;

import de.algoviz.algoviz.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ColorConverterTest {

    private static Stream<Arguments> generateColor() {
        return Stream.of(
                arguments(Color.GRAY),
                arguments(Color.DARK_GRAY),
                arguments(Color.GREEN),
                arguments(Color.WHITE),
                arguments(Color.BLACK)
        );
    }

    private static Stream<Arguments> generateIllegalHexadecimalColor() {
        return Stream.of(
                null,
                "",
                "#1234545623456",
                "ffffff",
                "mu"
        ).map(Arguments::arguments);
    }

    @ParameterizedTest
    @MethodSource("generateColor")
    void testColorConverter(Color color) throws ParseException {
        assertEquals(color, ColorConverter.getColor(ColorConverter.getColorHexadecimal(color)));
    }

    @ParameterizedTest
    @MethodSource("generateIllegalHexadecimalColor")
    void testIllegalHexadecimalColors(String colorHexadecimal) {
        assertThrows(ParseException.class, () -> ColorConverter.getColor(colorHexadecimal));
    }
}
