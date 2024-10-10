package de.algoviz.algoviz.parser.graph_parser;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.parser.ParseException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * parses a graph in dot format file and creates a graph as {@link Graph}
 *
 * @author Tim
 * @version 1.0
 */
@Component
public class DotFormatParser implements IGraphParser {
    private static final String LINE_SEPARATOR = "\\R";
    private static final String NODE = "(\\d+)";
    private static final String COORDINATES_2D = "\\[pos=\"(.+),(.+)!\"]";
    private static final String COORDINATES_3D = "\\[pos=\"(.+),(.+),(.+)!\"]";
    private static final String EDGE_DIRECTED = "(\\d+)->(\\d+)";
    private static final String EDGE_UNDIRECTED = "(\\d+)--(\\d+)";
    private static final String WEIGHT = "\\[weight=(.+)]";

    private static final String START_FILE = "graphG{";
    private static final String END_FILE = "}";

    private static final String PREFIX_ERROR_PARSING_GRAPH = "Error parsing graph: ";
    private static final String ERROR_MESSAGE_ILLEGAL_START_OR_END = PREFIX_ERROR_PARSING_GRAPH + "The file must start with \"" + START_FILE + "\" and end with \"" + END_FILE + "\".";
    private static final String ERROR_MESSAGE_ILLEGAL_NODE = PREFIX_ERROR_PARSING_GRAPH + "Line %d does not contain a valid node";
    private static final String ERROR_MESSAGE_ILLEGAL_EDGE = PREFIX_ERROR_PARSING_GRAPH + "Line %d does not contain a valid edge";
    private static final String ERROR_UNKNOWN_REASON = PREFIX_ERROR_PARSING_GRAPH + "The file does not contain a valid graph. Error in line %d.";

    private static final String ERROR_MESSAGE_FIRST_NODE_OF_EDGE_DOES_NOT_EXIST = "The first node of the edge in line %d does not exist";
    private static final String ERROR_MESSAGE_SECOND_NODE_OF_EDGE_DOES_NOT_EXIST = "The second node of the edge in line %d does not exist";
    private static final String ERROR_MESSAGE_EDGE_WITH_SAME_NODES = "The first node and the second node of the edge in line %d should not be the same.";


    /**
     * creates a graph from a dot format file
     *
     * @param graphString the dof format file
     * @return a graph
     * @throws ParseException throws a parse exception with an error message if the file is not valid.
     */
    @Override
    public Graph parse(String graphString) throws ParseException {

        String[] graphData = graphString.replace(" ", "").split(LINE_SEPARATOR);

        testStartAndEndOfFile(graphData);

        boolean is2d = !graphData[1].matches(NODE + COORDINATES_3D);
        Pattern nodePattern = Pattern.compile(NODE + (is2d ? COORDINATES_2D : COORDINATES_3D));
        int numberOfCoordinates = is2d ? 2 : 3;

        boolean isWeighted = graphData[graphData.length - 2].matches(".+" + WEIGHT);
        boolean isDirected = graphData[graphData.length - 2].matches(EDGE_DIRECTED + ".*");

        Pattern edgePattern = Pattern.compile((isDirected ? EDGE_DIRECTED : EDGE_UNDIRECTED) + (isWeighted ? WEIGHT : ""));

        Map<Integer, Node> nodes = new HashMap<>();
        List<Edge> edges = new ArrayList<>();

        int i;
        for (i = 1; i < graphData.length - 1; i++) {
            Node node = getNode(i + 1, nodePattern, numberOfCoordinates, graphData[i]);
            if (node == null) break;
            nodes.put(node.getId(), node);
        }
        for (; i < graphData.length - 1; i++) {
            Edge edge = getEdge(i + 1, isWeighted, edgePattern, nodes, edges, graphData[i]);
            if (edge == null) break;
            edges.add(edge);
        }

        if (i < graphData.length - 1) {
            throw new ParseException(String.format(ERROR_UNKNOWN_REASON, i + 1));
        }
        return new Graph(nodes.values(), edges, isDirected, isWeighted);
    }

    private Edge getEdge(int line, boolean isWeighted, Pattern edgePattern, Map<Integer, Node> nodes, List<Edge> edges, String graphData) throws ParseException {
        Matcher matcher = edgePattern.matcher(graphData);
        if (!matcher.matches()) {
            return null;
        }
        int idFirstNode;
        int idSecondNode;
        double weight;
        try {
            idFirstNode = Integer.parseInt(matcher.group(1));
            idSecondNode = Integer.parseInt(matcher.group(2));
        } catch (NumberFormatException exception) {
            throw new ParseException(String.format(ERROR_MESSAGE_ILLEGAL_EDGE, line));
        }
        Node firstNode = nodes.get(idFirstNode);
        Node secondNode = nodes.get(idSecondNode);
        if (firstNode == null)
            throw new ParseException(String.format(ERROR_MESSAGE_FIRST_NODE_OF_EDGE_DOES_NOT_EXIST, line));
        if (secondNode == null)
            throw new ParseException(String.format(ERROR_MESSAGE_SECOND_NODE_OF_EDGE_DOES_NOT_EXIST, line));
        if (idFirstNode == idSecondNode)
            throw new ParseException(String.format(ERROR_MESSAGE_EDGE_WITH_SAME_NODES, line));
        if (!isWeighted) {
            return new Edge(edges.size() + 1, nodes.get(idFirstNode), nodes.get(idSecondNode));
        }
        try {
            weight = Double.parseDouble(matcher.group(3));
        } catch (NumberFormatException exception) {
            throw new ParseException(String.format(ERROR_MESSAGE_ILLEGAL_EDGE, line));
        }
        return new Edge(edges.size() + 1, nodes.get(idFirstNode), nodes.get(idSecondNode), weight);
    }

    private Node getNode(int line, Pattern nodePattern, int numberOfCoordinates, String graphData) throws ParseException {
        Matcher matcher = nodePattern.matcher(graphData);
        if (!matcher.matches()) {
            return null;
        }
        int nodeId;
        double[] coordinates = new double[numberOfCoordinates];
        try {
            nodeId = Integer.parseInt(matcher.group(1));
            for (int g = 0; g < coordinates.length; g++) {
                coordinates[g] = Double.parseDouble(matcher.group(g + 2));
            }
        } catch (NumberFormatException exception) {
            throw new ParseException(String.format(ERROR_MESSAGE_ILLEGAL_NODE, line));
        }
        return new Node(nodeId, new Coordinates(coordinates));
    }

    private void testStartAndEndOfFile(String[] graphData) throws ParseException {
        if (!graphData[0].equals(START_FILE) || !graphData[graphData.length - 1].equals(END_FILE)) {
            throw new ParseException(ERROR_MESSAGE_ILLEGAL_START_OR_END);
        }
    }
}
