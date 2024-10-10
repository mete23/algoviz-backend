package de.algoviz.algoviz.model.graph_general.graph;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.algoviz.algoviz.model.graph_general.modifications.node.CoordinatesNodeModification;

import java.util.Arrays;
import java.util.Optional;

/**
 * The Coordinates class represents a set of coordinates with a fixed size.
 *
 * @author Metehan, Tim
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
// otherwise int[] coordinates will not be added to JSON
public class Coordinates implements Cloneable {
    private final double[] coordinates;
    private Optional<NodeObserverUpdateInterface> observerUpdater = Optional.empty();

    public Coordinates(int amount) {
        coordinates = new double[amount];
    }

    public Coordinates(double... values) {
        this(values.length);
        for (int i = 0; i < values.length; i++) {
            this.setCoordinate(i, values[i]);
        }
    }

    /**
     * This constructor creates a copy of coordinates
     *
     * @param coordinates the coordinates to copy
     */
    private Coordinates(Coordinates coordinates) {
        this.coordinates = coordinates.coordinates.clone();
    }

    /**
     * This method is used to notify an observer if a coordinate changes
     *
     * @param observerUpdater is called if a coordinate changes
     */
    public void setObserverUpdater(NodeObserverUpdateInterface observerUpdater) {
        this.observerUpdater = Optional.of(observerUpdater);
    }

    /**
     * This function is used to get a Coordinate at a specific index
     *
     * @param index the index of the coordinate
     * @return the value of the coordinate at the index
     */
    public double getCoordinate(int index) {
        return coordinates[index];
    }

    public void setCoordinate(int index, double value) {
        if (!indexIsInRange(index)) {
            return;
        }
        Coordinates oldCoordinates = this.clone();
        this.coordinates[index] = value;
        this.observerUpdater.ifPresent(observer -> observer.updateObserver(new CoordinatesNodeModification(this, oldCoordinates)));
    }

    public int getNumberOfCoordinates() {
        return coordinates.length;
    }

    /**
     * this method is used to get an array of the coordinates
     *
     * @return an array of the coordinates
     */
    public double[] getCoordinatesArray() {
        return Arrays.copyOf(this.coordinates, this.coordinates.length);
    }

    private boolean indexIsInRange(int index) {
        return index >= 0 && index < this.coordinates.length;
    }


    @Override
    public Coordinates clone() {
        return new Coordinates(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Arrays.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinates);
    }

    /**
     * Scales the coordinate to a value between 0 and 1
     *
     * @param minCoordinate will be mapped to 0
     * @param maxCoordinate will be mapped to 1
     */
    public void scale(double minCoordinate, double maxCoordinate) {
        double difference = maxCoordinate - minCoordinate;
        if (difference <= 0) {
            Arrays.fill(coordinates, 0);
            return;
        }
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = (coordinates[i] - minCoordinate) / (difference);
        }
    }
}