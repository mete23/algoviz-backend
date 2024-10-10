package de.algoviz.algoviz.model.graph_general.graph;

import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;

/**
 * Interface for the observer of the node
 *
 * @author Tim
 * @version 1.0
 */
public interface NodeObserverUpdateInterface {
    /**
     * Updates the observer
     *
     * @param modification the modification as {@link INodeModification}
     */
    void updateObserver(INodeModification modification);
}
