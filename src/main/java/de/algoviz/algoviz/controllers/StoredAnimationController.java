package de.algoviz.algoviz.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.algoviz.algoviz.database.datamodel.StoredAnimation;
import de.algoviz.algoviz.database.repository.StoredAnimationRepository;
import de.algoviz.algoviz.external.graph.GraphExternal;
import de.algoviz.algoviz.model.ApplicationData;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.util.data_file_manager.DataFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller for the stored animations.
 *
 * @author David
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/stored-animation")
public class StoredAnimationController {

    private static final String STORED_ANIMATION_DIR = "stored_animations/";
    private static final String NO_STORED_ANIMATION_MESSAGE = "No stored animation with id %d found.";
    private static final String GRAPH_NOT_FOUND_MESSAGE = "The graph is not available.";
    private static final String GRAPH_FILE_SUFFIX = ".json";

    private final StoredAnimationRepository storedAnimationRepository;
    private final ApplicationData applicationData;
    private final ObjectMapper objectMapper;

    /**
     * Constructor for the StoredAnimationController.
     *
     * @param storedAnimationRepository the repository for the stored animations
     * @param applicationData           the application data
     */
    @Autowired
    public StoredAnimationController(StoredAnimationRepository storedAnimationRepository,
                                     ApplicationData applicationData,
                                     ObjectMapper objectMapper) {
        this.storedAnimationRepository = storedAnimationRepository;
        this.applicationData = applicationData;
        this.objectMapper = objectMapper;
    }

    /**
     * Stores the animation of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession} in the database.
     *
     * @param sessionId the id of the session
     * @return the id of the stored animation
     */
    @PostMapping("/store")
    public long storeAnimation(@RequestParam int sessionId) {
        long fileId = System.identityHashCode(applicationData.getSession(sessionId).getGraph());
        GraphExternal storedGraph = new GraphExternal(applicationData.getSession(sessionId).getGraph());
        StoredAnimation storedAnimation = new StoredAnimation(fileId + GRAPH_FILE_SUFFIX,
                applicationData.getSession(sessionId).getAlgorithmManager().getGenerator());

        storedAnimation = storedAnimationRepository.save(storedAnimation);
        try {
            DataFileManager.insertData(STORED_ANIMATION_DIR, storedAnimation.getPathGraphFile(), objectMapper.writeValueAsString(storedGraph));
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, GRAPH_NOT_FOUND_MESSAGE);
        }

        return storedAnimation.getId();
    }

    /**
     * Sets the animation of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession} to the specified stored animation.
     *
     * @param sessionId         the id of the session
     * @param storedAnimationId the id of the stored animation
     */
    @PostMapping("/{storedAnimationId}")
    public void setAnimation(@PathVariable long storedAnimationId, @RequestParam int sessionId) {
        StoredAnimation storedAnimation = storedAnimationRepository.findById(storedAnimationId).orElse(null);
        if (Objects.isNull(storedAnimation))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NO_STORED_ANIMATION_MESSAGE, storedAnimationId));

        Graph graph;
        try {
            GraphExternal graphExternal = objectMapper
                    .readValue(DataFileManager.getData(STORED_ANIMATION_DIR, storedAnimation.getPathGraphFile()), GraphExternal.class);
            graph = graphExternal.createGraph();
        } catch (IOException | ParseException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, GRAPH_NOT_FOUND_MESSAGE);
        }
        applicationData.getSession(sessionId).setGraph(graph);
        applicationData.getSession(sessionId).getAlgorithmManager().setAlgorithm(storedAnimation.getAlgorithmGenerator().generate());
        applicationData.getSession(sessionId).getAlgorithmManager().setGenerator(storedAnimation.getAlgorithmGenerator());
    }
}
