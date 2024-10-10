package de.algoviz.algoviz.controllers;

import de.algoviz.algoviz.model.ApplicationData;
import de.algoviz.algoviz.model.log_file.LogFile;
import de.algoviz.algoviz.model.session.UserSession;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.parser.logfile_parser.ILogFileParser;
import de.algoviz.algoviz.parser.logfile_parser.LogFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for the log file upload.
 *
 * @author Tim
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/logfile")
public class LogFileController {
    private final ApplicationData applicationData;

    /**
     * Constructor for the LogFileController.
     *
     * @param applicationData the application data.
     */
    @Autowired
    public LogFileController(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }

    /**
     * Sets the log file of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId   id of the session as {@link Integer}
     * @param fileContent the log file as {@link String}
     */
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = "text/plain")
    public void uploadLogFile(@RequestParam int sessionId, @RequestBody String fileContent) {
        UserSession session = applicationData.getSession(sessionId);
        LogFile logFile;
        ILogFileParser parser = new LogFileParser();
        try {
            logFile = parser.parse(fileContent);
        } catch (ParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        session.setLogFile(logFile);
    }
}
