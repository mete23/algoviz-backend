package de.algoviz.algoviz.external.modification;

import de.algoviz.algoviz.external.modification.graph.*;
import de.algoviz.algoviz.model.graph_general.modification.ModificationStep;
import de.algoviz.algoviz.model.graph_general.modifications.graph.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of node and edge modifications.
 * Contains a list of node changes as {@link NodeChangesExternal}, a list of edge changes as {@link EdgeChangesExternal}
 * and a list of graph changes as {@link IGraphModification}.
 *
 * @author Benedikt, Tim
 * @version 1.0
 */
public record ModificationStepExternal(List<NodeChangesExternal> nodeChanges, List<EdgeChangesExternal> edgeChanges,
                                       List<IGraphChanges> graphChanges) {

    /**
     * Creates a new {@link ModificationStepExternal} from a {@link ModificationStep}.
     *
     * @param modificationStep the {@link ModificationStep} to convert
     */
    public ModificationStepExternal(ModificationStep modificationStep) {
        this(modificationStep.getChangedNodes().entrySet().stream()
                        .map(entry -> new NodeChangesExternal(entry.getKey(), entry.getValue().getModificationList())).toList(),
                modificationStep.getChangedEdges().entrySet().stream()
                        .map(entry -> new EdgeChangesExternal(entry.getKey(), entry.getValue().getModifications())).toList(),
                createGraphChanges(modificationStep.getGraphModificationList()));

    }

    private static List<IGraphChanges> createGraphChanges(List<IGraphModification> graphModification) {
        List<IGraphChanges> graphChanges = new ArrayList<>();
        for (IGraphModification graphMod : graphModification) {
            if (graphMod instanceof GraphAddEdgeModification graphAddEdgeModification) {
                graphChanges.add(new GraphAddEdgeExternal(graphAddEdgeModification.edge()));
            } else if (graphMod instanceof GraphRemoveEdgeModification graphRemoveEdgeModification) {
                graphChanges.add(new GraphDeleteEdgeExternal(graphRemoveEdgeModification.edge()));
            } else if (graphMod instanceof GraphAddNodeModification graphAddNodeModification) {
                graphChanges.add(new GraphAddNodeExternal(graphAddNodeModification.node(), false));
            } else if (graphMod instanceof GraphRemoveNodeModification graphRemoveNodeModification) {
                graphChanges.add(new GraphDeleteNodeExternal(graphRemoveNodeModification.node(), graphRemoveNodeModification.removedEdges(), false));
            }
        }
        return graphChanges;
    }
}
