package de.algoviz.algoviz.external;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Represents a generator to generate a graph.
 * Contains the id as {@link java.lang.String} and the name as {@link java.lang.String}.
 *
 * @author Benedikt, David
 * @version 1.0
 */
public record GeneratorExternal(@JsonGetter("id") String id, @JsonGetter("name") String name) {
}
