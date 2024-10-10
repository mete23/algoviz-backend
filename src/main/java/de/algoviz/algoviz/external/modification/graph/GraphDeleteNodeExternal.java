package de.algoviz.algoviz.external.modification.graph;

import de.algoviz.algoviz.external.graph.EdgeExternal;
import de.algoviz.algoviz.external.graph.NodeExternal;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * this record is used to delete a {@link NodeExternal} from the graph.
 * @param node the node to delete
 * @param removedEdge the edges that are removed from the graph
 * @param delete true
 */
public record GraphDeleteNodeExternal(NodeExternal node, List<EdgeExternal> removedEdge, boolean delete) implements IGraphChanges {

    public GraphDeleteNodeExternal(Node node, List<Edge> removedEdges, boolean isStartNode) {
        this(new NodeExternal(node, isStartNode), new ArrayList<>(), true);
        for (Edge edge : removedEdges) {
            this.removedEdge.add(new EdgeExternal(edge));
        }
    }
}
