package de.algoviz.algoviz.controllers.algorithm;

import de.algoviz.algoviz.external.GeneratorExternal;
import de.algoviz.algoviz.external.NumberExternal;
import de.algoviz.algoviz.generators.export_generator.ExportFrameInterface;
import de.algoviz.algoviz.generators.export_generator.Generators;
import de.algoviz.algoviz.model.ApplicationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for the export of the algorithm.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/algorithms/export")
public class AlgorithmExportController {
    private final ApplicationData applicationData;

    /**
     * Constructor for the AlgorithmExportController.
     *
     * @param applicationData the application data.
     */
    @Autowired
    public AlgorithmExportController(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }

    /**
     * Returns a list of all available export generators.
     *
     * @return a list of all available export generators as string.
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<GeneratorExternal> getExportGenerators() {
        List<Generators> generators = Arrays.stream(Generators.values()).toList();
        List<GeneratorExternal> output = new ArrayList<>();
        for (Generators generator : generators) {
            output.add(new GeneratorExternal(generator.toString(), generator.getName()));
        }
        return output;
    }

    /**
     * Exports the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession} as a list of frames.
     *
     * @param sessionId       id of the session as {@link Integer}
     * @param exportGenerator the export generator as {@link Generators}
     * @return a list of frames as {@link String}
     */
    @GetMapping(
            value = "/{exportGenerator}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<String> exportAnimation(@RequestParam int sessionId, @PathVariable Generators exportGenerator) {
        return exportAlgorithm(sessionId, exportGenerator.getInstance());
    }

    /**
     * Returns the number of frames of the algorithm of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId id of the session as {@link Integer}
     * @return the number of frames as {@link Integer}
     */
    @GetMapping(
            value = "/size",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public NumberExternal getNumberOfFrames(@RequestParam int sessionId) {
        return new NumberExternal(applicationData.getSession(sessionId).getAlgorithmManager().getNumberOfSteps());
    }

    private List<String> exportAlgorithm(int sessionId, ExportFrameInterface export) {
        return applicationData.getSession(sessionId).getAlgorithmManager().exportAlgorithm(export).getFrameList().stream().map(ExportFrameInterface::toString).toList();
    }
}
