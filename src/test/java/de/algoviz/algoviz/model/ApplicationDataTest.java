package de.algoviz.algoviz.model;

import de.algoviz.algoviz.model.session.UserSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationDataTest {

    @Test
    void addSession() {

        ApplicationData applicationData = new ApplicationData();
        Map<Integer, UserSession> sessions = new HashMap<>();

        // create sessions
        for (int i = 0; i < 10; i++) {
            int id = applicationData.addSession();
            sessions.put(id, applicationData.getSession(id));
            assertSame(i + 1, sessions.size());
        }

        // access sessions
        for (Map.Entry<Integer, UserSession> entry : sessions.entrySet()) {
            assertEquals(entry.getValue(), applicationData.getSession(entry.getKey()));
        }

        // remove session
        for (int id : sessions.keySet()) {
            applicationData.removeSessionById(id);
        }

        // try to access sessions
        for (int id : sessions.keySet()) {
            assertThrows(ResponseStatusException.class, () -> applicationData.getSession(id));
        }
    }
}
