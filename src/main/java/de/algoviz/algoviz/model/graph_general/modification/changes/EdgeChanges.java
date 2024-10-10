package de.algoviz.algoviz.model.graph_general.modification.changes;

import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;

import java.util.LinkedList;
import java.util.List;

/**
 * this class is used to store the changes of a node
 *
 * @author Metehan, Tim
 * @version 1.0
 */
public class EdgeChanges {

    List<IEdgeModification> modificationList = new LinkedList<>();

    /**
     * applies the changes to the given edge
     *
     * @param edge the edge to apply the changes to
     */
    public void applyChanges(Edge edge) {
        for (IEdgeModification iEdgeModification : modificationList) {
            iEdgeModification.applyChange(edge);
        }
    }

    /**
     * getter for the list of modifications
     *
     * @return a list of modifications
     */
    public List<IEdgeModification> getModifications() {
        return this.modificationList;
    }

    /**
     * adds a modification to the list of modifications
     *
     * @param modification the modification to add
     */
    public void addModification(IEdgeModification modification) {
        this.modificationList.add(modification);
    }
}
