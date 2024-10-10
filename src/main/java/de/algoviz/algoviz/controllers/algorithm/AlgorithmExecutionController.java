package de.algoviz.algoviz.controllers.algorithm;

import de.algoviz.algoviz.external.graph.GraphExternal;
import de.algoviz.algoviz.external.modification.ModificationStepExternal;
import de.algoviz.algoviz.model.ApplicationData;
import de.algoviz.algoviz.model.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the execution of the algorithm.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/algorithms/execute")
public class AlgorithmExecutionController {

    private final ApplicationData applicationData;

    /**
     * Constructor for the AlgorithmExecutionController.
     *
     * @param applicationData the application data.
     */
    @Autowired
    public AlgorithmExecutionController(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }

    /**
     * Starts the execution of the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId id of the session as {@link Integer}
     */
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void startExecution(@RequestParam int sessionId) {
        UserSession session = applicationData.getSession(sessionId);
        session.getAlgorithmManager().startAlgorithm(session.getGraph());
    }

    /**
     * Stops the execution of the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId id of the session as {@link Integer}
     * @return true if the algorithm has been stopped, otherwise false.
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean isExecuted(@RequestParam int sessionId) {
        return applicationData.getSession(sessionId).getAlgorithmManager().algorithmHasBeenExecuted();
    }

    /**
     * gets the next step of the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId id of the session as {@link Integer}
     * @return the next step of the algorithm as {@link ModificationStepExternal}
     */
    @GetMapping(
            value = "/next",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ModificationStepExternal getNextStep(@RequestParam int sessionId) {
        return new ModificationStepExternal(applicationData.getSession(sessionId).getAlgorithmManager().getNextStepOfAlgorithm());
    }

    /**
     * gets the next step of the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId id of the session as {@link Integer}
     * @return true if the algorithm has a next step, otherwise false.
     */
    @GetMapping(
            value = "/next/available",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean hasNext(@RequestParam int sessionId) {
        return applicationData.getSession(sessionId).getAlgorithmManager().algorithmHasNext();
    }

    /**
     * resets the animation of the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession},
     * so that animation can be started again.
     *
     * @param sessionId id of the session as {@link Integer}
     * @return the origin, unmodified graph as {@link GraphExternal}
     */
    @GetMapping(
            value = "/reset",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GraphExternal resetAnimation(@RequestParam int sessionId) {
        return new GraphExternal(applicationData.getSession(sessionId).resetAnimation());
    }

    /**
     * gets the last step of the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId id of the session as {@link Integer}
     * @return the last step of the algorithm as {@link ModificationStepExternal}
     */
    @GetMapping(
            value = "/last",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ModificationStepExternal getLastStep(@RequestParam int sessionId) {
        return new ModificationStepExternal(applicationData.getSession(sessionId).getAlgorithmManager().getLastStep());
    }
}
