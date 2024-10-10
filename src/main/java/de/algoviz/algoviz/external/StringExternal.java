package de.algoviz.algoviz.external;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Represents a string.
 * Contains the string as {@link String}.
 *
 * @author Benedikt
 * @version 1.0
 */
public record StringExternal(@JsonGetter("value") String value) {
}
