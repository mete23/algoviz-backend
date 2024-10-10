package de.algoviz.algoviz.model.graph_general.graph.node;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class NodeTest {

    private static Stream<Arguments> generateNodes() {
        return Stream.of(
                arguments(new Node(new Coordinates(0.0, 0.0), ""))
        );
    }

    @ParameterizedTest
    @MethodSource("generateNodes")
    void testClone(Node node) {
        Node clonedNode = node.clone();
        assertNotSame(node, clonedNode);
        assertEquals(node, clonedNode);

        // change label
        String label = node.getNodeProperties().getLabel();
        node.getNodeProperties().setLabel(label + ":)");
        assertNotEquals(node, clonedNode);
        node.getNodeProperties().setLabel(label);
        assertEquals(node, clonedNode);

        //change color
        node.getNodeProperties().setColor(Color.BLUE);
        assertNotEquals(node, clonedNode);
    }
}


