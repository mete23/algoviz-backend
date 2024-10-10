package de.algoviz.algoviz.model.graph_general.graph;

import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all edges of a node
 *
 * @author Tim
 * @version 1.0
 */
public class AdjacencyListEntry {
    private final Node entryNode;
    private final List<AdjacencyListEntry> adjacencyEntries = new ArrayList<>();
    private final List<Edge> edgesAdjacencyNodes = new ArrayList<>();

    /**
     * Constructor for an AdjacencyListEntry
     *
     * @param entryNode the node of the entry as {@link Node}
     */
    public AdjacencyListEntry(Node entryNode) {
        this.entryNode = entryNode;
    }

    /**
     * Getter for the entryNode
     *
     * @return the entryNode as {@link Node}
     */
    public Node getEntryNode() {
        return entryNode;
    }

    /**
     * Getter for the adjacencyEntries
     *
     * @return the adjacencyEntries as {@link List} of {@link AdjacencyListEntry}
     */
    public List<AdjacencyListEntry> getAdjacencyEntries() {
        return adjacencyEntries;
    }

    /**
     * Getter for the edgesAdjacencyNodes
     *
     * @return the edgesAdjacencyNodes as {@link List} of {@link Edge}
     */
    public List<Edge> getEdgesAdjacencyNodes() {
        return edgesAdjacencyNodes;
    }

    /**
     * Adds an adjacencyEntry and the corresponding edge to the adjacencyListEntry
     *
     * @param adjacencyListEntry the adjacencyListEntry to add as {@link AdjacencyListEntry}
     * @param edge               the edge to add as {@link Edge}
     */
    public void addAdjacencyEntry(AdjacencyListEntry adjacencyListEntry, Edge edge) {
        adjacencyEntries.add(adjacencyListEntry);
        edgesAdjacencyNodes.add(edge);
    }
}
