package de.algoviz.algoviz.external.graph;

import com.fasterxml.jackson.annotation.JsonGetter;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.parser.ParseException;

import java.util.*;

/**
 * Represents a graph in the external representation of a graph.
 * Contains a list of nodes as {@link List<NodeExternal>}, a list of edges as {@link List<EdgeExternal>},
 * a boolean if the graph is weighted as {@link Boolean} and a boolean if the graph is directed as {@link Boolean}.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
public record GraphExternal(@JsonGetter("nodes") List<NodeExternal> nodes,
                            @JsonGetter("edges") List<EdgeExternal> edges,
                            @JsonGetter("weighted") boolean weighted,
                            @JsonGetter("directed") boolean directed) {

    public GraphExternal(Graph graph) {
        this(graph.getNodes().stream().map(node -> new NodeExternal(node, graph.isStartingNode(node))).toList(),
                graph.getEdges().stream().map(EdgeExternal::new).toList(),
                graph.isWeighted(), graph.isDirected());
    }

    public Graph createGraph() throws ParseException {
        // A map with the id of the node and its new instance
        Map<Integer, Node> nodesMap = new HashMap<>();
        for (NodeExternal node : this.nodes) {
            nodesMap.put(node.getId(), node.createNode());
        }
        List<Edge> edges = new ArrayList<>();
        for (EdgeExternal edge: this.edges){
            edges.add(edge.createEdge(nodesMap,this.weighted));
        }

        Graph graph = new Graph(nodesMap.values(), edges, this.directed, this.weighted);
        for (NodeExternal nodeExternal : this.nodes) {
            if (nodeExternal.isStartingNode()) {
                graph.addStartingNode(nodeExternal.createNode());
            }
        }
        return graph;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GraphExternal graph = (GraphExternal) obj;
        return weighted == graph.weighted
                && directed == graph.directed
                && new HashSet<>(nodes).containsAll(graph.nodes) && new HashSet<>(graph.nodes).containsAll(nodes)
                && new HashSet<>(edges).containsAll(graph.edges) && new HashSet<>(graph.edges).containsAll(edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges, weighted, directed);
    }
}