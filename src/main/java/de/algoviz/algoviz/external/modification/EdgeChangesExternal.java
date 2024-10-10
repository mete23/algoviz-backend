package de.algoviz.algoviz.external.modification;

import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;

import java.util.List;

/**
 * Represents a list of edge modifications.
 * Contains the id of the edge as {@link java.lang.Integer}
 * and a list of edge modifications as {@link de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification}.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
public record EdgeChangesExternal(int edgeId, List<IEdgeModification> changes) {
}

