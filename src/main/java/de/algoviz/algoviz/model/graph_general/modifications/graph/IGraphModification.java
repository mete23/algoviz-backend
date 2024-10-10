package de.algoviz.algoviz.model.graph_general.modifications.graph;

import de.algoviz.algoviz.model.graph_general.graph.Graph;

/**
 * this interface is for classes which represent a modification of a graph
 *
 * @author Metehan
 * @version 1.0
 */
public interface IGraphModification {
    /**
     * applies the change to the graph
     *
     * @param graph the graph to be modified
     */
    void applyChange(Graph graph);
}
