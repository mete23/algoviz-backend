package de.algoviz.algoviz.model.graph_general.modification.changes;

import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.modifications.graph.IGraphModification;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is used to store the changes of a graph and apply them
 *
 * @author Metehan
 * @version 1.0
 */
public class GraphChanges {

    /**
     * the list of changes
     */
    private final List<IGraphModification> graphModificationList = new ArrayList<>();

    /**
     * adds a modification to the list of modifications
     *
     * @param graphModification the modification to add
     */
    public void addChange(IGraphModification graphModification) {
        graphModificationList.add(graphModification);
    }

    /**
     * getter for the list of modifications
     *
     * @return a list of modifications
     */
    public List<IGraphModification> getGraphModificationList() {
        return graphModificationList;
    }


    /**
     * applies the changes to the given graph
     *
     * @param graph the graph to apply the changes to
     */
    public void applyChanges(Graph graph) {
        this.graphModificationList.forEach(modification -> modification.applyChange(graph));
    }
}
