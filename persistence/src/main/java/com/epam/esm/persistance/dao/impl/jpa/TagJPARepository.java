package com.epam.esm.persistance.dao.impl.jpa;

import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.entity.Tag;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link TagRepository} to work with JPA
 *
 * @author Tamerlan Hurbanov
 * @see TagRepository
 * @see EntityManager
 * @since 1.0
 */
@Repository
public class TagJPARepository extends SimpleJpaRepository<Tag,Long> implements TagRepository {

  @Autowired
  public TagJPARepository() {
    super(Tag.class);
  }

  public TagJPARepository(EntityManager entityManager) {
    super(Tag.class, entityManager);
  }

  @Override
  protected <S extends Tag> Long getIdFromEntity(S entity) {
    return entity.getId();
  }

  @Override
  public Optional<Tag> findByName(String name) {
    return findOne((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
  }
}
