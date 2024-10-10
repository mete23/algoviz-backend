package de.algoviz.algoviz.external.graph;

import com.fasterxml.jackson.annotation.JsonGetter;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.util.external.ColorConverter;
import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a node in the external representation of a graph.
 *
 * @author Tim
 * @version 1.0
 */
public class NodeExternal {

    private int id;
    private double[] coordinates;
    private String label;
    private boolean startingNode;
    private String color;

    /**
     * The default constructor is needed for object mapper to convert request body.
     */
    private NodeExternal() {

    }

    /**
     * creates a node external object from a node.
     *
     * @param node         the node as {@link Node} to be converted
     * @param startingNode whether the node is a starting node
     */
    public NodeExternal(Node node, boolean startingNode) {
        this.id = node.getId();
        this.coordinates = node.getNodeProperties().getCoordinates().getCoordinatesArray();
        this.label = node.getNodeProperties().getLabel();
        this.startingNode = startingNode;
        this.color = ColorConverter.getColorHexadecimal(node.getNodeProperties().getColor());
    }

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    @JsonGetter("coordinates")
    public double[] getCoordinates() {
        return coordinates;
    }

    @JsonGetter("label")
    public String getLabel() {
        return label;
    }

    @JsonGetter("startingNode")
    public boolean isStartingNode() {
        return startingNode;
    }

    @JsonGetter("colorHexadecimal")
    public String getColor() {
        return color;
    }

    /**
     * creates a node from the node external object.
     *
     * @return the node as {@link Node}
     */
    public Node createNode() throws ParseException {
        Node node = new Node(id, new Coordinates(this.coordinates), label);
        node.getNodeProperties().setColor(ColorConverter.getColor(color));
        return node;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(coordinates), label, startingNode, color);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NodeExternal edge = (NodeExternal) obj;
        return id == edge.getId()
                && Arrays.equals(coordinates, edge.getCoordinates())
                && label.equals(edge.getLabel())
                && startingNode == edge.isStartingNode()
                && color.equals(edge.getColor());
    }
}