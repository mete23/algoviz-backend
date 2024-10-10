package de.algoviz.algoviz.model.graph_general.graph.edge;

import de.algoviz.algoviz.model.graph_general.graph.EdgeObserverUpdateInterface;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.observable.EdgeObservable;
import de.algoviz.algoviz.model.graph_general.modification.observers.GraphObserverInterface;
import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;

import java.util.*;

/**
 * An edge with two Nodes with specified order. An edge has no weight
 *
 * @author Metehan
 * @version 1.0
 */
public class Edge implements EdgeObservable {
    private static final double DEFAULT_WEIGHT = 1;
    private final int id;
    private final Node firstNode;
    private final Node secondNode;
    private final List<GraphObserverInterface> observer = new LinkedList<>();
    private final double weight;
    private final EdgeObserverUpdateInterface updateObservers = this::notifyObserver;
    private EdgeProperties edgeProperties = new EdgeProperties(updateObservers);

    /**
     * This constructor creates a new unweighted Edge
     *
     * @param firstNode  the first node of the edge as {@link Node}
     * @param secondNode the second node of the edge as {@link Node}
     */
    public Edge(Node firstNode, Node secondNode) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.weight = DEFAULT_WEIGHT;
        this.id = System.identityHashCode(this);
    }

    /**
     * This constructor creates a new weighted Edge with a certain weight
     *
     * @param firstNode  the first node of the edge as {@link Node}
     * @param secondNode the second node of the edge as {@link Node}
     * @param weight     the weight of the edge as {@link double}
     */
    public Edge(Node firstNode, Node secondNode, double weight) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.weight = weight;
        this.id = System.identityHashCode(this);
    }

    /**
     * This constructor creates a new Edge with a certain id
     *
     * @param firstNode  the first node of the edge as {@link Node}
     * @param secondNode the second node of the edge as {@link Node}
     * @param id         the id of the edge as {@link Integer}
     */
    public Edge(int id, Node firstNode, Node secondNode) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.weight = DEFAULT_WEIGHT;
        this.id = id;
    }

    /**
     * This constructor creates a new weighted Edge with a certain weight and id
     *
     * @param id         the id of the edge as {@link Integer}
     * @param firstNode  the first node of the edge as {@link Node}
     * @param secondNode the second node of the edge as {@link Node}
     * @param weight     the weight of the edge as {@link double}
     */
    public Edge(int id, Node firstNode, Node secondNode, double weight) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.weight = weight;
        this.id = id;
    }

    /**
     * Copy constructor
     *
     * @param edge                 the edge to be copied
     * @param pairOfNodesAndClones a map of the original nodes and their clones
     */
    private Edge(Edge edge, Map<Node, Node> pairOfNodesAndClones) {
        this.firstNode = pairOfNodesAndClones.get(edge.firstNode);
        this.secondNode = pairOfNodesAndClones.get(edge.secondNode);
        this.weight = edge.getWeight();
        this.id = edge.id;
        this.edgeProperties = edge.edgeProperties.clone(updateObservers);
    }

    /**
     * Copy constructor
     *
     * @param edge the edge to be copied
     */
    private Edge(Edge edge) {
        this.firstNode = edge.firstNode;
        this.secondNode = edge.secondNode;
        this.weight = edge.getWeight();
        this.id = edge.id;
        this.edgeProperties = edge.edgeProperties.clone(updateObservers);
    }

    /**
     * Getter for the first node of the edge
     *
     * @return the first node of the edge as {@link Node}
     */
    public Node getFirstNode() {
        return firstNode;
    }

    /**
     * Getter for the second node of the edge
     *
     * @return the second node of the edge as {@link Node}
     */
    public Node getSecondNode() {
        return secondNode;
    }

    /**
     * Getter for the edge properties
     *
     * @return the edge properties as {@link EdgeProperties}
     */
    public EdgeProperties getEdgeProperties() {
        return this.edgeProperties;
    }

    /**
     * Getter for the weight of the edge
     *
     * @return the weight of the edge as {@link double}
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Getter for the id of the edge
     *
     * @return the id of the edge as {@link Integer}
     */
    public int getId() {
        return id;
    }

    @Override
    public void notifyObserver(IEdgeModification modification) {
        for (GraphObserverInterface o : observer) {
            o.update(this, modification);
        }
    }

    @Override
    public void addObserver(GraphObserverInterface observer) {
        this.observer.add(observer);
    }

    @Override
    public void removeObserver(GraphObserverInterface observer) {
        this.observer.remove(observer);
    }

    @Override
    public Set<GraphObserverInterface> getObservers() {
        return Set.copyOf(observer);
    }


    /**
     * creates clone of the edge. The nodes of the edge will be according to the mapping
     *
     * @param pairOfNodesAndClones a map with a node as key and a node as value. The new nodes of the edge will be the value of the node.
     * @return the cloned edge
     */
    public Edge clone(Map<Node, Node> pairOfNodesAndClones) {
        return new Edge(this, pairOfNodesAndClones);
    }

    /**
     * 7
     * creates clone of the edge. The adjacency nodes will not be cloned.
     *
     * @return the edge
     */
    public Edge clone() {
        return new Edge(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return id == edge.id && Double.compare(edge.weight, weight) == 0 && Objects.equals(edgeProperties, edge.edgeProperties) && Objects.equals(firstNode, edge.firstNode) && Objects.equals(secondNode, edge.secondNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edgeProperties, id, firstNode, secondNode, weight);
    }
}
