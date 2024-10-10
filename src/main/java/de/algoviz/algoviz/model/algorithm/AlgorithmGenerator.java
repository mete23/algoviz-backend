package de.algoviz.algoviz.model.algorithm;

import de.algoviz.algoviz.model.algorithm.implementation.*;

/**
 * this enum is used to generate instances of Algorithms
 *
 * @author Metehan
 * @version 1.0
 */
public enum AlgorithmGenerator {
    //the names of all classes from the
    BFS(BFSAlgorithm.NAME, BFSAlgorithm.DESCRIPTION) {
        @Override
        public AlgorithmInterface generate() {
            return new BFSAlgorithm();
        }
    },
    DFS(DFSAlgorithm.NAME, DFSAlgorithm.DESCRIPTION) {
        @Override
        public AlgorithmInterface generate() {
            return new DFSAlgorithm();
        }
    },
    DIJKSTRA(DijkstraAlgorithm.NAME, DijkstraAlgorithm.DESCRIPTION) {
        @Override
        public AlgorithmInterface generate() {
            return new DijkstraAlgorithm();
        }
    },
    KRUSKAL(KruskalAlgorithm.NAME, KruskalAlgorithm.DESCRIPTION) {
        @Override
        public AlgorithmInterface generate() {
            return new KruskalAlgorithm();
        }
    },
    LABEL_PROPAGATION(LabelPropagationAlgorithm.NAME, LabelPropagationAlgorithm.DESCRIPTION) {
        @Override
        public AlgorithmInterface generate() {
            return new LabelPropagationAlgorithm();
        }
    };

    /**
     * generates an instance of the algorithm
     *
     * @return the instance of the algorithm as an AlgorithmInterface
     */
    public abstract AlgorithmInterface generate();

    /**
     * the name of the algorithm
     */
    public final String NAME;
    /**
     * the description of the algorithm
     */
    public final String DESCRIPTION;


    AlgorithmGenerator(String name, String description) {
        this.NAME = name;
        this.DESCRIPTION = description;
    }
}
