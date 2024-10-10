package de.algoviz.algoviz.external.graph;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.parser.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphExternalTest {

    @ParameterizedTest
    @MethodSource("generateGraph")
    void testGraphExternal(Graph graph) throws ParseException {

        GraphExternal external = new GraphExternal(graph);
        Graph graphBack = external.createGraph();
        assertEquals(graph, graphBack);

    }

    private static Stream<Arguments> generateGraph() {
        Node node1 = new Node(1, new Coordinates(2), "node1");
        Node node2 = new Node(new Coordinates(2), "node2");
        Edge edge = new Edge(node1, node2);
        Graph graph = new Graph(List.of(node1, node2), List.of(edge), false, false);
        graph.addStartingNode(node1);
        return Stream.of(
               graph
        ).map(Arguments::arguments);
    }
}
