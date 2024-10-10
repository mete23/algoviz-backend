package de.algoviz.algoviz.model.graph_general.graph.edge;

import de.algoviz.algoviz.model.graph_general.graph.EdgeObserverUpdateInterface;
import de.algoviz.algoviz.model.graph_general.modifications.edge.EdgeColorModification;

import java.awt.*;
import java.util.Objects;

/**
 * This class encapsulates the optional properties of an edge.
 *
 * @author Metehan
 * @version 1.0
 */
public class EdgeProperties {
    private Color color = Color.BLACK;
    private final EdgeObserverUpdateInterface observerUpdater;

    /**
     * This constructor creates a new EdgeProperties object
     *
     * @param observerUpdater the observerUpdater as {@link EdgeObserverUpdateInterface}
     */
    public EdgeProperties(EdgeObserverUpdateInterface observerUpdater) {
        this.observerUpdater = observerUpdater;
    }

    /**
     * Copy constructor
     *
     * @param edgeProperties  the edgeProperties to copy
     * @param observerUpdater the observerUpdater as {@link EdgeObserverUpdateInterface}
     */
    private EdgeProperties(EdgeProperties edgeProperties, EdgeObserverUpdateInterface observerUpdater) {
        this(observerUpdater);
        this.color = new Color(edgeProperties.getColor().getRGB());
    }

    /**
     * Getter for the color
     *
     * @return the color as {@link Color}
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter for the color
     *
     * @param color the color as {@link Color}
     */
    public void setColor(Color color) {
        Color oldColor = this.color;
        this.color = color;
        observerUpdater.updateObserver(new EdgeColorModification(color, oldColor));
    }

    /**
     * Clones the EdgeProperties
     *
     * @param observerUpdate the observerUpdate as {@link EdgeObserverUpdateInterface}
     * @return the cloned EdgeProperties as {@link EdgeProperties}
     */
    public EdgeProperties clone(EdgeObserverUpdateInterface observerUpdate) {
        return new EdgeProperties(this, observerUpdate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeProperties that = (EdgeProperties) o;
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
