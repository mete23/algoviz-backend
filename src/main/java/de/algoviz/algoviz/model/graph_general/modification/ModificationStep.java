package de.algoviz.algoviz.model.graph_general.modification;

import com.fasterxml.jackson.annotation.JsonGetter;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.graph_general.modification.changes.EdgeChanges;
import de.algoviz.algoviz.model.graph_general.modification.changes.GraphChanges;
import de.algoviz.algoviz.model.graph_general.modification.changes.NodeChanges;
import de.algoviz.algoviz.model.graph_general.modifications.edge.IEdgeModification;
import de.algoviz.algoviz.model.graph_general.modifications.graph.IGraphModification;
import de.algoviz.algoviz.model.graph_general.modifications.node.INodeModification;

import java.util.*;

/**
 * this class is used to store the changes of a graph
 *
 * @author Metehan, Tim
 * @version 1.0
 */
public class ModificationStep {

    private final List<IGraphModification> graphModificationList = new LinkedList<>();

    private final Map<Integer, NodeChanges> changedNodes = new LinkedHashMap<>();
    private final Map<Integer, EdgeChanges> changedEdges = new LinkedHashMap<>();

    /**
     * add all modifications from a {@link GraphChanges} object to this modification step
     *
     * @param modification the {@link GraphChanges} object to add
     */
    public void addModification(GraphChanges modification) {
        this.graphModificationList.addAll(modification.getGraphModificationList());
    }

    /**
     * add all modifications from a {@link ModificationStep} object containing the node to this modification step
     *
     * @param node         the containing node as {@link Node}
     * @param modification the {@link ModificationStep} object to add
     */
    public void addModification(Node node, INodeModification modification) {
        if (Objects.isNull(this.changedNodes.get(node.getId()))) {
            this.changedNodes.put(node.getId(), new NodeChanges());
        }
        this.changedNodes.get(node.getId()).addModification(modification);
    }

    /**
     * add all modifications from a {@link ModificationStep} object containing the edge to this modification step
     *
     * @param edge         the containing edge as {@link Edge}
     * @param modification the {@link ModificationStep} object to add
     */
    public void addModification(Edge edge, IEdgeModification modification) {
        if (Objects.isNull(this.changedEdges.get(edge.getId()))) {
            this.changedEdges.put(edge.getId(), new EdgeChanges());
        }
        this.changedEdges.get(edge.getId()).addModification(modification);
    }

    /**
     * getter for the list of graph modifications
     * used to generate JSON automatically
     *
     * @return the list of graph modifications
     */
    @JsonGetter("graphModifications")
    public List<IGraphModification> getGraphModificationList() {
        return graphModificationList;
    }

    /**
     * This method gets a map of the changes. The key is the id of the node and the value is the change.
     *
     * @return a map of the changes
     */
    @JsonGetter("nodeModifications")
    public Map<Integer, NodeChanges> getChangedNodes() {
        return changedNodes;
    }

    /**
     * This method gets a map of the changes. The key is the id of the edge and the value is the change.
     *
     * @return a map of the changes
     */
    @JsonGetter("edgeModifications")
    public Map<Integer, EdgeChanges> getChangedEdges() {
        return changedEdges;
    }

    /**
     * This method applies all changes to the given graph.
     */
    public void addModification(IGraphModification modification) {
        this.graphModificationList.add(modification);
    }
}
