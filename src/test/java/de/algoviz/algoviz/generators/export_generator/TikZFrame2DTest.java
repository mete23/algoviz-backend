package de.algoviz.algoviz.generators.export_generator;

import de.algoviz.algoviz.model.algorithm.AlgorithmInterface;
import de.algoviz.algoviz.model.algorithm.AlgorithmManager;
import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@SpringBootTest
public class TikZFrame2DTest {
    private static final String TIKZ_BIG = """
            \\usetikzlibrary{arrows, automata}
            \\begin{tikzpicture}[- >, >= stealth', node distance = 2.8 cm, thick]
            \\node[state, draw={rgb:red,255;green,200;blue,0}, fill={rgb:red,255;green,200;blue,0}, fill opacity=0.25, text opacity = 1, accepting] (1) at(0,18) {1};
            \\node[state, draw=black, fill=black, fill opacity=0.25, text opacity = 1] (2) at(12,0) {-2};
                        
            \\draw[->] (1) edge[above, draw={rgb:red,255;green,0;blue,0}] node{10.0} (2);
                        
            \\end{tikzpicture}""";
    private static final String TIKZ_COMPACT = """
            \\usetikzlibrary{arrows, automata}
            \\begin{tikzpicture}[- >, >= stealth', node distance = 2.8 cm, thick]
            \\node[circle, inner sep=4pt, fill={rgb:red,255;green,200;blue,0}, accepting] (1) at(0,18) {};
            \\node[circle, inner sep=4pt, fill=black] (2) at(12,0) {};
                        
            \\draw[->] (1) edge[above, draw={rgb:red,255;green,0;blue,0}] (2);
                        
            \\end{tikzpicture}""";

    @ParameterizedTest
    @MethodSource("generateGraphAndTikZ")
    void testTikZ(Graph graph, String tikZ, ExportFrameInterface exportFrame) {

        exportFrame.generateExportData(graph);

        String output = exportFrame.toString().replace("\n", "").replace("\r", "");
        String expected = tikZ.replace("\n", "").replace("\r", "");

        assertEquals(expected, output);
    }

    @ParameterizedTest
    @MethodSource("generateGraphAndTikZ")
    void testTikZAlgorithmManager(Graph graph, String tikZ, ExportFrameInterface exportFrame) {

        AlgorithmManager manager = new AlgorithmManager();

        // use dummy algorithm
        manager.setAlgorithm(new AlgorithmInterface() {
            @Override
            public void initialize(Graph graph) {

            }

            @Override
            public void next() {

            }

            @Override
            public boolean isDone() {
                return true;
            }
        });

        // execute algorithm
        manager.startAlgorithm(graph);

        // wait till executed
        while (!manager.algorithmHasBeenExecuted()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // one step for graph and one step to initialize algorithm
        assertSame(2, manager.getNumberOfSteps());
        List<ExportFrameInterface> frames = manager.exportAlgorithm(exportFrame).getFrameList();
        assertSame(2, frames.size());

        // algorithm has not changed the graph
        assertEquals(frames.get(0).toString(), frames.get(1).toString());

        // check result
        String output = frames.get(0).toString().replace("\n", "").replace("\r", "");
        String expected = tikZ.replace("\n", "").replace("\r", "");

        assertEquals(expected, output);
    }

    private static Stream<Arguments> generateGraphAndTikZ() {

        return Stream.of(
                arguments(getGraph(), TIKZ_BIG, new TikZFrame2dBigStatement()),
                arguments(getGraph(), TIKZ_COMPACT, new TikZFrame2dCompactStatement()));
    }

    private static Graph getGraph() {
        Node node1 = new Node(1, new Coordinates(0, 0), "1");
        Node node2 = new Node(2, new Coordinates(1, 1), "-2");
        Edge edge1 = new Edge(node1, node2, 10);
        node1.getNodeProperties().setColor(Color.ORANGE);
        node2.getNodeProperties().setColor(Color.BLACK);
        edge1.getEdgeProperties().setColor(Color.RED);
        java.util.List<Node> nodes = java.util.List.of(node1, node2);
        java.util.List<Edge> edges = List.of(edge1);

        Graph graph = new Graph(nodes, edges, true, true);

        graph.addStartingNode(node1);
        return graph;
    }
}
