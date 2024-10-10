package de.algoviz.algoviz.model.log_file;

import de.algoviz.algoviz.model.algorithm.AlgorithmInterface;
import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modifications.edge.EdgeColorModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.ColorNodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.CoordinatesNodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.LabelNodeModification;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LogFileToAlgorithmTest {
    private static final Node node1 = new Node(new Coordinates(5, 7), "1");
    private static final Node node2 = new Node(new Coordinates(4, 4), "2");
    private static final Node node3 = new Node(new Coordinates(1, 6), "3");
    private static final Node node4 = new Node(new Coordinates(6, 1), "4");

    private static final Edge edge1 = new Edge(node1, node2, 10);
    private static final Edge edge2 = new Edge(node2, node4, 1);
    private static final Edge edge3 = new Edge(node4, node1, -5.3);
    private static final Edge edge4 = new Edge(node3, node2, Math.PI);

    private static final Graph GRAPH = new Graph(List.of(node1, node2, node3, node4), List.of(edge1, edge2, edge3, edge4), true, true);


    @Test
    public void testLogFileToAlgorithm() {
        List<ModificationStep> modificationSteps = List.of(
                new ModificationStep());
        modificationSteps.get(0).addModification(edge1, new EdgeColorModification(Color.BLUE, edge1.getEdgeProperties().getColor()));
        modificationSteps.get(0).addModification(edge2, new EdgeColorModification(Color.RED, edge2.getEdgeProperties().getColor()));
        modificationSteps.get(0).addModification(node1, new ColorNodeModification(Color.BLUE, node1.getNodeProperties().getColor()));
        modificationSteps.get(0).addModification(node2, new CoordinatesNodeModification(new Coordinates(10, 10), node1.getNodeProperties().getCoordinates()));
        modificationSteps.get(0).addModification(node1, new LabelNodeModification("100", node1.getNodeProperties().getLabel()));
        Graph graphIntern = GRAPH.clone();
        Graph assertGraph = GRAPH.clone();
        List<Node> nodes = assertGraph.getNodes().stream().toList();
        List<Edge> edges = assertGraph.getEdges().stream().toList();

        Node assertNode1 = nodes.get(nodes.indexOf(node1));
        Node assertNode2 = nodes.get(nodes.indexOf(node2));

        Edge assertEdge1 = edges.get(edges.indexOf(edge1));
        Edge assertEdge2 = edges.get(edges.indexOf(edge2));

        assertNode1.getNodeProperties().setColor(Color.BLUE);
        assertNode1.getNodeProperties().setLabel("100");
        assertNode2.getNodeProperties().setCoordinates(new Coordinates(10, 10));

        assertEdge2.getEdgeProperties().setColor(Color.RED);
        assertEdge1.getEdgeProperties().setColor(Color.BLUE);

        LogFile logFile = new LogFile(graphIntern, modificationSteps);
        AlgorithmInterface algorithm = new LogFileToAlgorithm(logFile);
        algorithm.initialize(graphIntern);
        while (!algorithm.isDone()) {
            algorithm.next();
        }

        assertEquals(assertGraph, graphIntern);
    }
}