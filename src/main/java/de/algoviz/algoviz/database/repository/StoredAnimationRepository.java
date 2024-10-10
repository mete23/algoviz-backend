package de.algoviz.algoviz.database.repository;

import de.algoviz.algoviz.database.datamodel.StoredAnimation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides methods for the stored animation to use the database
 *
 * @author David
 * @version 1.0
 */
public interface StoredAnimationRepository extends JpaRepository<StoredAnimation, Long> {
}
