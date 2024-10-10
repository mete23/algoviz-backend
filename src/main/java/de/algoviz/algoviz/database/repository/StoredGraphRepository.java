package de.algoviz.algoviz.database.repository;

import de.algoviz.algoviz.database.datamodel.StoredGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Provides methods for the stored graphs to use the database
 *
 * @author David
 * @version 1.0
 */
public interface StoredGraphRepository extends JpaRepository<StoredGraph, Long> {

    @Query("SELECT g.id FROM StoredGraph g")
    List<Long> findAllIds();
}
