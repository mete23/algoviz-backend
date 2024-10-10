package de.algoviz.algoviz.model.graph_general.modifications.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

/**
 * this interface is used to modify a node
 *
 * @author Metehan
 * @version 1.0
 */
public interface INodeModification {
    /**
     * this method is used to apply the change to the node
     *
     * @param node the node to be modified
     */
    @JsonIgnore
    void applyChange(Node node);
}
