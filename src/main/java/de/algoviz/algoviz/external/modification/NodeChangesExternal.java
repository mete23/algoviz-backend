package de.algoviz.algoviz.external.modification;

import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;

import java.util.List;

/**
 * Represents a list of node modifications.
 * Contains the id of the node as {@link java.lang.Integer}
 * and a list of node modifications as {@link de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification}.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
public record NodeChangesExternal(int nodeId, List<INodeModification> changes) {
}

