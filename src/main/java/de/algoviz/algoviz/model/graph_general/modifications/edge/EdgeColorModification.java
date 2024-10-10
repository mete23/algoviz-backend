package de.algoviz.algoviz.model.graph_general.modifications.edge;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.algoviz.algoviz.util.external.ColorConverter;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;

import java.awt.*;

/**
 * this record is used to modify the color of a {@link Edge}.
 *
 * @param newColor the new color of the edge
 * @param oldColor the old color of the edge
 * @author Metehan
 * @version 1.0
 */
public record EdgeColorModification(@JsonIgnore Color newColor,
                                    @JsonIgnore Color oldColor) implements IEdgeModification {

    @Override
    public void applyChange(Edge edge) {
        edge.getEdgeProperties().setColor(this.newColor);
    }

    /**
     * this method is used to get the color of the edge as a hexadecimal string
     *
     * @return the color of the edge as a hexadecimal string
     */
    @JsonGetter("newColor")
    public String getColorHexadecimal() {
        return ColorConverter.getColorHexadecimal(this.newColor);
    }

    @JsonGetter("oldColor")
    public String getOldColorHexadecimal() {
        return ColorConverter.getColorHexadecimal(this.oldColor);
    }
}