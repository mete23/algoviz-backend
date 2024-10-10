package de.algoviz.algoviz.external.modification.graph;

import de.algoviz.algoviz.external.graph.EdgeExternal;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;

/**
 * this record is used to add a {@link EdgeExternal} to the graph.
 * @param edge the edge to add
 * @param delete false
 */
public record GraphAddEdgeExternal(EdgeExternal edge, boolean delete) implements IGraphChanges {

    public GraphAddEdgeExternal(Edge edge) {
        this(new EdgeExternal(edge), false);
    }
}
