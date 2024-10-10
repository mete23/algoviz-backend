package de.algoviz.algoviz.model.graph_general.modification.changes;

import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is used to store the changes of a node
 *
 * @author Metehan, Tim
 * @version 1.0
 */
public class NodeChanges {

    private final List<INodeModification> modificationList = new ArrayList<>();


    public void applyChanges(Node node) {
        for (INodeModification iNodeModification : modificationList) {
            iNodeModification.applyChange(node);
        }
    }

    /**
     * this is a getter for the list of modifications
     *
     * @return the list of modifications
     */
    public List<INodeModification> getModificationList() {
        return modificationList;
    }

    public void addModification(INodeModification modification) {
        modificationList.add(modification);
    }
}
