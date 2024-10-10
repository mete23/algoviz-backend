package de.algoviz.algoviz.model.log_file;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;

import java.util.List;

/**
 * Represents a log file.
 * Contains a graph as {@link Graph} and a list of modification steps as {@link ModificationStep}.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
public record LogFile(Graph graph, List<ModificationStep> modificationStepList) {
}

