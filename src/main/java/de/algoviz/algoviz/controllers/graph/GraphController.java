package de.algoviz.algoviz.controllers.graph;

import de.algoviz.algoviz.external.graph.GraphExternal;
import de.algoviz.algoviz.model.ApplicationData;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

/**
 * Controller for the graph.
 *
 * @author Benedikt, Tom, Tobias
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final ApplicationData applicationData;

    /**
     * Constructor for the GraphController.
     *
     * @param applicationData the application data.
     */
    @Autowired
    public GraphController(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }

    /**
     * Sets the graph of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId     id of the session as {@link Integer}
     * @param graphExternal the graph as {@link GraphExternal}
     */
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void setGraph(@RequestParam int sessionId, @RequestBody GraphExternal graphExternal) {
        Graph graph;
        try {
            graph = graphExternal.createGraph();
        } catch (ParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        applicationData.getSession(sessionId).setGraph(graph);
    }

    /**
     * Returns the graph of the specified {@link de.algoviz.algoviz.model.session.UserSession UserSession}.
     *
     * @param sessionId id of the session as {@link Integer}
     * @return the graph as {@link GraphExternal}
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GraphExternal getGraph(@RequestParam int sessionId) {
        Graph graph = applicationData.getSession(sessionId).getGraph();

        // user creates a new graph in the frontend
        if (graph == null) {
            graph = new Graph(new ArrayList<>(), new ArrayList<>(), false, false);
        }

        return new GraphExternal(graph);
    }
}
