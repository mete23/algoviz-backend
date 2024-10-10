package de.algoviz.algoviz.generators.export_generator;

import de.algoviz.algoviz.model.graph_general.graph.Graph;

/**
 * interface all exportable files musst implement
 *
 * @author Benedikt
 * @version 1.0
 */
public interface ExportFrameInterface {
    /**
     * generates out of the graph an exportFrame
     *
     * @param graph graph for generation
     */
    void generateExportData(Graph graph);

    /**
     * getter-method for the concrete instance
     *
     * @return an instance of the exportFrameGenerator
     */
    ExportFrameInterface getInstance();

    @Override
    String toString();
}
