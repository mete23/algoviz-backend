package de.algoviz.algoviz.database.datamodel;

import de.algoviz.algoviz.external.graph.GraphExternal;
import de.algoviz.algoviz.model.algorithm.AlgorithmGenerator;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Entity for the stored animations.
 *
 * @author David
 * @version 1.0
 */
@Entity
public class StoredAnimation {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String pathGraphFile;
    private AlgorithmGenerator algorithmGenerator;

    /**
     * Constructor for the StoredAnimation.
     *
     * @param pathGraphFile      the path to the graph file of the animation as {@link String}
     * @param algorithmGenerator the algorithm generator of the animation as {@link AlgorithmGenerator}
     */
    public StoredAnimation(String pathGraphFile, AlgorithmGenerator algorithmGenerator) {
        this.pathGraphFile = pathGraphFile;
        this.algorithmGenerator = algorithmGenerator;
    }

    /**
     * Default constructor for the StoredAnimation.
     */
    public StoredAnimation() {
    }

    /**
     * Getter for the id.
     *
     * @return the id of the stored animation as {@link Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the id.
     *
     * @param id the id of the stored animation as {@link Long}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the path to the graph file.
     *
     * @return the path to the graph file of the animation as {@link String}
     */
    public String getPathGraphFile() {
        return pathGraphFile;
    }

    /**
     * Getter for the algorithm generator.
     *
     * @return the algorithm generator of the animation as {@link AlgorithmGenerator}
     */
    public AlgorithmGenerator getAlgorithmGenerator() {
        return algorithmGenerator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathGraphFile, algorithmGenerator);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StoredAnimation animation = (StoredAnimation) obj;
        return pathGraphFile.equals(animation.pathGraphFile)
                && algorithmGenerator.equals(animation.algorithmGenerator);
    }
}
