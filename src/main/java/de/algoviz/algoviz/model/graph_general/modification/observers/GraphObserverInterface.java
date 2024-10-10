package de.algoviz.algoviz.model.graph_general.modification.observers;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;
import de.algoviz.algoviz.model.graph_general.modifications.graph.IGraphModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;

/**
 * Interface for the observer of the graph.
 *
 * @author Metehan
 * @version 1.0
 */
public interface GraphObserverInterface {

    /**
     * Sets the modification list to the given one.
     *
     * @param modifications The new modification list.
     */
    void setModificationList(ModificationStep modifications);

    /**
     * Updates the observer with the given modification of the {@link Node}.
     *
     * @param modifiedNode The node that was modified.
     * @param modification The modification that was applied to the node.
     */
    void update(Node modifiedNode, INodeModification modification);

    /**
     * Updates the observer with the given modification of th {@link Edge}.
     *
     * @param modifiedEdge The edge that was modified.
     * @param modification The modification that was applied to the edge.
     */
    void update(Edge modifiedEdge, IEdgeModification modification);

    /**
     * Updates the observer with the given modification of the {@link Graph}.
     *
     * @param modification The modification that was applied to the graph.
     */
    void update(IGraphModification modification);
}
