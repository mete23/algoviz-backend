package de.algoviz.algoviz.model.session;

import de.algoviz.algoviz.model.log_file.LogFile;
import de.algoviz.algoviz.model.log_file.LogFileToAlgorithm;
import de.algoviz.algoviz.model.algorithm.AlgorithmManager;
import de.algoviz.algoviz.model.graph_general.graph.Graph;

/**
 * this class represents a user-session
 *
 * @author Metehan
 * @version 1.0
 */
public class UserSession {
    private final AlgorithmManager algorithmManager;
    private Graph graph;


    /**
     * constructor
     */
    public UserSession() {
        algorithmManager = new AlgorithmManager();
    }

    /**
     * setter for the graph of a user
     *
     * @param graph the graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * getter for the graph of a userSession.
     *
     * @return the graph of this userSession
     */
    public Graph getGraph() {
        return this.graph;
    }

    /**
     * getter for the algorithmManager of a userSession.
     *
     * @return the algorithmManager
     */
    public AlgorithmManager getAlgorithmManager() {
        return algorithmManager;
    }

    /**
     * resets the animation of the algorithmManager.
     *
     * @return the original graph
     */
    public Graph resetAnimation() {
        algorithmManager.resetAnimation();
        return graph;
    }

    public void setLogFile(LogFile logFile) {
        setGraph(logFile.graph());
        this.algorithmManager.setAlgorithm(new LogFileToAlgorithm(logFile));
    }
}
