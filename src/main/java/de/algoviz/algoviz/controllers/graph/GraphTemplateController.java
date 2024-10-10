package de.algoviz.algoviz.controllers.graph;

import de.algoviz.algoviz.database.datamodel.StoredGraph;
import de.algoviz.algoviz.database.repository.StoredGraphRepository;
import de.algoviz.algoviz.external.graph.GraphExternal;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.parser.graph_parser.DotFormatParser;
import de.algoviz.algoviz.util.data_file_manager.DataFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Serves different graph examples, that are stored n the database
 *
 * @author David
 * @version 1.0
 */

@CrossOrigin
@RestController
@RequestMapping("/api/graph/templates")
public class GraphTemplateController {
    public static final String STORED_GRAPH_PATH = "stored_graphs/";
    private static final String ERROR_FILE_NOT_FOUND = "Error: file not found";
    private final StoredGraphRepository storedGraphRepository;
    private final DotFormatParser dotFormatParser;

    /**
     * Constructor for the GraphTemplateController.
     *
     * @param storedGraphRepository the repository for the stored graphs
     */
    @Autowired
    public GraphTemplateController(StoredGraphRepository storedGraphRepository,
                                   DotFormatParser dotFormatParser) {
        this.storedGraphRepository = storedGraphRepository;
        this.dotFormatParser = dotFormatParser;
    }

    /**
     * returns a list of all available graph ids
     *
     * @return list of all available graph ids
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Long> getTemplatedGraphIds() {
        return storedGraphRepository.findAllIds();
    }

    /**
     * returns the stored graph date of the database
     *
     * @return list of all stored graphs in the database
     */
    @GetMapping(
            value = "/{graphId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public StoredGraph getStoredGraph(@PathVariable long graphId) {
        Optional<StoredGraph> storedGraph = storedGraphRepository.findById(graphId);
        return storedGraph.orElse(null);
    }

    /**
     * Returns the graph template, that is stored in the database.
     *
     * @param graphId id of the stored graph
     * @return graph template
     */
    @GetMapping(
            value = "/{graphId}/template",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GraphExternal getTemplateGraph(@PathVariable long graphId) {
        Optional<StoredGraph> storedGraph = storedGraphRepository.findById(graphId);
        if (storedGraph.isEmpty()) {
            return null;
        }
        String graphString;
        try {
            graphString = DataFileManager.getData(STORED_GRAPH_PATH, storedGraph.get().getPathGraphFile());
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_FILE_NOT_FOUND);
        }
        Graph templateGraph;
        try {
            templateGraph = dotFormatParser.parse(graphString);
        } catch (ParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return new GraphExternal(templateGraph);
    }
}
