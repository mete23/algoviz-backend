package de.algoviz.algoviz.external.graph;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class EdgeExternalTest {

    @ParameterizedTest
    @MethodSource("generateEdge")
    void testCreateExternal(Edge edge, boolean weighted) throws ParseException {

        // create mapping
        Map<Integer, Node> nodesMap = new HashMap<>();
        nodesMap.put(edge.getFirstNode().getId(), edge.getFirstNode());
        nodesMap.put(edge.getSecondNode().getId(), edge.getSecondNode());

        // create external
        EdgeExternal edgeExternal = new EdgeExternal(edge);

        // test properties of external
        assertEquals(edge.getId(), edgeExternal.getId());
        assertEquals(edge.getFirstNode().getId(), edgeExternal.getFirstNodeId());
        assertEquals(edge.getSecondNode().getId(), edgeExternal.getSecondNodeId());
        assertEquals(edge.getWeight(), edgeExternal.getWeight());
        assertEquals(edge.getEdgeProperties().getColor(), Color.decode(edgeExternal.getColor()));

        // create edge from external
        assertEquals(edge, edgeExternal.createEdge(nodesMap, weighted));

    }

    @ParameterizedTest
    @MethodSource("generateEdge")
    void testCreateExternalWithoutNode(Edge edge, boolean weighted) {

        // create external
        EdgeExternal edgeExternal = new EdgeExternal(edge);

        // create mapping without first node
        Map<Integer, Node> nodesMap = new HashMap<>();
        nodesMap.put(edge.getFirstNode().getId(), edge.getFirstNode());

        // try to create edge
        assertThrows(ResponseStatusException.class, () -> edgeExternal.createEdge(nodesMap, weighted));

        // create mapping without second node
        nodesMap.clear();
        nodesMap.put(edge.getSecondNode().getId(), edge.getSecondNode());

        // try to create edge
        assertThrows(ResponseStatusException.class, () -> edgeExternal.createEdge(nodesMap, weighted));
    }

    private static Stream<Arguments> generateEdge() {
        return Stream.of(
                arguments(new Edge(new Node(new Coordinates(0, 0)), new Node(new Coordinates(1, 0))), false),
                arguments(new Edge(1, new Node(1, new Coordinates(0, 0)), new Node(2, new Coordinates(1, 0)), Math.PI), true)
        );
    }

}
