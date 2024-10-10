package de.algoviz.algoviz.model.graph_general.modifications.edge;

import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;

/**
 * this interface is used to define methods for applying changes to an edge
 *
 * @author Metehan
 * @version 1.0
 */
public interface IEdgeModification {
    /**
     * this method is used to apply changes to an edge
     *
     * @param edge the edge to apply changes to
     */
    void applyChange(Edge edge);
}
