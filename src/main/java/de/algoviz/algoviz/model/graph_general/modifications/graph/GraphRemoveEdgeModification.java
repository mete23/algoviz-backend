package de.algoviz.algoviz.model.graph_general.modifications.graph;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;

/**
 * this record is used to remove a {@link Edge} from a {@link Graph}
 * contains the edge to be removed
 *
 * @author Metehan
 * @version 1.0
 */
public record GraphRemoveEdgeModification(Edge edge) implements IGraphModification {
    @Override
    public void applyChange(Graph graph) {
        graph.removeEdge(edge);
    }
}
