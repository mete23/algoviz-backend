package de.algoviz.algoviz.external;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Represents a number.
 * Contains the number as {@link java.lang.Integer}.
 *
 * @author Benedikt
 * @version 1.0
 */
public record NumberExternal(@JsonGetter("number") int number) {
}
