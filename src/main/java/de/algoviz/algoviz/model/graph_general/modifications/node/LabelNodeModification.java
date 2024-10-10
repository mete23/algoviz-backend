package de.algoviz.algoviz.model.graph_general.modifications.node;

import de.algoviz.algoviz.model.graph_general.graph.node.Node;

/**
 * this record is used to modify the label of {@link Node}.
 *
 * @param newLabel the new label of the node
 * @param oldLabel the old label of the node
 * @author Metehan
 * @version 1.0
 */
public record LabelNodeModification(String newLabel, String oldLabel) implements INodeModification {

    @Override
    public void applyChange(Node node) {
        node.getNodeProperties().setLabel(this.newLabel);
    }
}
