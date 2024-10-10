package de.algoviz.algoviz.model.algorithm;

import de.algoviz.algoviz.model.graph_general.graph.Graph;

/**
 * Interface of an algorithm.
 *
 * @author metehan
 * @version 1.0
 */
public interface AlgorithmInterface {

    /**
     * Initializes the algorithm.
     *
     * @param graph the graph on which the algorithm should be executed.
     */
    void initialize(Graph graph);

    /**
     * Executes the next step of the algorithm.
     */
    void next();

    /**
     * Checks if the algorithm is done.
     *
     * @return true if the algorithm is done, false otherwise.
     */
    boolean isDone();

}