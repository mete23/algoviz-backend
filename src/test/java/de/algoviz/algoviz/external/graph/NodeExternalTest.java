package de.algoviz.algoviz.external.graph;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NodeExternalTest {

    @Test
    void testCreateExternal() throws ParseException {
        Node node = new Node(new Coordinates(2), "-1");
        NodeExternal nodeExternal = new NodeExternal(node, true);
        assertEquals(node, nodeExternal.createNode());
    }

    @ParameterizedTest
    @MethodSource("generateNode")
    void testCreateExternal(Node node) throws ParseException {

        for (int i = 0; i < 2; i++) {
            boolean isStartingNode = i % 2 == 0;

            // create external
            NodeExternal nodeExternal = new NodeExternal(node, isStartingNode);

            // test properties of external
            assertEquals(node.getId(), nodeExternal.getId());
            assertEquals(isStartingNode, nodeExternal.isStartingNode());
            assertArrayEquals(node.getNodeProperties().getCoordinates().getCoordinatesArray(), nodeExternal.getCoordinates());
            assertEquals(node.getNodeProperties().getLabel(), nodeExternal.getLabel());
            assertEquals(node.getNodeProperties().getColor(), Color.decode(nodeExternal.getColor()));

            // create node from external
            assertEquals(node, nodeExternal.createNode());
        }
    }

    private static Stream<Arguments> generateNode() {
        return Stream.of(
                new Node(new Coordinates(2)),
                new Node(1, new Coordinates(Math.PI, -1 * Math.E), "a Label")
        ).map(Arguments::arguments);
    }

}
