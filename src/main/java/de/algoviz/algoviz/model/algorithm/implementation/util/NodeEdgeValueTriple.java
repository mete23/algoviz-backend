package de.algoviz.algoviz.model.algorithm.implementation.util;

import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.util.Optional;

/**
 * This record contains a triple of a node, an edge and a value. The edge is optional, because not every triple must have a valid edge.
 *
 * @param node  The node
 * @param edge  The edge
 * @param value The value
 * @author Tim
 * @version 1.0
 */
public record NodeEdgeValueTriple(Node node, Optional<Edge> edge, double value) {
}
