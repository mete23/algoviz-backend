package de.algoviz.algoviz.model.algorithm.implementation;

import de.algoviz.algoviz.model.algorithm.AlgorithmInterface;
import de.algoviz.algoviz.model.algorithm.implementation.util.NodeEdgeValueTriple;
import de.algoviz.algoviz.model.algorithm.implementation.util.NodeValuePair;
import de.algoviz.algoviz.model.graph_general.graph.AdjacencyListEntry;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Dijkstra's Algorithm
 *
 * @author Tim
 * @version 1.0
 */
public class DijkstraAlgorithm implements AlgorithmInterface {

    private int availableSteps; // makes sure, that the algorithm terminates if the graph has a negative circle
    public static final String NAME = "Dijkstra's Algorithm";
    public static final String DESCRIPTION = NAME + " is a path finding algorithm for graphs without edges with negative weight.";
    private static final Color COLOR_VISITED = Color.BLUE;
    private static final Color COLOR_FINISHED = new Color(0,128,0);
    private static final Color COLOR_DEFAULT = Color.BLACK;

    private final Map<Integer, NodeEdgeValueTriple> visitedNodes = new HashMap<>();

    // if  shorter path to a node is found, the entry will be invalid. The number of invalid nodes must be stored in order to decide if there is a valid entry left.
    private int numberOfInvalidEntries = 0;
    private final PriorityQueue<NodeValuePair> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(NodeValuePair::value));

    @Override
    public void initialize(Graph graph) {
        Map<Integer, AdjacencyListEntry> adjacencyList = graph.getAdjacencyListMap();
        Node startingNode = graph.getStartingNode();
        if (!Objects.isNull(startingNode) && !graphHasNegativeEdge(graph)) {
            visitNodeIfUnvisited(adjacencyList.get(startingNode.getId()), Optional.empty(), 0);
        }
        availableSteps = adjacencyList.size() + 1;
    }

    private boolean graphHasNegativeEdge(Graph graph) {
        for (Edge edge : graph.getEdges()) {
            if (edge.getWeight() < 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void next() {

        NodeValuePair nodeEntry = priorityQueue.poll();
        if (!entryIsValid(nodeEntry)) { // current entry is not valid
            numberOfInvalidEntries--;
            next();
            return;
        }
        availableSteps--;
        AdjacencyListEntry adjacencyListEntry = nodeEntry.adjacencyListEntry();
        List<AdjacencyListEntry> entries = adjacencyListEntry.getAdjacencyEntries();
        List<Edge> edges = adjacencyListEntry.getEdgesAdjacencyNodes();

        // change color of node and edge
        adjacencyListEntry.getEntryNode().getNodeProperties().setColor(COLOR_FINISHED);
        visitedNodes.get(adjacencyListEntry.getEntryNode().getId()).edge().ifPresent(edge -> edge.getEdgeProperties().setColor(COLOR_FINISHED));

        // visit adjacency nodes
        for (int i = 0; i < entries.size(); i++) {
            visitNodeIfUnvisited(entries.get(i), Optional.of(edges.get(i)), nodeEntry.value() + edges.get(i).getWeight());
        }
    }

    // if an entry of the priority queue is not valid the value has changed
    private boolean entryIsValid(NodeValuePair nodeEntry) {
        return nodeEntry.value() == visitedNodes.get(nodeEntry.adjacencyListEntry().getEntryNode().getId()).value();
    }

    @Override
    // check if there is a valid entry left
    public boolean isDone() {
        return priorityQueue.size() <= numberOfInvalidEntries || availableSteps <= 0;
    }

    private void visitNodeIfUnvisited(AdjacencyListEntry adjacencyListEntry, Optional<Edge> edge, double value) {
        Node node = adjacencyListEntry.getEntryNode();
        NodeEdgeValueTriple triple = visitedNodes.get(node.getId());

        // node was not found
        if (Objects.isNull(triple)) {
            node.getNodeProperties().setColor(COLOR_VISITED);
            visitNode(adjacencyListEntry, edge, value);
            return;
        }

        // a shorter path to the node exists
        if (triple.value() <= value) {
            return;
        }

        // a shorter path to the node
        triple.edge().ifPresent(e -> e.getEdgeProperties().setColor(COLOR_DEFAULT));
        visitNode(adjacencyListEntry, edge, value);
        numberOfInvalidEntries++;
    }

    private void visitNode(AdjacencyListEntry adjacencyListEntry, Optional<Edge> edge, double value) {

        Node node = adjacencyListEntry.getEntryNode();
        visitedNodes.put(node.getId(), new NodeEdgeValueTriple(node, edge, value));
        node.getNodeProperties().setLabel(String.valueOf(value));
        priorityQueue.add(new NodeValuePair(adjacencyListEntry, value));

        // the starting node has no edge
        edge.ifPresent(e -> e.getEdgeProperties().setColor(COLOR_VISITED));
    }
}