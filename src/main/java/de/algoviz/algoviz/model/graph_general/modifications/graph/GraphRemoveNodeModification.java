package de.algoviz.algoviz.model.graph_general.modifications.graph;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.util.List;

/**
 * this record is used to remove a {@link Node} from a {@link Graph}
 * contains the node to be removed
 *
 * @author Metehan
 * @version 1.0
 */
public record GraphRemoveNodeModification(Node node, List<Edge> removedEdges) implements IGraphModification {
    @Override
    public void applyChange(Graph graph) {
        graph.removeNode(node);
    }
}
