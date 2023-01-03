package com.epam.esm.persistance.dao.impl.jdbc;

import static com.epam.esm.persistance.entity.Tag.CERTIFICATES_TAGS_KEYS_TABLE;
import static com.epam.esm.persistance.entity.Tag.TABLE_NAME;

import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.entity.Tag;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link TagRepository} to work with Jdbc
 *
 * @author Tamerlan Hurbanov
 * @see TagRepository
 * @see JdbcTemplate
 * @since 1.0
 */
@Repository
@RequiredArgsConstructor
public class TagJdbcRepository implements TagRepository {

  public static final RowMapper<Tag> ROW_MAPPER = new BeanPropertyRowMapper<>(Tag.class);
  private final JdbcTemplate jdbcTemplate;

  @Override
  public <S extends Tag> S save(S entity) {
    S result;
    if (entity.getId() == null) {
      result = onInsert(entity);
    } else {
      result = onUpdate(entity);
    }
    return result;
  }

  private <S extends Tag> S onUpdate(S entity) {
    List<Object> values = new ArrayList<>();
    values.add(entity.getName());
    values.add(entity.getId());
    jdbcTemplate.update(Query.UPDATE, values.toArray());
    return entity;
  }

  private <S extends Tag> S onInsert(S entity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement preparedStatement = connection.prepareStatement(Query.INSERT,
          Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, entity.getName());
      return preparedStatement;
    }, keyHolder);
    entity.setId(keyHolder.getKey().longValue());
    return entity;
  }

  @Override
  public Optional<Tag> findById(Long id) {
    return jdbcTemplate.query(Query.SELECT_BY_ID, ROW_MAPPER, id).stream().findFirst();
  }

  @Override
  public boolean existsById(Long id) {
    return Boolean.TRUE.equals(
        jdbcTemplate.query(Query.EXIST_BY_ID, pss -> pss.setLong(1, id), (rs -> {
          if (rs.next()) {
            return rs.getLong("count(*)") > 0;
          }
          throw new DataIntegrityViolationException(String.format("Can't exist for %s", Tag.class));
        })));
  }

  @Override
  public List<Tag> findAllAsList() {
    return jdbcTemplate.query(Query.SELECT_ALL, ROW_MAPPER);
  }

  @Override
  public void delete(Tag entity) {
    jdbcTemplate.update(Query.DELETE_CERTIFICATES_REFERENCE, entity.getId());
    jdbcTemplate.update(Query.DELETE_BY_ID, entity.getId());
  }

  @Override
  public Optional<Tag> findByName(String name) {
    return jdbcTemplate.query(Query.SELECT_BY_NAME, ROW_MAPPER, name).stream().findFirst();
  }

  public static class Query {

    public static final String INSERT = "INSERT INTO " + TABLE_NAME + "(`name`) VALUES (?)";
    public static final String UPDATE = "UPDATE " + TABLE_NAME + " SET `name` = ? WHERE id = ?";
    public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    public static final String EXIST_BY_ID = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE id = ?";
    public static final String SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    public static final String SELECT_BY_NAME = "SELECT * FROM " + TABLE_NAME + " WHERE `name` = ?";
    public static final String DELETE_BY_ID = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    public static final String DELETE_CERTIFICATES_REFERENCE =
        "DELETE FROM " + CERTIFICATES_TAGS_KEYS_TABLE + " WHERE tags_id = ?";

    private Query() {

    }
  }
}
