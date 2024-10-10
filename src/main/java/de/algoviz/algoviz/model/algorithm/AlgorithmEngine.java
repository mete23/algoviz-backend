package de.algoviz.algoviz.model.algorithm;

import de.algoviz.algoviz.generators.export_generator.ExportFile;
import de.algoviz.algoviz.generators.export_generator.ExportFrameInterface;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modification.observers.GraphObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for executing the algorithm and saving the changes.
 * The changes are saved in a list of ModificationSteps.
 * The class implements the Runnable interface so that the algorithm can be executed on a new Thread.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
public class AlgorithmEngine implements Runnable {

    private final List<ModificationStep> modificationStepList = new ArrayList<>();

    private int indexModifications = 0;
    private final Graph graphStart;
    private final AlgorithmInterface algorithm;

    private boolean algorithmHasBeenExecuted = false;

    /**
     * Constructor for the AlgorithmEngine.
     *
     * @param graph     the graph on which the algorithm should be executed.
     * @param algorithm the algorithm which should be executed.
     */
    public AlgorithmEngine(Graph graph, AlgorithmInterface algorithm) {
        this.graphStart = graph;
        this.algorithm = algorithm;
    }

    /**
     * Checks if the algorithm has been executed.
     *
     * @return true if the algorithm has been completely executed, false otherwise.
     */
    public boolean algorithmHasBeenExecuted() {
        return algorithmHasBeenExecuted;
    }

    /**
     * Checks if the next step of the algorithm is available.
     *
     * @return true if the next step is available, false otherwise.
     */
    public boolean hasNext() {
        return modificationStepList.size() > indexModifications;
    }

    /**
     * Get the next step of the algorithm.
     *
     * @return the changes of the graph in the next step of the algorithm as ModificationStep.
     */
    public ModificationStep getNext() {
        indexModifications++;
        return modificationStepList.get(indexModifications - 1);
    }

    /**
     * Generates the export data for the next step of the algorithm.
     *
     * @param exportFrame the export frame which should be used.
     * @return the export data as ExportFile.
     */
    public ExportFile exportAlgorithm(ExportFrameInterface exportFrame) {

        ExportFile file = new ExportFile();
        Graph graphRuntime = graphStart.clone();

        ExportFrameInterface export = exportFrame.getInstance();
        export.generateExportData(graphRuntime);
        file.addFrame(export);

        for (ModificationStep step : modificationStepList) {
            graphRuntime.applyChanges(step);
            export = exportFrame.getInstance();
            export.generateExportData(graphRuntime);
            file.addFrame(export);
        }
        return file;
    }

    /**
     * Get the number of steps of the algorithm.
     *
     * @return the number of steps of the algorithm.
     */
    public int getNumberOfSteps() {
        // +1 because the initial graph is also a step
        return modificationStepList.size() + 1;
    }

    /**
     * Resets the animation. This means that the index of the current step is set to 0.
     */
    public void resetAnimation() {
        indexModifications = 0;
    }

    /**
     * Get the last step of the algorithm and decrease the index of the current step.
     *
     * @return the changes of the graph in the last step of the algorithm as ModificationStep.
     */
    public ModificationStep getLastModificationStep() {
        if (indexModifications > 0) {
            indexModifications --;
        }
        return modificationStepList.get(indexModifications);
    }

    /**
     * Executes the algorithm on a new Thread.
     */
    @Override
    public void run() {
        executeAlgorithm();
    }

    private void executeAlgorithm() {

        if (algorithmHasBeenExecuted) {
            return;
        }
        Graph graphRuntime = graphStart.clone();
        ModificationStep modificationStep = new ModificationStep();
        GraphObserver observer = new GraphObserver(modificationStep);
        graphRuntime.addObserver(observer);
        algorithm.initialize(graphRuntime);
        this.modificationStepList.add(modificationStep);
        modificationStep = new ModificationStep();
        observer.setModificationList(modificationStep);

        while (!algorithm.isDone()) {
            algorithm.next();
            this.modificationStepList.add(modificationStep);
            modificationStep = new ModificationStep();
            observer.setModificationList(modificationStep);
        }

        graphRuntime.removeObserver(observer);
        algorithmHasBeenExecuted = true;
    }
}
