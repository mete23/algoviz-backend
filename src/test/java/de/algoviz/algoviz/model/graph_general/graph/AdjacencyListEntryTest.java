package de.algoviz.algoviz.model.graph_general.graph;

import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdjacencyListEntryTest {

    @Test
    void testCreateAdjacencyList() {

        // initialize graph and get adjacency list
        Graph graph = GraphTest.getGraph();
        Map<Integer, Node> nodes = graph.getNodesMap();
        Map<Integer, AdjacencyListEntry> adjacencyList = graph.getAdjacencyListMap();

        // size of adjacency list should be number of nodes
        assertSame(nodes.size(), adjacencyList.size());

        // test entries of adjacency list
        for (Map.Entry<Integer, AdjacencyListEntry> entry : adjacencyList.entrySet()) {

            Node node = nodes.get(entry.getKey());

            // check if entry node is correct
            assertEquals(node, entry.getValue().getEntryNode());

            List<AdjacencyListEntry> adjacencyEntries = entry.getValue().getAdjacencyEntries();
            List<Edge> edges = entry.getValue().getEdgesAdjacencyNodes();

            assertSame(adjacencyEntries.size(), edges.size());

            // node 0 and 2 are adjacency to node 1 and their weight is 2
            if (node.getId() == 0 || node.getId() == 2) {
                assertSame(1, adjacencyEntries.size());
                assertEquals(2.0, edges.get(0).getWeight(), 0.00001);
                assertTrue(adjacencyEntries.contains(adjacencyList.get(1)));

            } else { // there are no other edges
                assertTrue(adjacencyEntries.isEmpty());
            }
        }
    }

}
