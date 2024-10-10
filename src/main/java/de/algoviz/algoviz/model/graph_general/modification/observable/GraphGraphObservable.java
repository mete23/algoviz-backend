package de.algoviz.algoviz.model.graph_general.modification.observable;

import de.algoviz.algoviz.model.graph_general.modifications.graph.IGraphModification;

/**
 * this interface is for an Observable Graph
 *
 * @author Metehan
 * @version 1.0
 */
public interface GraphGraphObservable extends GraphObservable {
    void notifyObserver(IGraphModification modification);
}
