package de.algoviz.algoviz.generators.export_generator;

import java.awt.Color;

/**
 * concrete export-frame for a 2d graph in the big statement type
 *
 * @autor Benedikt
 * @version 1.0
 */
public class TikZFrame2dBigStatement extends TikZFrame2dAbstract {
    @Override
    public TikZFrame2dAbstract getInstance() {
        return new TikZFrame2dBigStatement();
    }

    @Override
    protected void addNode(int id, int xPos, int yPos, String label, Color color, boolean start) {
        String drawColor = defineColor(color);
        stringBuilder.append("\\node");
        stringBuilder.append(String.format("[state, draw=%s, fill=%s, fill opacity=0.25, text opacity = 1%s]", drawColor, drawColor, start ? ", accepting" : ""));
        stringBuilder.append(String.format(" (%d) at(%d,%d) {%s};%n", id, xPos, yPos, label));
    }

    @Override
    protected void addEdge(int idFirstNode, int idSecondNode, Color color, String label, boolean directed) {
        String drawColor = defineColor(color);
        stringBuilder.append(String.format("\\draw[-%s] ", directed ? ">" : ""));
        stringBuilder.append(String.format("(%d) edge[above, draw=%s] node{%s} (%d);%n", idFirstNode, drawColor, label, idSecondNode));
    }
}
