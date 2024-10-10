package de.algoviz.algoviz.model.graph_general.modifications.node;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.algoviz.algoviz.util.external.ColorConverter;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;

import java.awt.*;

/**
 * this record is used to modify the color of {@link Node}.
 *
 * @param newColor the new color of the node
 * @param oldColor the old color of the node
 * @author Metehan
 * @version 1.0
 */
public record ColorNodeModification(@JsonIgnore Color newColor,
                                    @JsonIgnore Color oldColor) implements INodeModification {

    @Override
    public void applyChange(Node node) {
        node.getNodeProperties().setColor(this.newColor);
    }


    /**
     * this method is to get the color as a hexadecimal string
     * this method is used by the automatic JSON generator
     *
     * @return the color as a hexadecimal string
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