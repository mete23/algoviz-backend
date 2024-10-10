package de.algoviz.algoviz.model.algorithm.implementation.util;

/**
 * This class represents an element of a union find data structure.
 *
 * @author Tim
 * @version 1.0
 */
public class UnionFindElement {
    private UnionFindElement parent = this;
    private int rank = 0;

    /**
     * Finds the representative of the set this element is in.
     *
     * @return the representative of the set this element is in
     */
    public UnionFindElement find() {
        if (parent == this) {
            return this;
        }
        parent = parent.find(); // path compression
        return parent;
    }

    /**
     * Sets the parent of this element.
     *
     * @param parent the parent of this element
     */
    public void setParent(UnionFindElement parent) {
        this.parent = parent;
    }

    /**
     * Returns the rank of this element.
     *
     * @return the rank of this element
     */
    public int getRank() {
        return rank;
    }

    /**
     * Increases the rank of this element by one.
     */
    public void increaseRank() {
        rank++;
    }
}