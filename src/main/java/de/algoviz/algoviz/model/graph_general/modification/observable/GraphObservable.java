package de.algoviz.algoviz.model.graph_general.modification.observable;

import de.algoviz.algoviz.model.graph_general.modification.observers.GraphObserverInterface;

import java.util.Set;

/**
 * this interface is for an Observable Component of a graph.
 *
 * @author Metehan
 * @version 1.0
 */
public interface GraphObservable {
    void addObserver(GraphObserverInterface observer);

    void removeObserver(GraphObserverInterface observer);

    Set<GraphObserverInterface> getObservers();
}
