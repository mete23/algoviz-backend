package de.algoviz.algoviz.model.algorithm.implementation.util;

import de.algoviz.algoviz.model.graph_general.graph.AdjacencyListEntry;

/**
 * This record represents an adjacency list entry with a value. The record can be used to store a value as additional information of a node.
 *
 * @param adjacencyListEntry the adjacency list entry to represent
 * @param value              the value of the node
 * @author Tim
 * @version 1.0
 */
public record NodeValuePair(AdjacencyListEntry adjacencyListEntry, double value) {
}
