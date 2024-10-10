package de.algoviz.algoviz.model.algorithm.implementation;

import de.algoviz.algoviz.model.algorithm.AlgorithmInterface;
import de.algoviz.algoviz.model.graph_general.graph.AdjacencyListEntry;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Depth First Search Algorithm
 *
 * @author Tim
 * @version 1.0
 */
public class DFSAlgorithm implements AlgorithmInterface {

    public static final String NAME = "Depth First Search";
    public static final String DESCRIPTION = NAME + " is an algorithm for traversing a graph.";
    private static final Color COLOR_VISITED = Color.BLUE;
    private static final Color COLOR_FINISHED = new Color(0, 128, 0);

    private final Set<Integer> visitedNodeIdSet = new HashSet<>();
    private final Stack<AdjacencyListEntry> stack = new Stack<>();

    @Override
    public void initialize(Graph graph) {
        Map<Integer, AdjacencyListEntry> adjacencyList = graph.getAdjacencyListMap();
        Node startingNode = graph.getStartingNode();
        if (!Objects.isNull(startingNode)) {
            visitNodeIfUnvisited(adjacencyList.get(startingNode.getId()));
        }
    }

    @Override
    public void next() {

        AdjacencyListEntry adjacencyListEntry = stack.peek();
        List<AdjacencyListEntry> entries = adjacencyListEntry.getAdjacencyEntries();
        List<Edge> edges = adjacencyListEntry.getEdgesAdjacencyNodes();

        for (int i = 0; i < entries.size(); i++) {
            if (visitNodeFromEdgeIfUnvisited(entries.get(i), edges.get(i))) {
                return;
            }
        }
        adjacencyListEntry.getEntryNode().getNodeProperties().setColor(COLOR_FINISHED);
        stack.pop();
    }

    @Override
    public boolean isDone() {
        return stack.isEmpty();
    }

    private boolean visitNodeFromEdgeIfUnvisited(AdjacencyListEntry adjacencyListEntry, Edge edgeVisitedFrom) {
        if (visitNodeIfUnvisited(adjacencyListEntry)) {
            edgeVisitedFrom.getEdgeProperties().setColor(COLOR_FINISHED);
            return true;
        }
        return false;
    }

    private boolean visitNodeIfUnvisited(AdjacencyListEntry adjacencyListEntry) {

        Node node = adjacencyListEntry.getEntryNode();
        if (!visitedNodeIdSet.add(adjacencyListEntry.getEntryNode().getId())) {
            return false;
        }
        node.getNodeProperties().setLabel(String.valueOf(visitedNodeIdSet.size() - 1));
        node.getNodeProperties().setColor(COLOR_VISITED);

        stack.push(adjacencyListEntry);
        return true;
    }
}
