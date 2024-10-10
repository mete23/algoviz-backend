package de.algoviz.algoviz.model.algorithm.implementation;

import de.algoviz.algoviz.model.algorithm.AlgorithmInterface;
import de.algoviz.algoviz.model.algorithm.implementation.util.NodeValuePair;
import de.algoviz.algoviz.model.graph_general.graph.AdjacencyListEntry;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

/**
 * Breadth First Search Algorithm
 *
 * @author Tim
 * @version 1.0
 */
public class BFSAlgorithm implements AlgorithmInterface {

    public static final String NAME = "Breadth First Search";
    public static final String DESCRIPTION = NAME + " is a path search algorithm for unweighted graphs.";

    private static final Color COLOR_VISITED = Color.BLUE;
    private static final Color COLOR_FINISHED = new Color(0,128,0);;
    private final Set<Integer> visitedNodeIdSet = new HashSet<>();
    private final Queue<NodeValuePair> queue = new LinkedList<>();

    @Override
    public void initialize(Graph graph) {
        Map<Integer, AdjacencyListEntry> adjacencyList = graph.getAdjacencyListMap();
        Node startingNode = graph.getStartingNode();
        if (!Objects.isNull(startingNode)) {
            visitNodeIfUnvisited(adjacencyList.get(startingNode.getId()), 0);
        }
    }

    @Override
    public void next() {

        NodeValuePair nodeEntry = queue.poll();
        AdjacencyListEntry adjacencyListEntry = nodeEntry.adjacencyListEntry();
        List<AdjacencyListEntry> entries = adjacencyListEntry.getAdjacencyEntries();
        List<Edge> edges = adjacencyListEntry.getEdgesAdjacencyNodes();

        adjacencyListEntry.getEntryNode().getNodeProperties().setColor(COLOR_FINISHED);

        for (int i = 0; i < entries.size(); i++) {
            visitNodeFromEdgeIfUnvisited(entries.get(i), edges.get(i), nodeEntry.value() + 1);
        }
    }

    @Override
    public boolean isDone() {
        return queue.isEmpty();
    }

    private void visitNodeFromEdgeIfUnvisited(AdjacencyListEntry adjacencyListEntry, Edge edgeVisitedFrom, double value) {
        if (visitNodeIfUnvisited(adjacencyListEntry, value)) {
            edgeVisitedFrom.getEdgeProperties().setColor(COLOR_FINISHED);
        }
    }

    private boolean visitNodeIfUnvisited(AdjacencyListEntry adjacencyListEntry, double value) {
        Node node = adjacencyListEntry.getEntryNode();
        if (!visitedNodeIdSet.add(adjacencyListEntry.getEntryNode().getId())) {
            return false;
        }
        node.getNodeProperties().setLabel(String.valueOf(Math.round(value))); //value should be an integer (label should be "1" instead of "1.0")
        node.getNodeProperties().setColor(COLOR_VISITED);

        queue.add(new NodeValuePair(adjacencyListEntry, value));
        return true;
    }
}
