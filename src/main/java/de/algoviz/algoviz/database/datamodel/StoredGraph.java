package de.algoviz.algoviz.database.datamodel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a graph stored in a relational database
 *
 * @author David
 * @version 1.0
 */
@Entity
public class StoredGraph {

    @Id
    private Long id;
    private String pathGraphFile;
    private String imageLink;
    private String name;

    /**
     * Default constructor for the StoredGraph.
     */
    public StoredGraph() {

    }

    /**
     * Constructor for the StoredGraph.
     *
     * @param id the id of the stored graph as {@link Long}
     * @param pathGraphFile the path to the graph file as {@link String}
     * @param imageLink the path to the image file as {@link String}
     * @param name the name of the stored graph as {@link String}
     */
    public StoredGraph(Long id, String pathGraphFile, String imageLink, String name) {
        this.id = id;
        this.pathGraphFile = pathGraphFile;
        this.imageLink = imageLink;
        this.name = name;
    }

    /**
     * Getter for the id.
     *
     * @return the id of the stored graph as {@link Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the id.
     *
     * @param id the id of the stored graph as {@link Long}
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the path to the graph file.
     *
     * @return the path to the graph file as {@link String}
     */
    public String getPathGraphFile() {
        return pathGraphFile;
    }

    /**
     * Setter for the path to the graph file.
     *
     * @param path the path to the graph file as {@link String}
     */
    public void setPathGraphFile(String path) {
        this.pathGraphFile = path;
    }

    /**
     * Getter for the name.
     *
     * @return the name of the stored graph as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name.
     *
     * @param name the name of the stored graph as {@link String}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the path to the image file.
     *
     * @return the path to the image file as {@link String}
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * Setter for the path to the image file.
     *
     * @param pathGraphImage the path to the image file as {@link String}
     */
    public void setImageLink(String pathGraphImage) {
        this.imageLink = pathGraphImage;
    }
}