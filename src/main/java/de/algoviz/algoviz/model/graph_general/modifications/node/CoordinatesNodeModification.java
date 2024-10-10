package de.algoviz.algoviz.model.graph_general.modifications.node;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

/**
 * record to modify the coordinates of a {@link Node}
 * contains the new coordinates of the node as a {@link Coordinates} object
 *
 * @author Metehan
 * @version 1.0
 */
public record CoordinatesNodeModification(@JsonIgnore Coordinates newCoordinates, @JsonIgnore Coordinates oldCoordinates) implements INodeModification {

    @Override
    public void applyChange(Node node) {
        node.getNodeProperties().setCoordinates(this.newCoordinates);
    }

    /**
     * this is a getter for the coordinates array of a node
     *
     * @return the coordinates array of a node
     */
    @JsonGetter("newCoordinates")
    public double[] getCoordinatesArray() {
        return this.newCoordinates.getCoordinatesArray();
    }

    @JsonGetter("oldCoordinates")
    public double[] getOldCoordinatesArray() {
        return this.oldCoordinates.getCoordinatesArray();
    }
}
