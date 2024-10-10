package de.algoviz.algoviz.controllers.graph;

import de.algoviz.algoviz.external.graph.GraphExternal;
import de.algoviz.algoviz.external.kagen.ErrorBodyExternal;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.parser.graph_parser.DotFormatParser;
import io.netty.handler.codec.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controller for the graph generation.
 *
 * @author Benedikt, David, Tim
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/api/graph/generators")
public class GraphGeneratorController {
    private static final String KAGEN_BACKEND_API_URL = "/api/generate";
    private static final String GET_GENERATOR_OPTION_URL = "/option";
    private static final String GET_PARAMETER_INFORMATION_URL = "/param";

    private final WebClient webClient;
    private final DotFormatParser dotFormatParser;

    /**
     * Constructor for the GraphGeneratorController.
     *
     * @param webClient       the web client of the generator.
     * @param dotFormatParser the dot format parser.
     */
    @Autowired
    public GraphGeneratorController(WebClient webClient, DotFormatParser dotFormatParser) {
        this.dotFormatParser = dotFormatParser;
        this.webClient = webClient;
    }

    /**
     * Returns all generators types.
     *
     * @return all generators type as {@link String}
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getGeneratorTypes() {
        return webClient.get()
                .uri(KAGEN_BACKEND_API_URL)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Returns the generator card of the specified generator.
     *
     * @param generator the generator as {@link String}
     * @return the generator card as {@link String}
     */
    @GetMapping(
            value = "/{generator}/option",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getGeneratorCard(@PathVariable String generator) {
        return webClient.get()
                .uri(KAGEN_BACKEND_API_URL + "/" + generator + GET_GENERATOR_OPTION_URL)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Returns the parameter information of the specified generator.
     *
     * @param generator the generator as {@link String}
     * @return the parameter information as {@link String}
     */
    @GetMapping(
            value = "/{generator}/param",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getParameterInformation(@PathVariable String generator) {
        return webClient.get()
                .uri(KAGEN_BACKEND_API_URL + "/" + generator + GET_PARAMETER_INFORMATION_URL)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Generates a graph with the specified generator.
     * Sets the generated graph to the specified session.
     *
     * @param generator  the generator as {@link String}
     * @param parameters the parameters as {@link List} of {@link Object}
     */
    @PostMapping(
            value = "/{generator}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GraphExternal getGeneratedGraph(@PathVariable String generator, @RequestBody List<Object> parameters) {
        String responseBody = webClient.post()
                .uri(KAGEN_BACKEND_API_URL + "/" + generator)
                .body(BodyInserters.fromValue(parameters))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ErrorBodyExternal.class)
                        .flatMap(error -> Mono.error(new ResponseStatusException(response.statusCode(), error.getMessage()))))
                .bodyToMono(String.class)
                .block();

        if (responseBody == null) {
            return null;
        }
        Graph graph;
        try {
            graph = dotFormatParser.parse(responseBody);
        } catch (ParseException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return new GraphExternal(graph);
    }
}
