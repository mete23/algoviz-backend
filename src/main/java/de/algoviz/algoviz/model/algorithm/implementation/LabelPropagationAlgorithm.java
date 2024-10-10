package de.algoviz.algoviz.model.algorithm.implementation;

import de.algoviz.algoviz.model.algorithm.AlgorithmInterface;
import de.algoviz.algoviz.model.graph_general.graph.AdjacencyListEntry;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Label Propagation Algorithm
 *
 * @author Tim
 * @version 1.0
 */
public class LabelPropagationAlgorithm implements AlgorithmInterface {

    public static final String NAME = "Label Propagation Algorithm";
    public static final String DESCRIPTION = NAME + " is an algorithm to label nodes based on clusters.";
    private static final float STEPS_PER_ENTRY = 0.2f;
    private static final int MIN_NUMBER_OF_STEPS = 100;

    private final Map<String, Color> labelColorMap = new HashMap<>();

    /**
     * adjacency entries which are ready to be labeled
     */
    private List<AdjacencyListEntry> entries;

    private Random random;
    private boolean isDone = false;
    private int remainingSteps;

    @Override
    public void initialize(Graph graph) {

        // add labeled nodes to visitedNodes and add their adjacency nodes to queue
        entries = graph.getAdjacencyListMap().values().stream().sorted(
                Comparator
                        .comparingInt((AdjacencyListEntry e) -> e.getEdgesAdjacencyNodes().size())
                        .thenComparingInt(e -> e.getEntryNode().getId())
        ).collect(Collectors.toList());
        random = new Random(entries.size());
        remainingSteps = MIN_NUMBER_OF_STEPS + Math.round(graph.getNodes().size() * STEPS_PER_ENTRY);

        int index = 0;
        for (AdjacencyListEntry entry : entries) {
            String label = String.valueOf(index);
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

            labelColorMap.put(label, color);
            entry.getEntryNode().getNodeProperties().setLabel(label);
            entry.getEntryNode().getNodeProperties().setColor(color);
            index++;
        }
    }

    @Override
    public void next() {
        isDone = true;
        remainingSteps--;
        for (AdjacencyListEntry entry : this.entries) {
            changeLabelOfEntry(entry);
        }
    }

    @Override
    public boolean isDone() {
        return this.isDone || remainingSteps <= 0;
    }

    private void changeLabelOfEntry(AdjacencyListEntry entry) {
        // count labels of adjacency entries
        HashMap<String, Double> adjacencyLabels = new HashMap<>();
        Iterator<AdjacencyListEntry> entryIterator = entry.getAdjacencyEntries().iterator();
        Iterator<Edge> edgeIterator = entry.getEdgesAdjacencyNodes().iterator();
        while (entryIterator.hasNext() && edgeIterator.hasNext()) {
            AdjacencyListEntry adjacencyEntry = entryIterator.next();
            double weight = edgeIterator.next().getWeight();
            String label = adjacencyEntry.getEntryNode().getNodeProperties().getLabel();
            double numberOfUses = adjacencyLabels.getOrDefault(label, 0.0);
            adjacencyLabels.put(label, numberOfUses + weight);
        }

        // select most used labels
        List<String> mostUsedLabels = new ArrayList<>();
        double numberOfUses = 0;
        for (Map.Entry<String, Double> pair : adjacencyLabels.entrySet()) {

            if (pair.getValue() > numberOfUses || mostUsedLabels.isEmpty()) {
                mostUsedLabels.clear();
                numberOfUses = pair.getValue();
                mostUsedLabels.add(pair.getKey());

            } else if (pair.getValue() == numberOfUses) {
                mostUsedLabels.add(pair.getKey());
            }
        }
        if (mostUsedLabels.isEmpty()) {
            return;
        }

        // get new Label
        String label = mostUsedLabels.get(random.nextInt(mostUsedLabels.size()));
        if (entry.getEntryNode().getNodeProperties().getLabel().equals(label)) {
            return;
        }

        // label has changed
        this.isDone = false;
        entry.getEntryNode().getNodeProperties().setLabel(label);
        entry.getEntryNode().getNodeProperties().setColor(labelColorMap.get(label));
    }
}
