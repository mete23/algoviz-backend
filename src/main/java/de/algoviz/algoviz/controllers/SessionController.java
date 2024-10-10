package de.algoviz.algoviz.controllers;

import de.algoviz.algoviz.model.ApplicationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the session management.
 *
 * @author Tim, Tobias
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final ApplicationData applicationData;

    /**
     * Constructor for the SessionController.
     *
     * @param applicationData the application data.
     */
    @Autowired
    public SessionController(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }

    /**
     * Creates a new session.
     *
     * @return the id of the new session as {@link Integer}
     */
    @PostMapping(
            value = "/start",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public int startSession() {
        return applicationData.addSession();
    }

    /**
     * Deletes the session with the specified id.
     *
     * @param sessionId id of the session as {@link Integer}
     */
    @PostMapping(
            value = "/delete",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void endSession(@RequestParam int sessionId) {
        applicationData.removeSessionById(sessionId);
    }
}
