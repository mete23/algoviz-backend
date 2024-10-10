package de.algoviz.algoviz.model.graph_general.graph.node;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.NodeObserverUpdateInterface;
import de.algoviz.algoviz.model.graph_general.modifications.node.ColorNodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.CoordinatesNodeModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.LabelNodeModification;

import java.awt.*;
import java.util.Objects;

/**
 * this class represents a node in a graph with coordinates.
 *
 * @author Metehan, Tim
 * @version 1.0
 */
public class NodeProperties {
    private final NodeObserverUpdateInterface observerUpdater;
    private Coordinates coordinates;
    private String label;
    private Color color = Color.BLACK;

    /**
     * Constructor for a nodeProperties with coordinates and label
     *
     * @param observerUpdater NodeObserverUpdateInterface to update the observers
     * @param coordinates     the coordinates of the node as {@link Coordinates}
     * @param label           the label of the node as {@link String}
     */
    public NodeProperties(NodeObserverUpdateInterface observerUpdater, Coordinates coordinates, String label) {
        this.observerUpdater = observerUpdater;
        this.coordinates = coordinates;
        this.coordinates.setObserverUpdater(this.observerUpdater);
        this.label = label;
    }

    /**
     * Copy constructor for a nodeProperties
     *
     * @param nodeProperties  NodeProperties to copy
     * @param observerUpdater NodeObserverUpdateInterface to update the observers
     */
    private NodeProperties(NodeProperties nodeProperties, NodeObserverUpdateInterface observerUpdater) {
        this(observerUpdater, nodeProperties.coordinates.clone(), String.valueOf(nodeProperties.getLabel()));
        this.color = new Color(nodeProperties.getColor().getRGB());
    }

    /**
     * Getter for the label
     *
     * @return label as {@link String}
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter for the label
     *
     * @param label label as {@link String}
     */
    public void setLabel(String label) {
        String oldLabel = this.label;
        this.label = label;
        this.observerUpdater.updateObserver(new LabelNodeModification(String.valueOf(label), String.valueOf(oldLabel)));
    }

    /**
     * Getter for the color
     *
     * @return color as {@link Color}
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter for the color
     *
     * @param color color as {@link Color}
     */
    public void setColor(Color color) {
        Color oldColor = this.color;
        this.color = color;
        this.observerUpdater.updateObserver(new ColorNodeModification(color, oldColor));
    }

    /**
     * Getter for the coordinates
     *
     * @return coordinates as {@link Coordinates}
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Setter for the coordinates
     *
     * @param coordinates coordinates as {@link Coordinates}
     */
    public void setCoordinates(Coordinates coordinates) {
        Coordinates oldCoordinates = this.coordinates;
        this.coordinates = coordinates;
        this.observerUpdater.updateObserver(new CoordinatesNodeModification(coordinates, oldCoordinates));
    }

    /**
     * Clones the NodeProperties
     *
     * @param observerUpdater NodeObserverUpdateInterface to update the observers
     * @return the cloned NodeProperties as {@link NodeProperties}
     */
    public NodeProperties clone(NodeObserverUpdateInterface observerUpdater) {
        return new NodeProperties(this, observerUpdater);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeProperties that = (NodeProperties) o;
        return Objects.equals(coordinates, that.coordinates) && Objects.equals(label, that.label) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, label, color);
    }
}