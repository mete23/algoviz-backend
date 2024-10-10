package de.algoviz.algoviz.model.graph_general.modifications.graph;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.observers.GraphObserverInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * this record is used to add a {@link Edge} to a {@link Graph}
 *
 * @param edge the edge to be added
 * @author Metehan, Tim
 * @version 1.0
 */
public record GraphAddEdgeModification(Edge edge) implements IGraphModification {

    public GraphAddEdgeModification(Edge edge) {
        this.edge = edge.clone();
    }

    @Override
    public void applyChange(Graph graph) { // A copy is used because the changes might be applied to a copy of the graph
        Map<Node, Node> nodeHashMap = new HashMap<>();
        nodeHashMap.put(this.edge.getFirstNode(), graph.getNodesMap().get(this.edge.getFirstNode().getId()));
        nodeHashMap.put(this.edge.getSecondNode(), graph.getNodesMap().get(this.edge.getSecondNode().getId()));
        Edge newEdge = this.edge.clone(nodeHashMap);
        for (GraphObserverInterface observer : graph.getObservers()) {
            newEdge.addObserver(observer);
        }
        graph.addEdge(newEdge);
    }
}
