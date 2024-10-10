package de.algoviz.algoviz.model.graph_general.modification.observers;

import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;
import de.algoviz.algoviz.model.graph_general.modifications.graph.IGraphModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;

/**
 * This class is the observer of the graph
 *
 * @author Metehan
 * @version 1.0
 */
public class GraphObserver implements GraphObserverInterface {
    private ModificationStep modificationStep;

    /**
     * Constructor
     *
     * @param modifications the modification list as {@link ModificationStep}
     */
    public GraphObserver(ModificationStep modifications) {
        this.modificationStep = modifications;
    }

    @Override
    public void setModificationList(ModificationStep modifications) {
        this.modificationStep = modifications;
    }

    @Override
    public void update(Node modificationNode, INodeModification modification) {
        modificationStep.addModification(modificationNode, modification);
    }

    @Override
    public void update(Edge modificationEdge, IEdgeModification modification) {
        modificationStep.addModification(modificationEdge, modification);
    }

    @Override
    public void update(IGraphModification modification) {
        modificationStep.addModification(modification);
    }

}
