package de.algoviz.algoviz.external.graph;

import com.fasterxml.jackson.annotation.JsonGetter;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.util.external.ColorConverter;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

/**
 * represents an edge in the external representation of a graph.
 *
 * @author Tim
 * @version 1.0
 */
public class EdgeExternal {
    private static final String NODE_DOES_NOT_EXIST_MESSAGE = "Edge %d could not be created because a node does not exist.";
    private int id;
    private int firstNodeId;
    private int secondNodeId;

    private String color;

    private double weight;

    /**
     * The default constructor is needed for object mapper to convert request body.
     */
    private EdgeExternal() {

    }

    /**
     * creates an edge external object from an edge.
     *
     * @param edge the edge as {@link Edge} to be converted
     */
    public EdgeExternal(Edge edge) {
        this.id = edge.getId();
        this.firstNodeId = edge.getFirstNode().getId();
        this.secondNodeId = edge.getSecondNode().getId();
        this.weight = edge.getWeight();
        this.color = ColorConverter.getColorHexadecimal(edge.getEdgeProperties().getColor());
    }

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    @JsonGetter("sourceId")
    public int getFirstNodeId() {
        return firstNodeId;
    }

    @JsonGetter("targetId")
    public int getSecondNodeId() {
        return secondNodeId;
    }

    @JsonGetter("colorHexadecimal")
    public String getColor() {
        return color;
    }

    @JsonGetter("weight")
    public double getWeight() {
        return weight;
    }

    /**
     * creates an edge from an edge external object.
     *
     * @param nodes    the nodes the edge connects
     * @param weighted whether the edge is weighted
     * @return the edge as {@link Edge}
     */
    public Edge createEdge(Map<Integer, Node> nodes, boolean weighted) throws ParseException {
        Edge edge;
        if (weighted) {
            edge = new Edge(id, nodes.get(firstNodeId), nodes.get(secondNodeId), weight);
        } else {
            edge = new Edge(id, nodes.get(firstNodeId), nodes.get(secondNodeId));
        }
        if (Objects.isNull(edge.getFirstNode()) || Objects.isNull(edge.getSecondNode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NODE_DOES_NOT_EXIST_MESSAGE, id));
        }
        edge.getEdgeProperties().setColor(ColorConverter.getColor(color));
        return edge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstNodeId, secondNodeId, color, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EdgeExternal edge = (EdgeExternal) obj;
        return id == edge.getId()
                && firstNodeId == edge.getFirstNodeId()
                && secondNodeId == edge.getSecondNodeId()
                && color.equals(edge.getColor())
                && weight == edge.getWeight();
    }
}