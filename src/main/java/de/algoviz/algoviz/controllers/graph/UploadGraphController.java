package de.algoviz.algoviz.controllers.graph;

import de.algoviz.algoviz.model.ApplicationData;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.parser.graph_parser.DotFormatParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for the graph upload.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/graph/upload")
public class UploadGraphController {
    private final ApplicationData applicationData;

    /**
     * Constructor for the UploadGraphController.
     *
     * @param applicationData the application data.
     */
    @Autowired
    public UploadGraphController(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }

    /**
     * Sets the graph in the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId   id of the session as {@link Integer}
     * @param fileContent the graph as string
     */
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = "text/plain")
    public void uploadGraph(@RequestParam int sessionId, @RequestBody String fileContent) {
        try {
            applicationData.getSession(sessionId).setGraph(new DotFormatParser().parse(fileContent));
        } catch (ParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }
}
