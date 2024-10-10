package de.algoviz.algoviz.model.graph_general.graph.node;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.NodeObserverUpdateInterface;
import de.algoviz.algoviz.model.graph_general.modification.observable.NodeObservableInterface;
import de.algoviz.algoviz.model.graph_general.modification.observers.GraphObserverInterface;
import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * this class represents a node in a graph with coordinates.
 *
 * @author Metehan, Tim
 * @version 1.0
 */
public class Node implements NodeObservableInterface, Cloneable {
    private final int id;
    private final NodeProperties nodeProperties;

    private final List<GraphObserverInterface> observerList = new ArrayList<>();

    private final NodeObserverUpdateInterface updateObservers = this::notifyObserver;

    /**
     * Constructor for a node with id and label
     *
     * @param id          the id of the node as {@link int}
     * @param coordinates the coordinates of the node as {@link Coordinates}
     * @param label       the label of the node as {@link String}
     */
    public Node(int id, Coordinates coordinates, String label) {
        this.nodeProperties = new NodeProperties(updateObservers, coordinates, label);
        this.id = id;
    }

    /**
     * Constructor for a node with id and coordinates
     *
     * @param id          the id of the node as {@link int}
     * @param coordinates the coordinates of the node as {@link Coordinates}
     */
    public Node(int id, Coordinates coordinates) {
        this(id, coordinates, "");
    }


    /**
     * constructor with coordinates, label and random id
     *
     * @param coordinates coordinates of the node as {@link Coordinates}
     * @param label       label of the node as {@link String}
     */
    public Node(Coordinates coordinates, String label) {
        this.nodeProperties = new NodeProperties(updateObservers, coordinates, label);
        this.id = System.identityHashCode(this);
    }

    /**
     * constructor with coordinates and random id
     *
     * @param coordinates coordinates of the node as {@link Coordinates}
     */
    public Node(Coordinates coordinates) {
        this(coordinates, "");
    }

    /**
     * copy constructor
     *
     * @param node node to clone
     */
    private Node(Node node) {
        this.id = node.id;
        this.nodeProperties = node.nodeProperties.clone(updateObservers);
    }

    /**
     * Getter for the id
     *
     * @return the id as {@link int}
     */
    public int getId() {
        return id;
    }

    @Override
    public void notifyObserver(INodeModification modification) {
        for (GraphObserverInterface o : observerList) {
            o.update(this, modification);
        }
    }

    public NodeProperties getNodeProperties() {
        return nodeProperties;
    }

    @Override
    public void addObserver(GraphObserverInterface observer) {
        this.observerList.add(observer);
    }

    @Override
    public void removeObserver(GraphObserverInterface observer) {
        this.observerList.remove(observer);
    }

    @Override
    public Set<GraphObserverInterface> getObservers() {
        return Set.copyOf(observerList);
    }

    @Override
    public Node clone() {
        return new Node(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id && Objects.equals(nodeProperties, node.nodeProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nodeProperties);
    }
}
