package de.algoviz.algoviz.model.graph_general.graph.edge;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class EdgeTest {

    private static Stream<Arguments> generateEdges() {
        return Stream.of(
                arguments(new Edge(new Node(new Coordinates(1, 1)), new Node((new Coordinates(2, 3)))))
        );
    }

    @ParameterizedTest
    @MethodSource("generateEdges")
    void testClone(Edge edge) {
        Edge clonedEdge = edge.clone();
        assertNotSame(edge, clonedEdge);
        assertEquals(edge, clonedEdge);

        //change color
        edge.getEdgeProperties().setColor(Color.BLUE);
        assertNotEquals(edge, clonedEdge);
    }

    @ParameterizedTest
    @MethodSource("generateEdges")
    void testCloneNodeMap(Edge edge) {

        Map<Node, Node> pairOfNodes = new HashMap<>();
        pairOfNodes.put(edge.getFirstNode(), edge.getFirstNode().clone());
        pairOfNodes.put(edge.getSecondNode(), edge.getSecondNode().clone());
        Edge clonedEdge = edge.clone(pairOfNodes);
        assertNotSame(edge, clonedEdge);
        assertEquals(edge, clonedEdge);

        //change color
        edge.getEdgeProperties().setColor(Color.BLUE);
        assertNotEquals(edge, clonedEdge);
    }

}
