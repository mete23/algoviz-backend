package de.algoviz.algoviz.model.graph_general.modification.observable;

import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;

/**
 * this interface is for an Observable {@link de.algoviz.algoviz.model.graph_general.graph.node.Node Node}
 *
 * @author Metehan
 * @version 1.0
 */
public interface NodeObservableInterface extends GraphObservable {
    void notifyObserver(INodeModification modification);
}
