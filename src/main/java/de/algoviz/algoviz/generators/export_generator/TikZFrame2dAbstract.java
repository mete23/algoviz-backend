package de.algoviz.algoviz.generators.export_generator;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.awt.Color;
import java.util.Map;

/**
 * generates an export-frame for 2d graphs
 *
 * @author Benedikt
 * @version 1.0
 */
public abstract class TikZFrame2dAbstract implements ExportFrameInterface {
    protected final StringBuilder stringBuilder = new StringBuilder();
    /**
     * max wide of the TikZ frame in cm
     */
    private static final int MAX_WIDE = 12;
    /**
     * max height of the TikZ frame in cm
     */
    private static final int MAX_HEIGHT = 18;
    private static final String PREAMBLE = "\\usetikzlibrary{arrows, automata}";
    private static final String BEGIN_TIKZ_PICTURE = "\\begin{tikzpicture}[- >, >= stealth', node distance = 2.8 cm, thick]";
    private static final String END_TIKZ_PICTURE = "\\end{tikzpicture}";
    private static final String NEW_LINE = "\n";

    /**
     * generates a TikZ Frame out of a graph
     *
     * @param graph graph instance for generation
     */
    @Override
    public void generateExportData(Graph graph) {

        Map<Integer, Node> startingNodes = graph.getStartingNodesMap();

        stringBuilder.append(PREAMBLE);
        stringBuilder.append(NEW_LINE);
        stringBuilder.append(BEGIN_TIKZ_PICTURE);
        stringBuilder.append(NEW_LINE);

        for (Node actualNode : graph.getNodes()) {
            Coordinates coordinates = actualNode.getNodeProperties().getCoordinates();
            int xPos = defineXPos(coordinates);
            int yPos = defineYPos(coordinates);
            addNode(actualNode.getId(), xPos, yPos, actualNode.getNodeProperties().getLabel(),
                    actualNode.getNodeProperties().getColor(),
                    startingNodes.containsKey(actualNode.getId()));
        }

        stringBuilder.append(NEW_LINE);

        for (Edge actualEdge : graph.getEdges()) {
            String edgeWeight = graph.isWeighted() ? Double.toString(actualEdge.getWeight()) : "";
            addEdge(actualEdge.getFirstNode().getId(), actualEdge.getSecondNode().getId(),
                    actualEdge.getEdgeProperties().getColor(), edgeWeight,
                    graph.isDirected());
        }

        stringBuilder.append(NEW_LINE);
        stringBuilder.append(END_TIKZ_PICTURE);
    }

    @Override
    public String toString() {
        return this.stringBuilder.toString();
    }

    /**
     * defines the color of a node or edge
     *
     * @param color color of the node or edge as {@link Color
     * @return color as string in TikZ format
     */
    protected String defineColor(Color color) {
        if ((color.equals(Color.black)) || (color.equals(Color.BLACK))) {
            return "black";
        }
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return String.format("{rgb:red,%d;green,%d;blue,%d}", red, green, blue);
    }

    private int defineXPos(Coordinates coordinates) {
        double xCoordinate = coordinates.getCoordinate(0);
        // TikZ x-axis is inverted
        return (int) Math.round(xCoordinate * MAX_WIDE);
    }

    private int defineYPos(Coordinates coordinates) {
        double yCoordinate = coordinates.getCoordinate(1);
        // TikZ y-axis is NOT inverted
        return (int) (MAX_HEIGHT - Math.round(yCoordinate * MAX_HEIGHT));
    }

    /**
     * adds a node to the TikZ frame in the right statement type
     *
     * @param id    id of the node as {@link Integer}
     * @param xPos  x-position of the node as {@link Integer}
     * @param yPos  y-position of the node as {@link Integer}
     * @param label label of the node as string
     * @param color color of the node as {@link Color}
     * @param start true if the node is a starting node
     */
    protected abstract void addNode(int id, int xPos, int yPos, String label, Color color, boolean start);

    /**
     * adds an edge to the TikZ frame in the right statement type
     *
     * @param idFirstNode  id of the first node as {@link Integer}
     * @param idSecondNode id of the second node as {@link Integer}
     * @param color        color of the edge as {@link Color}
     * @param label        label of the edge as string
     * @param directed     true if the edge is directed
     */
    protected abstract void addEdge(int idFirstNode, int idSecondNode, Color color, String label, boolean directed);
}
