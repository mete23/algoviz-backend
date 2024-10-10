package de.algoviz.algoviz.model.graph_general.modification.observable;

import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;

/**
 * this interface is for an Observable {@link de.algoviz.algoviz.model.graph_general.graph.edge.Edge Edge}
 *
 * @author Metehan
 * @version 1.0
 */
public interface EdgeObservable extends GraphObservable {
    void notifyObserver(IEdgeModification modification);

}
