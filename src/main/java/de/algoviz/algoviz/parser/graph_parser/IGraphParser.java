package de.algoviz.algoviz.parser.graph_parser;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.parser.ParseException;

/**
 * This interface provides methods to parse a graph in string format.
 *
 * @author David
 * @version 1.0
 */
public interface IGraphParser {

    /**
     * This method parses a graph in string format to an instance of {@link Graph}.
     *
     * @param graphString the graph in string format
     * @return the graph in instance of {@link Graph}
     */
    Graph parse(String graphString) throws ParseException;
}
