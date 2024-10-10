package de.algoviz.algoviz.model.graph_general.graph;

import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class GraphTest {

    private static Stream<Arguments> generateGraph() {
        return Stream.of(
                arguments(getGraph())
        );
    }

    // should not be changed because the graph is used for several tests
    public static Graph getGraph() {
        List<Node> nodes = List.of(
                new Node(0, new Coordinates(-5.0, 0.0), "1"),
                new Node(1, new Coordinates(1.5, 0.1), ""),
                new Node(2, new Coordinates(-3.4, 5.0), "3")
        );
        List<Edge> edges = List.of(
                new Edge(0, nodes.get(0), nodes.get(1), 2),
                new Edge(1,nodes.get(2), nodes.get(1), 2)
        );
        return new Graph(nodes, edges, true, true);
    }

    private static List<Coordinates> getCoordinatesScaled() {

        return List.of(
                new Coordinates(0.0, 0.5),
                new Coordinates(0.65, 0.51),
                new Coordinates(0.16, 1.0)
        );
    }


    @ParameterizedTest
    @MethodSource("generateGraph")
    void testClone(Graph graph) {
        Graph graphScaled = graph.clone();
        assertNotSame(graphScaled, graph);
        assertEquals(graph, graphScaled);
    }


    @Test
    void testScale() {
        // The coordinates of the graph should be scaled
        Collection<Node> nodes = getGraph().getNodes();
        List<Coordinates> coordinatesScaled = getCoordinatesScaled();
        for (Node node : nodes) {
            assertEquals(node.getNodeProperties().getCoordinates(), coordinatesScaled.get(node.getId()));
        }
    }
}
