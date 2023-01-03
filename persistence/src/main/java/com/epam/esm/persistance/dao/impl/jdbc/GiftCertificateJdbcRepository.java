package com.epam.esm.persistance.dao.impl.jdbc;

import static com.epam.esm.persistance.entity.GiftCertificate.CERTIFICATES_TAGS_KEYS_TABLE;
import static com.epam.esm.persistance.entity.GiftCertificate.TABLE_NAME;

import com.epam.esm.persistance.dao.GiftCertificateRepository;
import com.epam.esm.persistance.dao.GiftCertificateSearchParameters;
import com.epam.esm.persistance.dao.Sort.Order;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Tag;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


/**
 * Implementation of {@link GiftCertificateRepository} to work with Jdbc
 *
 * @author Tamerlan Hurbanov
 * @see GiftCertificateRepository
 * @see JdbcTemplate
 * @since 1.0
 */
@Repository
@RequiredArgsConstructor
public class GiftCertificateJdbcRepository implements GiftCertificateRepository {


  public static final RowMapper<GiftCertificate> ROW_MAPPER = (rs, rowNum) -> new GiftCertificate(
      rs.getLong("id"), rs.getString("name"), rs.getString("description"),
      rs.getBigDecimal("price"), rs.getInt("duration"),
      rs.getTimestamp("create_date").toLocalDateTime().atZone(ZoneId.of("GMT+3")),
      rs.getTimestamp("last_update_date").toLocalDateTime().atZone(ZoneId.of("GMT+3")), null);
  private final JdbcTemplate jdbcTemplate;

  @Override
  public <S extends GiftCertificate> S save(S entity) {
    S result;
    if (entity.getId() == null) {
      result = onInsert(entity);
    } else {
      result = onUpdate(entity);
    }
    return result;
  }

  private <S extends GiftCertificate> S onUpdate(S entity) {
    List<Object> values = new ArrayList<>();
    values.add(entity.getName());
    values.add(entity.getDescription());
    values.add(entity.getPrice());
    values.add(entity.getDuration());
    values.add(Timestamp.from(ZonedDateTime.now().toInstant()));
    values.add(entity.getId());
    jdbcTemplate.update(Query.UPDATE, values.toArray());
    jdbcTemplate.update(Query.DELETE_TAGS_REFERENCE, entity.getId());
    List<Object[]> batchUpdateArguments = entity.getTags().stream().map(Tag::getId)
        .map(tagId -> new Object[]{tagId, entity.getId()}).collect(Collectors.toList());
    jdbcTemplate.batchUpdate(Query.INSERT_GIFT_CERTIFICATE_TO_TAG_REFERENCE, batchUpdateArguments);
    return entity;
  }

  private <S extends GiftCertificate> S onInsert(S entity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement preparedStatement = connection.prepareStatement(Query.INSERT,
          Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, entity.getName());
      preparedStatement.setString(2, entity.getDescription());
      preparedStatement.setBigDecimal(3, entity.getPrice());
      preparedStatement.setInt(4, entity.getDuration());
      preparedStatement.setTimestamp(5, Timestamp.from(entity.getCreateDate().toInstant()));
      preparedStatement.setTimestamp(6, Timestamp.from(entity.getLastUpdateDate().toInstant()));
      return preparedStatement;
    }, keyHolder);
    entity.setId(keyHolder.getKey().longValue());
    List<Object[]> batchUpdateArguments = entity.getTags().stream().map(Tag::getId)
        .map(tagId -> new Object[]{tagId, entity.getId()}).collect(Collectors.toList());
    jdbcTemplate.batchUpdate(Query.INSERT_GIFT_CERTIFICATE_TO_TAG_REFERENCE, batchUpdateArguments);
    return entity;
  }

  @Override
  public Optional<GiftCertificate> findById(Long id) {
    return jdbcTemplate.query(Query.SELECT_BY_ID, ROW_MAPPER, id).stream().findFirst()
        .map(giftCertificate -> {
          List<Tag> tags = jdbcTemplate.query(Query.SELECT_ALL_TAGS_FOR_GIFT_CERTIFICATE,
              TagJdbcRepository.ROW_MAPPER, id);
          giftCertificate.setTags(tags);
          return giftCertificate;
        });
  }

  @Override
  public boolean existsById(Long id) {
    return Boolean.TRUE.equals(
        jdbcTemplate.query(Query.EXIST_BY_ID, pss -> pss.setLong(1, id), (rs -> {
          if (rs.next()) {
            return rs.getLong("count(*)") > 0;
          }
          throw new DataIntegrityViolationException(String.format("Can't exist for %s", GiftCertificate.class));
        })));
  }

  @Override
  public List<GiftCertificate> findAllAsList() {
    List<GiftCertificate> giftCertificates = jdbcTemplate.query(Query.SELECT_ALL, ROW_MAPPER);
    loadTags(giftCertificates);
    return giftCertificates;
  }

  @Override
  public List<GiftCertificate> findAllAsList(GiftCertificateSearchParameters searchParameters) {
    String query = "SELECT " + TABLE_NAME + ".*" + " FROM " + TABLE_NAME + " ";
    List<String> joins = new ArrayList<>();
    List<String> sort = new ArrayList<>();
    List<String> where = new ArrayList<>();
    List<String> whereArguments = new ArrayList<>();
    List<Object> finalArguments = new ArrayList<>();

    addTagName(searchParameters, joins, where, whereArguments);
    addPart(searchParameters, where, whereArguments);
    addSort(searchParameters, sort);

    query = compose(query, joins, sort, where, whereArguments, finalArguments);

    List<GiftCertificate> giftCertificates = jdbcTemplate.query(query, ROW_MAPPER,
        finalArguments.toArray());
    loadTags(giftCertificates);
    return giftCertificates;
  }

  private String compose(String query, List<String> joins, List<String> sort, List<String> where,
      List<String> whereArguments, List<Object> finalArguments) {
    if (!joins.isEmpty()) {
      query += String.join(" ", joins) + " ";
    }
    if (!where.isEmpty()) {
      query += "WHERE " + String.join(" AND ", where);
      finalArguments.addAll(whereArguments);
    }
    if (!sort.isEmpty()) {
      query += "ORDER BY " + String.join(", ", sort);
    }
    return query;
  }

  private void addSort(GiftCertificateSearchParameters searchParameters, List<String> sort) {
    if (searchParameters.isSortPresent()) {
      for (Order order : searchParameters.getSort().getOrder()) {
        addOrder(sort, order);
      }
    }
  }

  private void addOrder(List<String> sort, Order order) {
    if (order.getProperty().equals("name")) {
      sort.add(TABLE_NAME + ".`name` " + (order.isAscending() ? "ASC" : "DESC"));
    }
    if (order.getProperty().equals("date")) {
      sort.add(TABLE_NAME + ".`create_date` " + (order.isAscending() ? "ASC" : "DESC"));
    }
  }

  private void addTagName(GiftCertificateSearchParameters searchParameters, List<String> joins,
      List<String> where, List<String> whereArguments) {
    if (searchParameters.isTagNamePresent()) {
      joins.add(String.format("LEFT JOIN %1$s on %1$s.certificates_id = %2$s.id",
          CERTIFICATES_TAGS_KEYS_TABLE, TABLE_NAME));
      joins.add(String.format("LEFT JOIN %1$s on %1$s.id = %2$s.tags_id", Tag.TABLE_NAME,
          CERTIFICATES_TAGS_KEYS_TABLE));
      where.add(Tag.TABLE_NAME + ".`name` = ?");
      whereArguments.add(searchParameters.getTagName());
    }
  }

  private void addPart(GiftCertificateSearchParameters searchParameters, List<String> where,
      List<String> whereArguments) {
    if (searchParameters.isPartPresent()) {
      where.add(String.format(
          " %1$s.`name` LIKE CONCAT( '%%',?,'%%')OR %1$s.description LIKE CONCAT( '%%',?,'%%') ",
          TABLE_NAME));
      whereArguments.add(searchParameters.getPart());
      whereArguments.add(searchParameters.getPart());
    }
  }

  private void loadTags(List<GiftCertificate> giftCertificates) {
    for (GiftCertificate giftCertificate : giftCertificates) {
      List<Tag> tags = jdbcTemplate.query(Query.SELECT_ALL_TAGS_FOR_GIFT_CERTIFICATE,
          TagJdbcRepository.ROW_MAPPER, giftCertificate.getId());
      giftCertificate.setTags(tags);
    }
  }


  @Override
  public void delete(GiftCertificate entity) {
    jdbcTemplate.update(Query.DELETE_TAGS_REFERENCE, entity.getId());
    jdbcTemplate.update(Query.DELETE_BY_ID, entity.getId());
  }

  public static class Query {

    public static final String INSERT = "INSERT INTO " + TABLE_NAME
        + " (`name`, description, price, duration, create_date, last_update_date) VALUES (?,?,?,?,?,?)";

    public static final String UPDATE = "UPDATE " + TABLE_NAME
        + " SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? "
        + "WHERE id = ?";
    public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    public static final String EXIST_BY_ID = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE id = ?";
    public static final String SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    public static final String DELETE_BY_ID = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    public static final String DELETE_TAGS_REFERENCE =
        "DELETE FROM " + CERTIFICATES_TAGS_KEYS_TABLE + " WHERE certificates_id = ?";
    public static final String SELECT_ALL_TAGS_FOR_GIFT_CERTIFICATE =
        "SELECT t.* FROM " + Tag.TABLE_NAME + " as t " + "LEFT JOIN " + CERTIFICATES_TAGS_KEYS_TABLE
            + " as cht on cht.tags_id = t.id " + "WHERE cht.certificates_id = ?";
    public static final String INSERT_GIFT_CERTIFICATE_TO_TAG_REFERENCE =
        "INSERT INTO " + CERTIFICATES_TAGS_KEYS_TABLE + "(tags_id, certificates_id) VALUES (?,?)";

    private Query() {
    }
  }
}