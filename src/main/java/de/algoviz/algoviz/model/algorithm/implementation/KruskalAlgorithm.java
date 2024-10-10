package de.algoviz.algoviz.model.algorithm.implementation;

import de.algoviz.algoviz.model.algorithm.AlgorithmInterface;
import de.algoviz.algoviz.model.algorithm.implementation.util.UnionFindElement;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Kruskal's Algorithm
 *
 * @author Tim
 * @version 1.0
 */
public class KruskalAlgorithm implements AlgorithmInterface {

    public static final String NAME = "Kruskal's Algorithm";
    public static final String DESCRIPTION = NAME + " is an algorithm to find a minimum spanning forest.";

    private static final Color COLOR_EDGE_SELECTED = new Color(0,128,0);

    private static final Color COLOR_EDGE_NOT_SELECTED = Color.GRAY;
    private final Queue<Edge> edges = new LinkedList<>();
    private Map<Integer, UnionFindElement> unionFindNodes;

    @Override
    public void initialize(Graph graph) {

        // initialize queue of edges sorted by weight
        graph.getEdges().stream().sorted(Comparator.comparingDouble(Edge::getWeight)
                        .thenComparingInt(edge -> edge.getFirstNode().getId())
                        .thenComparingInt(edge -> edge.getSecondNode().getId())
                        .thenComparingInt(Edge::getId))
                .forEach(this.edges::add);

        // initialize union find data structure
        unionFindNodes = graph.getNodesMap().keySet().stream().collect(Collectors.toMap(id -> id, id -> new UnionFindElement()));

        if(!isDone()){
            next();
        }
    }

    @Override
    public void next() {

        Edge edge = edges.poll();

        UnionFindElement firstRepresentative = unionFindNodes.get(edge.getFirstNode().getId()).find();
        UnionFindElement secondRepresentative = unionFindNodes.get(edge.getSecondNode().getId()).find();

        // do not select edge
        if (firstRepresentative == secondRepresentative) {
            edge.getEdgeProperties().setColor(COLOR_EDGE_NOT_SELECTED);
            return;
        }

        // select edge
        union(firstRepresentative, secondRepresentative);
        edge.getEdgeProperties().setColor(COLOR_EDGE_SELECTED);
    }

    @Override
    public boolean isDone() {
        return edges.isEmpty();
    }

    private void union(UnionFindElement firstRepresentative, UnionFindElement secondRepresentative) {

        if (firstRepresentative.getRank() < secondRepresentative.getRank()) {
            firstRepresentative.setParent(secondRepresentative);
            return;
        }

        if (firstRepresentative.getRank() == secondRepresentative.getRank()) {
            firstRepresentative.increaseRank();
        }
        secondRepresentative.setParent(firstRepresentative);
    }
}
