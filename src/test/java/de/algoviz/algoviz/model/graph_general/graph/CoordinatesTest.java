package de.algoviz.algoviz.model.graph_general.graph;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class CoordinatesTest {

    private static Coordinates generateCoordinates(double... values) {
        Coordinates c = new Coordinates(values.length);
        for (int i = 0; i < values.length; i++) {
            c.setCoordinate(i, values[i]);
        }
        return c;
    }

    private static Stream<Arguments> generateCoordinates() {
        return Stream.of(
                arguments(new double[]{1.0}),
                arguments(new double[]{0, -0.123}),
                arguments(new double[]{10.60, -10.98, Math.PI}),
                arguments(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 10})
    void testCoordinatesSize(int number) {
        assertSame(number, new Coordinates(number).getNumberOfCoordinates());
    }

    @ParameterizedTest
    @MethodSource("generateCoordinates")
    void testValues(double[] values) {

        int number = values.length;

        // initialize coordinates
        Coordinates coordinates = generateCoordinates(values);
        assertEquals(coordinates, new Coordinates(values));

        //test values of coordinates
        for (int i = 0; i < number; i++) {
            assertEquals(values[i], coordinates.getCoordinate(i), 0.00001);
        }
    }

    @ParameterizedTest
    @MethodSource("generateCoordinates")
    void testClone(double[] values) {
        Coordinates coordinate = generateCoordinates(values);
        Coordinates coordinateClone = coordinate.clone();
        assertNotSame(coordinate, coordinateClone);
        assertEquals(coordinate, coordinateClone);
    }

    @Test
    void testScale() {
        Coordinates coordinate = generateCoordinates(0.0, 0.5, -1.0, 1.2);
        Coordinates scaledCoordinate = coordinate.clone();
        assertEquals(coordinate, scaledCoordinate);
        assertNotSame(coordinate, scaledCoordinate);

        // should not do anything
        coordinate.scale(0.0, 1.0);
        assertEquals(coordinate, scaledCoordinate);

        // double values
        scaledCoordinate = generateCoordinates(0.0, 1.0, -2.0, 2.4);
        coordinate.scale(0.0, 0.5);
        assertEquals(coordinate, scaledCoordinate);

        // scale coordinates to value between 0 and 1
        scaledCoordinate = generateCoordinates(2.0 / 4.4, 3.0 / 4.4, 0.0, 1.0);
        coordinate.scale(-2.0, 2.4);
        assertEquals(coordinate, scaledCoordinate);

        // test equal values
        coordinate.scale(Math.PI, Math.PI);
        assertEquals(coordinate, new Coordinates(0, 0, 0, 0));

        // test not allowed input
        coordinate.scale(-100.0, -100.01);
        assertEquals(coordinate, new Coordinates(0, 0, 0, 0));

    }
}

