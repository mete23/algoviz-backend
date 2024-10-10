package de.algoviz.algoviz.model.graph_general.graph;

import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modification.changes.EdgeChanges;
import de.algoviz.algoviz.model.graph_general.modification.changes.NodeChanges;
import de.algoviz.algoviz.model.graph_general.modification.observable.GraphGraphObservable;
import de.algoviz.algoviz.model.graph_general.modification.observers.GraphObserverInterface;
import de.algoviz.algoviz.model.graph_general.modifications.graph.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a graph with nodes and edges
 *
 * @author Metehan, Tim
 * @version 1.0
 */
public class Graph implements Cloneable, GraphGraphObservable {
    private final boolean directed;
    private final boolean weighted;
    private final Set<GraphObserverInterface> observerList = new HashSet<>();

    /**
     * The starting nodes of the graph with node id as key
     */
    private final Map<Integer, Node> startingNodes = new ConcurrentHashMap<>();

    /**
     * The nodes of the graph with node id as key
     */
    private final Map<Integer, Node> nodes = new ConcurrentHashMap<>();

    /**
     * The edges of the graph with node id as key
     */
    private final Map<Integer, Edge> edges = new ConcurrentHashMap<>();

    /**
     * Constructor creates a graph with nodes and edges
     *
     * @param nodes    the nodes of the graph
     * @param edges    the edges of the graph
     * @param directed true if the graph is directed
     * @param weighted true if the graph is weighted
     */
    public Graph(Collection<Node> nodes, Collection<Edge> edges, boolean directed, boolean weighted) {
        setNodes(nodes);
        setEdges(edges);
        this.directed = directed;
        this.weighted = weighted;
        scaleGraph();
    }

    /**
     * Copy constructor
     *
     * @param graph the graph to copy
     */
    private Graph(Graph graph) {

        this.weighted = graph.weighted;
        this.directed = graph.directed;

        Map<Node, Node> nodesMap = new HashMap<>();

        //create copy of Nodes and put copies into map
        for (Node node : graph.nodes.values()) {
            Node clone = node.clone();
            this.nodes.put(clone.getId(), clone);
            nodesMap.put(node, clone);
        }

        // set starting node
        for (Node startingNode : graph.startingNodes.values()) {
            this.startingNodes.put(startingNode.getId(), graph.nodes.get(startingNode.getId()));
        }

        // create copy of Edges
        for (Edge edge : graph.edges.values()) {
            this.edges.put(edge.getId(), edge.clone(nodesMap));
        }
    }

    /**
     * scales the coordinates of the nodes to a value between 0 and 1
     */
    private void scaleGraph() {
        if (this.nodes.isEmpty()) {
            return;
        }
        double maxCoordinate = Double.NEGATIVE_INFINITY;
        double minCoordinate = Double.POSITIVE_INFINITY;

        for (Node node : this.nodes.values()) {
            double[] nodeCoordinates = node.getNodeProperties().getCoordinates().getCoordinatesArray();
            maxCoordinate = Double.max(Arrays.stream(nodeCoordinates).max().getAsDouble(), maxCoordinate);
            minCoordinate = Double.min(Arrays.stream(nodeCoordinates).min().getAsDouble(), minCoordinate);
        }

        for (Node node : this.nodes.values()) {
            node.getNodeProperties().getCoordinates().scale(minCoordinate, maxCoordinate);
        }

    }

    /**
     * Returns a starting node of the graph. If the graph does not have any starting nodes a random node will be returned.
     *
     * @return A starting node of the graph
     */
    public Node getStartingNode() {
        for (Node startingNode : this.startingNodes.values()) {
            return startingNode;
        }
        for (Node node : this.nodes.values()) {
            return node;
        }
        return null;
    }

    /**
     * creates nodes map
     *
     * @param nodes the nodes of the graph
     */
    private void setNodes(Collection<Node> nodes) {
        this.nodes.clear();
        for (Node node : nodes) {
            this.nodes.put(node.getId(), node);
        }
    }

    /**
     * creates edges map
     *
     * @param edges the edges of the graph
     */
    private void setEdges(Collection<Edge> edges) {
        this.edges.clear();
        for (Edge edge : edges) {
            this.edges.put(edge.getId(), edge);
        }
    }

    public Collection<Node> getNodes() {
        return nodes.values();
    }

    public Collection<Edge> getEdges() {
        return edges.values();
    }

    /**
     * This method gets a map with nodes as value and their id as key.
     *
     * @return a map of nodes with their id
     */
    public Map<Integer, Node> getNodesMap() {
        return this.nodes;
    }

    /**
     * This method gets a map with edges as value and their id as key.
     *
     * @return a map of edges with their id
     */
    public Map<Integer, Edge> getEdgesMap() {
        return this.edges;
    }

    /**
     * This method gets a map with the starting nodes as value and their id as key.
     *
     * @return a map of starting nodes with their id
     */
    public Map<Integer, Node> getStartingNodesMap() {
        return this.startingNodes;
    }

    /**
     * This method checks if a node is a starting node of the graph.
     *
     * @param node the node
     * @return true if the node is a starting node
     */
    public boolean isStartingNode(Node node) {
        return this.startingNodes.containsKey(node.getId());
    }

    public Map<Integer, AdjacencyListEntry> getAdjacencyListMap() {
        Map<Integer, AdjacencyListEntry> adjacencyList = new HashMap<>();

        for (Node node : nodes.values()) {
            adjacencyList.put(node.getId(), new AdjacencyListEntry(node));
        }
        for (Edge edge : edges.values()) {
            AdjacencyListEntry firstEntry = adjacencyList.get(edge.getFirstNode().getId());
            AdjacencyListEntry secondEntry = adjacencyList.get(edge.getSecondNode().getId());
            firstEntry.addAdjacencyEntry(secondEntry, edge);
            if (!directed) {
                secondEntry.addAdjacencyEntry(firstEntry, edge);
            }
        }
        return adjacencyList;
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        notifyObserver(new GraphAddNodeModification(node));
    }

    public void removeNode(Node node) {
        List<Edge> removedEdges = new ArrayList<>();
        for(Map.Entry<Integer, Edge> entry : edges.entrySet()){
            if (entry.getValue().getFirstNode().getId() == node.getId() || entry.getValue().getSecondNode().getId() == node.getId()){
                removedEdges.add(entry.getValue());
            }
        }
        edges.entrySet().removeIf(edge -> edge.getValue().getFirstNode().getId() == node.getId() || edge.getValue().getSecondNode().getId() == node.getId());
        nodes.remove(node.getId());
        notifyObserver(new GraphRemoveNodeModification(node, removedEdges));
    }

    public void addEdge(Edge edge) {
        edges.put(edge.getId(), edge);
        notifyObserver(new GraphAddEdgeModification(edge));
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge.getId());
        notifyObserver(new GraphRemoveEdgeModification(edge));
    }

    @Override
    public void addObserver(GraphObserverInterface observer) {
        this.observerList.add(observer);
        for (Node node : nodes.values()) {
            node.addObserver(observer);
        }
        for (Edge edge : edges.values()) {
            edge.addObserver(observer);
        }
    }

    @Override
    public void removeObserver(GraphObserverInterface observer) {
        this.observerList.remove(observer);
        nodes.values().forEach(node -> node.removeObserver(observer));
        edges.values().forEach(edge -> edge.removeObserver(observer));
    }

    @Override
    public Set<GraphObserverInterface> getObservers() {
        return Set.copyOf(observerList);
    }

    public void addStartingNode(Node node) {
        startingNodes.put(node.getId(), node);
    }


    /**
     * This method tells if the graph is directed.
     *
     * @return true if the graph is directed, false otherwise
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * This method tells if the graph is weighted.
     *
     * @return true if the graph is weighted, false otherwise
     */
    public boolean isWeighted() {
        return weighted;
    }

    @Override
    public Graph clone() {
        return new Graph(this);
    }


    @Override
    public void notifyObserver(IGraphModification modification) {
        this.observerList.forEach(observer -> observer.update(modification));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return directed == graph.directed && weighted == graph.weighted && Objects.equals(startingNodes, graph.startingNodes) && Objects.equals(nodes, graph.nodes) && Objects.equals(edges, graph.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directed, weighted, startingNodes, nodes, edges);
    }

    public void applyChanges(ModificationStep modificationStep) {
        Map<Integer, NodeChanges> changedNodes = modificationStep.getChangedNodes();
        Map<Integer, EdgeChanges> changedEdges = modificationStep.getChangedEdges();
        List<IGraphModification> graphChanges = modificationStep.getGraphModificationList();

        for (IGraphModification graphChange : graphChanges) {
            graphChange.applyChange(this);
        }
        for (Map.Entry<Integer, NodeChanges> changedNode : changedNodes.entrySet()) {
            changedNode.getValue().applyChanges(nodes.get(changedNode.getKey()));
        }
        for (Map.Entry<Integer, EdgeChanges> changedEdge : changedEdges.entrySet()) {
            changedEdge.getValue().applyChanges(edges.get(changedEdge.getKey()));
        }
    }
}
