package de.algoviz.algoviz.controllers.algorithm;

import de.algoviz.algoviz.external.StringExternal;
import de.algoviz.algoviz.model.ApplicationData;
import de.algoviz.algoviz.model.algorithm.AlgorithmGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Controller for the selection of the algorithm.
 *
 * @author Benedikt, Metehan, Tim
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/algorithms")
public class AlgorithmSelectionController {
    private final ApplicationData applicationData;

    /**
     * Constructor for the AlgorithmSelectionController.
     *
     * @param applicationData the application data.
     */
    @Autowired
    public AlgorithmSelectionController(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }

    /**
     * Returns a list of all available algorithms.
     *
     * @return a list of all available algorithms as string.
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody List<AlgorithmGenerator> getAlgorithms() {
        return Arrays.stream(AlgorithmGenerator.values()).toList();
    }

    /**
     * Returns the name of the specified algorithm.
     *
     * @param algorithm the algorithm as {@link AlgorithmGenerator}
     * @return the name of the specified algorithm as {@link StringExternal}
     */
    @GetMapping(
            value = "/{algorithm}/name",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody StringExternal getName(@PathVariable AlgorithmGenerator algorithm) {
        return new StringExternal(algorithm.NAME);
    }

    /**
     * Returns the description of the specified algorithm.
     *
     * @param algorithm the algorithm as {@link AlgorithmGenerator}
     * @return the description of the specified algorithm as {@link StringExternal}
     */
    @GetMapping(
            value = "/{algorithm}/description",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody StringExternal getDescription(@PathVariable AlgorithmGenerator algorithm) {
        return new StringExternal(algorithm.DESCRIPTION);
    }

    /**
     * Sets the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId id of the session as {@link Integer}
     * @param algorithm the algorithm as {@link AlgorithmGenerator}
     */
    @PostMapping(
            value = "/{algorithm}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void setAlgorithm(@RequestParam int sessionId, @PathVariable AlgorithmGenerator algorithm) {
        applicationData.getSession(sessionId).getAlgorithmManager().setAlgorithm(algorithm.generate());
        applicationData.getSession(sessionId).getAlgorithmManager().setGenerator(algorithm);
    }
}
