package de.algoviz.algoviz.model;

import de.algoviz.algoviz.model.session.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class holds the application data.
 *
 * @author Metehan
 * @version 1.0
 */
@Component("ApplicationData")
public class ApplicationData {

    private static final String ID_NOT_FOUND = "The user session with the id %s was not found.";

    private final Map<Integer, UserSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * this method is used to add a session
     *
     * @return the id of the new session
     */
    public int addSession() {
        var newSession = new UserSession();
        int hashCode = System.identityHashCode(newSession.hashCode());
        this.sessionMap.put(hashCode, newSession);
        return hashCode;
    }

    /**
     * this method is used to get a session by its id
     *
     * @param id the id of the session
     * @return the session
     */
    public UserSession getSession(int id) {

        UserSession session = this.sessionMap.get(id);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(ID_NOT_FOUND, id));
        }
        return session;
    }

    /**
     * this method removes a {@link UserSession user-session} by its id
     *
     * @param id the id of the user-session
     */
    public void removeSessionById(int id) {
        this.sessionMap.remove(id);
    }

}