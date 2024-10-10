package de.algoviz.algoviz.model.algorithm;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modification.changes.EdgeChanges;
import de.algoviz.algoviz.model.graph_general.modification.changes.NodeChanges;
import de.algoviz.algoviz.model.graph_general.modifications.graph.IGraphModification;
import de.algoviz.algoviz.model.session.UserSession;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class AlgorithmTest {

    @ParameterizedTest
    @MethodSource("generateResultOfAlgorithms")
    void testAlgorithmResult(AlgorithmGenerator algorithmGenerator, Graph graph, Graph expectedResult, int expectedSteps) {
        UserSession session = new UserSession();
        session.setGraph(graph);
        AlgorithmManager algorithmManager = session.getAlgorithmManager();
        algorithmManager.setAlgorithm(algorithmGenerator.generate());
        algorithmManager.setGenerator(algorithmGenerator);
        algorithmManager.startAlgorithm(session.getGraph());

        // wait till executed
        for (int i = 0; !algorithmManager.algorithmHasBeenExecuted(); i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // algorithm does not terminate
            assertTrue(i < 100);
        }

        // execute algorithm twice to check if reset animation works
        for (int i = 0; i < 2; i++) {
            Graph unchangedGraph = graph.clone();

            // algorithm should not change the graph while executing
            assertEquals(graph, unchangedGraph);

            assertSame(expectedSteps + 1, algorithmManager.getNumberOfSteps()); // +1 for initial graph

            // apply changes of algorithm to graph
            while (algorithmManager.algorithmHasNext()) {

                // get next step
                ModificationStep step = algorithmManager.getNextStepOfAlgorithm();

                // should return last step, which is equal to the current step
                assertEquals(step, algorithmManager.getLastStep());

                // should return the current step again
                assertEquals(step, algorithmManager.getNextStepOfAlgorithm());

                // apply changes
                for (IGraphModification modification : step.getGraphModificationList()) {
                    modification.applyChange(graph);
                }
                for (Map.Entry<Integer, NodeChanges> changedNode : step.getChangedNodes().entrySet()) {
                    changedNode.getValue().applyChanges(graph.getNodesMap().get(changedNode.getKey()));
                }
                for (Map.Entry<Integer, EdgeChanges> changedEdge : step.getChangedEdges().entrySet()) {
                    changedEdge.getValue().applyChanges(graph.getEdgesMap().get(changedEdge.getKey()));
                }
            }
            assertEquals(expectedResult, graph);
            session.resetAnimation();
        }
        assertEquals(algorithmGenerator, algorithmManager.getGenerator());
    }

    @ParameterizedTest
    @MethodSource("generateAlgorithms")
    void testAlgorithmTerminates(AlgorithmGenerator algorithmGenerator, Graph graph, int minExecutionSteps, int maxExecutionSteps) {

        AlgorithmInterface algorithm = algorithmGenerator.generate();
        int executionSteps = 0;

        algorithm.initialize(graph);

        while (!algorithm.isDone()) {
            executionSteps++;
            assertTrue(executionSteps <= maxExecutionSteps);
            algorithm.next();
        }
        assertTrue(executionSteps >= minExecutionSteps);
    }

    private static Stream<Arguments> generateAlgorithms() {

        int numberOfEdges = getGraphWithStartingNode().getEdges().size();

        return Stream.of(

                // test on a regular graph
                arguments(AlgorithmGenerator.BFS, getGraphWithStartingNode(), 4, 4),
                arguments(AlgorithmGenerator.DFS, getGraphWithStartingNode(), 7, 7),
                arguments(AlgorithmGenerator.DIJKSTRA, getGraphWithStartingNode(), 4, 4),
                arguments(AlgorithmGenerator.KRUSKAL, getGraphWithStartingNode(), numberOfEdges - 1, numberOfEdges - 1),
                arguments(AlgorithmGenerator.LABEL_PROPAGATION, getGraphWithStartingNode(), 1, 200),

                // should pick a random node as starting node
                arguments(AlgorithmGenerator.BFS, getGraphWithoutStartingNode(), 0, 4),
                arguments(AlgorithmGenerator.DFS, getGraphWithoutStartingNode(), 0, 7),
                arguments(AlgorithmGenerator.DIJKSTRA, getGraphWithoutStartingNode(), 0, 6),

                // should terminate without doing anything
                arguments(AlgorithmGenerator.BFS, getEmptyGraph(), 0, 0),
                arguments(AlgorithmGenerator.DFS, getEmptyGraph(), 0, 0),
                arguments(AlgorithmGenerator.DIJKSTRA, getEmptyGraph(), 0, 0),
                arguments(AlgorithmGenerator.KRUSKAL, getEmptyGraph(), 0, 0),
                arguments(AlgorithmGenerator.LABEL_PROPAGATION, getEmptyGraph(), 0, 1) // one step to check if label possible
        );
    }

    private static Stream<Arguments> generateResultOfAlgorithms() {

        return Stream.of(
                arguments(AlgorithmGenerator.BFS, getGraphWithStartingNode(), getExpectedGraphBFS(), 5),
                arguments(AlgorithmGenerator.DFS, getGraphWithStartingNode(1), getExpectedGraphDFS(), 6),
                arguments(AlgorithmGenerator.DIJKSTRA, getGraphWithStartingNode(), getExpectedGraphDijkstra(), 5),
                arguments(AlgorithmGenerator.KRUSKAL, getGraphWithStartingNode(), getExpectedGraphKruskal(), getGraphWithStartingNode().getEdges().size()),
                arguments(AlgorithmGenerator.LABEL_PROPAGATION, getGraphLabelPropagation(), getExpectedGraphKLabelPropagation(), 4)
        );
    }

    private static Graph getGraphWithStartingNode(int idStartingNode) {
        Graph graph = getGraphWithoutStartingNode();
        graph.addStartingNode(graph.getNodesMap().get(idStartingNode));
        return graph;
    }

    private static Graph getGraphWithStartingNode() {
        return getGraphWithStartingNode(0);
    }

    private static Graph getGraphWithoutStartingNode() {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nodes.add(new Node(i, new Coordinates(i, i % 3), ""));
        }
        List<Edge> edges = List.of(
                new Edge(0, nodes.get(0), nodes.get(1), 100),
                new Edge(1, nodes.get(1), nodes.get(2), 2),
                new Edge(2, nodes.get(2), nodes.get(3), 3.1),
                new Edge(3, nodes.get(3), nodes.get(1), 4),
                new Edge(4, nodes.get(0), nodes.get(2), 5)
        );

        return new Graph(nodes, edges, true, true);
    }

    public static Graph getExpectedGraphBFS() {
        Graph graph = getGraphWithStartingNode();
        Map<Integer, Node> nodesMap = graph.getNodesMap();
        Map<Integer, Edge> edgesMap = graph.getEdgesMap();

        nodesMap.get(0).getNodeProperties().setColor(new Color(0, 128, 0));
        nodesMap.get(1).getNodeProperties().setColor(new Color(0, 128, 0));
        nodesMap.get(2).getNodeProperties().setColor(new Color(0, 128, 0));
        nodesMap.get(3).getNodeProperties().setColor(new Color(0, 128, 0));

        nodesMap.get(0).getNodeProperties().setLabel("0");
        nodesMap.get(1).getNodeProperties().setLabel("1");
        nodesMap.get(2).getNodeProperties().setLabel("1");
        nodesMap.get(3).getNodeProperties().setLabel("2");

        edgesMap.get(0).getEdgeProperties().setColor(new Color(0, 128, 0));
        edgesMap.get(2).getEdgeProperties().setColor(new Color(0, 128, 0));
        edgesMap.get(4).getEdgeProperties().setColor(new Color(0, 128, 0));

        return graph;
    }

    public static Graph getExpectedGraphDFS() {
        Graph graph = getGraphWithStartingNode(1);
        Map<Integer, Node> nodesMap = graph.getNodesMap();
        Map<Integer, Edge> edgesMap = graph.getEdgesMap();

        nodesMap.get(1).getNodeProperties().setColor(new Color(0, 128, 0));
        nodesMap.get(2).getNodeProperties().setColor(new Color(0, 128, 0));
        nodesMap.get(3).getNodeProperties().setColor(new Color(0, 128, 0));

        nodesMap.get(1).getNodeProperties().setLabel("0");
        nodesMap.get(2).getNodeProperties().setLabel("1");
        nodesMap.get(3).getNodeProperties().setLabel("2");

        edgesMap.get(1).getEdgeProperties().setColor(new Color(0, 128, 0));
        edgesMap.get(2).getEdgeProperties().setColor(new Color(0, 128, 0));

        return graph;
    }

    public static Graph getExpectedGraphDijkstra() {
        Graph graph = getGraphWithStartingNode();
        Map<Integer, Node> nodesMap = graph.getNodesMap();
        Map<Integer, Edge> edgesMap = graph.getEdgesMap();

        nodesMap.get(0).getNodeProperties().setColor(new Color(0, 128, 0));
        nodesMap.get(1).getNodeProperties().setColor(new Color(0, 128, 0));
        nodesMap.get(2).getNodeProperties().setColor(new Color(0, 128, 0));
        nodesMap.get(3).getNodeProperties().setColor(new Color(0, 128, 0));

        nodesMap.get(0).getNodeProperties().setLabel("0.0");
        nodesMap.get(1).getNodeProperties().setLabel("12.1");
        nodesMap.get(2).getNodeProperties().setLabel("5.0");
        nodesMap.get(3).getNodeProperties().setLabel("8.1");

        edgesMap.get(2).getEdgeProperties().setColor(new Color(0, 128, 0));
        edgesMap.get(3).getEdgeProperties().setColor(new Color(0, 128, 0));
        edgesMap.get(4).getEdgeProperties().setColor(new Color(0, 128, 0));

        return graph;
    }

    public static Graph getExpectedGraphKruskal() {
        Graph graph = getGraphWithStartingNode();
        Map<Integer, Edge> edgesMap = graph.getEdgesMap();

        edgesMap.get(0).getEdgeProperties().setColor(Color.GRAY);
        edgesMap.get(1).getEdgeProperties().setColor(new Color(0, 128, 0));
        edgesMap.get(2).getEdgeProperties().setColor(new Color(0, 128, 0));
        edgesMap.get(3).getEdgeProperties().setColor(Color.GRAY);
        edgesMap.get(4).getEdgeProperties().setColor(new Color(0, 128, 0));

        return graph;
    }

    private static Graph getGraphLabelPropagation() {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nodes.add(new Node(i, new Coordinates(0, 0)));
        }
        List<Edge> edges = List.of(
                new Edge(0, nodes.get(0), nodes.get(1), 1),
                new Edge(1, nodes.get(1), nodes.get(2), 10),
                new Edge(2, nodes.get(2), nodes.get(3), 5),
                new Edge(3, nodes.get(3), nodes.get(4), 6)
        );
        return new Graph(nodes, edges, false, true);
    }

    public static Graph getExpectedGraphKLabelPropagation() {
        Graph graph = getGraphLabelPropagation();
        Map<Integer, Node> nodesMap = graph.getNodesMap();

        nodesMap.get(0).getNodeProperties().setLabel("3");
        nodesMap.get(1).getNodeProperties().setLabel("3");
        nodesMap.get(2).getNodeProperties().setLabel("3");
        nodesMap.get(3).getNodeProperties().setLabel("4");
        nodesMap.get(4).getNodeProperties().setLabel("4");

        nodesMap.get(0).getNodeProperties().setColor(new Color(109, 178, 192));
        nodesMap.get(1).getNodeProperties().setColor(new Color(109, 178, 192));
        nodesMap.get(2).getNodeProperties().setColor(new Color(109, 178, 192));
        nodesMap.get(3).getNodeProperties().setColor(new Color(71, 88, 194));
        nodesMap.get(4).getNodeProperties().setColor(new Color(71, 88, 194));
        return graph;
    }

    private static Graph getEmptyGraph() {
        return new Graph(List.of(), List.of(), true, true);
    }
}
