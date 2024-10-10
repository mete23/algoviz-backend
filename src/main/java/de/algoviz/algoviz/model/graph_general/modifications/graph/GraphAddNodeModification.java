package de.algoviz.algoviz.model.graph_general.modifications.graph;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.observers.GraphObserverInterface;

/**
 * this record is used to add a {@link Node} to a {@link Graph}
 *
 * @param node the node to be added
 * @author Metehan
 * @version 1.0
 */
public record GraphAddNodeModification(Node node) implements IGraphModification {

    public GraphAddNodeModification(Node node) {
        this.node = node.clone();
    }

    @Override
    public void applyChange(Graph graph) {
        Node newNode = this.node.clone();
        for (GraphObserverInterface observer : graph.getObservers()) {
            newNode.addObserver(observer);
        }
        graph.addNode(newNode);
    }

}
