package de.algoviz.algoviz.model.algorithm;

import de.algoviz.algoviz.generators.export_generator.ExportFile;
import de.algoviz.algoviz.generators.export_generator.ExportFrameInterface;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Benedikt, Tim
 * @version 1.0
 */
public class AlgorithmManager {

    private static final String GRAPH_OR_ALGORITHM_NOT_SET_MESSAGE = "The graph and the algorithm must be set before the algorithm can be executed.";
    private static final String STEP_NOT_AVAILABLE_MESSAGE = "no step available";
    private static final String ENGINE_NOT_AVAILABLE_MESSAGE = "The algorithm has not been executed.";

    private AlgorithmInterface algorithm;
    private AlgorithmEngine engine;
    private AlgorithmGenerator generator;

    public void setAlgorithm(AlgorithmInterface algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * The algorithm of the section will be executed on a new Thread.
     * The method should not be used if the graph or the algorithm is not set.
     *
     * @param graph the graph on which the algorithm should be executed.
     */
    public void startAlgorithm(Graph graph) {
        if (!algorithmIsSet() || graph == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GRAPH_OR_ALGORITHM_NOT_SET_MESSAGE);
        }
        engine = new AlgorithmEngine(graph, algorithm);
        new Thread(engine).start();
    }

    /**
     * This method gets the next step of the algorithm and should only be used if algorithmHasNext() returns true.
     *
     * @return the next step of the algorithm
     */
    public ModificationStep getNextStepOfAlgorithm() {
        if (!algorithmHasNext()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, STEP_NOT_AVAILABLE_MESSAGE);
        }
        return engine.getNext();
    }

    /**
     * This method gets a TikZ2D file of the steps of the algorithm.
     *
     * @return the steps of the algorithm.
     */
    public ExportFile exportAlgorithm(ExportFrameInterface export) {
        if (!algorithmHasBeenExecuted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ENGINE_NOT_AVAILABLE_MESSAGE);
        }
        return engine.exportAlgorithm(export);
    }

    /**
     * checks if the algorithm has more steps.
     *
     * @return true if the algorithm has more steps, false otherwise.
     */
    public boolean algorithmHasNext() {
        return engineIsPresent() && engine.hasNext();
    }

    /**
     * checks if the algorithm has been executed.
     *
     * @return true if the algorithm has been executed, false otherwise.
     */
    public boolean algorithmHasBeenExecuted() {
        return engineIsPresent() && engine.algorithmHasBeenExecuted();
    }

    /**
     * This method gets the number of steps of the algorithm.
     *
     * @return the number of steps of the algorithm.
     */
    public int getNumberOfSteps() {
        return engine.getNumberOfSteps();
    }

    /**
     * Resets the animation of the algorithm.
     */
    public void resetAnimation() {
        engine.resetAnimation();
    }

    /**
     * This method returns the last step of the algorithm and decreases the count of the current step.
     *
     * @return the last step of the algorithm.
     */
    public ModificationStep getLastStep() {
        return engine.getLastModificationStep();
    }

    /**
     * This method returns the name of the algorithm.
     *
     * @return the generator of the algorithm.
     */
    public AlgorithmGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(AlgorithmGenerator generator) {
        this.generator = generator;
    }

    private boolean engineIsPresent() {
        return engine != null;
    }

    private boolean algorithmIsSet() {
        return algorithm != null;
    }
}