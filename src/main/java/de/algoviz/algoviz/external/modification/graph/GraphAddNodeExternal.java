package de.algoviz.algoviz.external.modification.graph;

import de.algoviz.algoviz.external.graph.NodeExternal;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

/**
 * this record is used to add a {@link NodeExternal} to the graph.
 * @param node the node to add
 * @param delete false
 */
public record GraphAddNodeExternal(NodeExternal node, boolean delete) implements IGraphChanges {
    public GraphAddNodeExternal(Node node, boolean isStartNode) {
        this(new NodeExternal(node, isStartNode), false);
    }
}
