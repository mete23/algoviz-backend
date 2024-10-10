package de.algoviz.algoviz.model.log_file;

import de.algoviz.algoviz.external.modification.ModificationStepExternal;
import de.algoviz.algoviz.model.algorithm.AlgorithmInterface;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;

import java.util.List;

/**
 * Represents the algorithm for the log file.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
public class LogFileToAlgorithm implements AlgorithmInterface {
    private final List<ModificationStep> modificationStepList;
    private int indexModificationStepList = 0;
    private Graph graph;

    /**
     * Constructor for the LogFileToAlgorithm.
     *
     * @param logFile the log file of the algorithm as {@link ModificationStepExternal}
     */
    public LogFileToAlgorithm(LogFile logFile) {
        this.modificationStepList = logFile.modificationStepList();
    }

    /**
     * set graph and execute first step
     *
     * @param graph the graph
     */
    @Override
    public void initialize(Graph graph) {
        this.graph = graph;
        if (!isDone()) {
            next();
        }
    }

    /**
     * set the next modification step
     */
    @Override
    public void next() {

        graph.applyChanges(modificationStepList.get(indexModificationStepList));
        indexModificationStepList++;
    }

    /**
     * check if the algorithm has finished.
     *
     * @return true if the algorithm has finished, false otherwise.
     */
    @Override
    public boolean isDone() {
        return indexModificationStepList >= modificationStepList.size();
    }

}
