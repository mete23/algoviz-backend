package de.algoviz.algoviz.external.modification.graph;

import de.algoviz.algoviz.external.graph.EdgeExternal;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;

/**
 * this record is used to delete a {@link EdgeExternal} from the graph.
 * @param edge the edge to delete
 * @param delete true
 */
public record GraphDeleteEdgeExternal(EdgeExternal edge, boolean delete) implements IGraphChanges {

    public GraphDeleteEdgeExternal(Edge edge) {
        this(new EdgeExternal(edge), true);
    }
}
