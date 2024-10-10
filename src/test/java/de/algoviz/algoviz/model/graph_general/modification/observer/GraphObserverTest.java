package de.algoviz.algoviz.model.graph_general.modification.observer;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.GraphTest;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modification.changes.EdgeChanges;
import de.algoviz.algoviz.model.graph_general.modification.changes.NodeChanges;
import de.algoviz.algoviz.model.graph_general.modification.observers.GraphObserver;
import de.algoviz.algoviz.model.graph_general.modifications.edge.EdgeColorModification;
import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;
import de.algoviz.algoviz.model.graph_general.modifications.graph.*;
import de.algoviz.algoviz.model.graph_general.modifications.node.ColorNodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.CoordinatesNodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.LabelNodeModification;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GraphObserverTest {

    // This test tests if the observer will be notified if a node or an edge is changed
    @Test
    void testNotifyNodeAndEdgeObserver() {

        // initialize graph and Observer
        ModificationStep modificationStep = new ModificationStep();
        GraphObserver observer = new GraphObserver(modificationStep);
        Graph graph = GraphTest.getGraph();
        graph.addObserver(observer);

        Map<Integer, Node> nodesMap = graph.getNodesMap();

        // change color and label of first node
        nodesMap.get(0).getNodeProperties().setColor(Color.MAGENTA);
        nodesMap.get(0).getNodeProperties().setLabel("Something");

        // check if observer was updated
        Map<Integer, NodeChanges> changedNodes = modificationStep.getChangedNodes();
        assertSame(1, changedNodes.size());
        NodeChanges nodeModificationList = changedNodes.get(0);
        assertNotNull(nodeModificationList);
        List<INodeModification> nodeModifications = nodeModificationList.getModificationList();
        assertSame(2, nodeModifications.size());
        INodeModification colorNodeModification = nodeModifications.get(0);
        INodeModification labelNodeModification = nodeModifications.get(1);
        assertEquals(colorNodeModification, new ColorNodeModification(Color.MAGENTA, Color.BLACK));
        assertEquals(labelNodeModification, new LabelNodeModification("Something", "1"));

        // change coordinates of second node
        Coordinates expectedCoordinates = nodesMap.get(1).getNodeProperties().getCoordinates().clone();
        expectedCoordinates.setCoordinate(0, Math.PI);
        Coordinates oldCoordinates = nodesMap.get(1).getNodeProperties().getCoordinates().clone();
        nodesMap.get(1).getNodeProperties().getCoordinates().setCoordinate(0, Math.PI);

        // check if observer was updated
        assertSame(2, changedNodes.size());
        nodeModificationList = changedNodes.get(1);
        assertNotNull(nodeModificationList);
        nodeModifications = nodeModificationList.getModificationList();
        assertSame(1, nodeModifications.size());
        assertEquals(nodeModifications.get(0), new CoordinatesNodeModification(expectedCoordinates, oldCoordinates));

        //change color of first edge
        graph.getEdgesMap().get(0).getEdgeProperties().setColor(Color.MAGENTA);

        // check if observer was updated
        Map<Integer, EdgeChanges> changedEdges = modificationStep.getChangedEdges();
        assertSame(1, changedEdges.size());
        EdgeChanges edgeModificationList = changedEdges.get(0);
        assertNotNull(edgeModificationList);
        List<IEdgeModification> edgeModifications = edgeModificationList.getModifications();
        assertSame(1, edgeModifications.size());
        assertEquals(edgeModifications.get(0), new EdgeColorModification(Color.MAGENTA, Color.BLACK));
    }

    @Test
    void testNotifyGraphObserver() {

        // initialize graph and Observer
        ModificationStep modificationStep = new ModificationStep();
        GraphObserver observer = new GraphObserver(modificationStep);

        Graph graph = GraphTest.getGraph();
        Graph clone = graph.clone();
        assertEquals(graph, clone);

        graph.addObserver(observer);

        Node nodeToRemove = graph.getNodesMap().get(0);
        Node nodeToAdd = new Node(new Coordinates(-0.01, 99.99), "new Node");
        Edge edgeToRemove = graph.getEdgesMap().get(0);
        Edge edgeToAdd = new Edge(graph.getNodesMap().get(1), graph.getNodesMap().get(2));

        List<Edge> removedEdges = graph.getAdjacencyListMap().get(nodeToRemove.getId()).getEdgesAdjacencyNodes();


        graph.removeNode(nodeToRemove);
        graph.addNode(nodeToAdd);
        graph.removeEdge(edgeToRemove);
        graph.addEdge(edgeToAdd);

        List<IGraphModification> graphModifications = modificationStep.getGraphModificationList();
        assertSame(4, graphModifications.size());

        assertTrue(graphModifications.contains(new GraphAddNodeModification(nodeToAdd)));
        assertTrue(graphModifications.contains(new GraphRemoveNodeModification(nodeToRemove, removedEdges)));
        assertTrue(graphModifications.contains(new GraphAddEdgeModification(edgeToAdd)));
        assertTrue(graphModifications.contains(new GraphRemoveEdgeModification(edgeToRemove)));

        for (IGraphModification modification : graphModifications) {
            modification.applyChange(clone);
        }
        assertEquals(graph, clone);
    }

    @Test
    void testApplyChanges() {

        // initialize graph and Observer
        ModificationStep modificationStep = new ModificationStep();
        GraphObserver observer = new GraphObserver(modificationStep);
        Graph graph = GraphTest.getGraph();
        graph.addObserver(observer);

        Graph clone = graph.clone();

        Map<Integer, Node> nodesMap = graph.getNodesMap();
        Map<Integer, Edge> edgesMap = graph.getEdgesMap();

        Map<Integer, Node> cloneNodesMap = clone.getNodesMap();
        Map<Integer, Edge> cloneEdgesMap = clone.getEdgesMap();

        // apply changes to nodes
        nodesMap.get(0).getNodeProperties().setColor(Color.MAGENTA);
        nodesMap.get(1).getNodeProperties().setLabel("Some Label");
        nodesMap.get(0).getNodeProperties().getCoordinates().setCoordinate(0, Math.E);

        // apply changes to edges
        edgesMap.get(0).getEdgeProperties().setColor(Color.MAGENTA);

        // the graph should have changed
        assertNotEquals(graph, clone);

        // apply the changes to the graph
        for (Map.Entry<Integer, NodeChanges> changedNode : modificationStep.getChangedNodes().entrySet()) {
            changedNode.getValue().applyChanges(cloneNodesMap.get(changedNode.getKey()));
        }

        for (Map.Entry<Integer, EdgeChanges> changedEdge : modificationStep.getChangedEdges().entrySet()) {
            changedEdge.getValue().applyChanges(cloneEdgesMap.get(changedEdge.getKey()));
        }

        // the graph and its clone should be equal
        assertEquals(graph, clone);
    }
}
