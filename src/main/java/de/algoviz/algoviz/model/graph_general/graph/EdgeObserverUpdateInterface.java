package de.algoviz.algoviz.model.graph_general.graph;

import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;

/**
 * Interface for the observer of the edge
 *
 * @author Tim
 * @version 1.0
 */
public interface EdgeObserverUpdateInterface {

    /**
     * Updates the observer
     *
     * @param modification the modification as {@link IEdgeModification}
     */
    void updateObserver(IEdgeModification modification);
}
