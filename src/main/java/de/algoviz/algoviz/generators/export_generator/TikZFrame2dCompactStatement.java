package de.algoviz.algoviz.generators.export_generator;

import java.awt.Color;

/**
 * TikZFrame2dCompactStatement
 *
 * @author Benedikt
 * @version 1.0
 */
public class TikZFrame2dCompactStatement extends TikZFrame2dAbstract {

    @Override
    public TikZFrame2dAbstract getInstance() {
        return new TikZFrame2dCompactStatement();
    }

    @Override
    protected void addNode(int id, int xPos, int yPos, String label, Color color, boolean start) {
        String fillingColor = defineColor(color);
        stringBuilder.append("\\node");
        stringBuilder.append(String.format("[circle, inner sep=4pt, fill=%s%s]", fillingColor, start ? ", accepting" : ""));
        stringBuilder.append(String.format(" (%d) at(%d,%d) {};%n", id, xPos, yPos));
    }

    @Override
    protected void addEdge(int idFirstNode, int idSecondNode, Color color, String label, boolean directed) {
        String drawColor = defineColor(color);
        stringBuilder.append(String.format("\\draw[-%s] ", directed ? ">" : ""));
        stringBuilder.append(String.format("(%d) edge[above, draw=%s] (%d);%n", idFirstNode, drawColor, idSecondNode));
    }
}
