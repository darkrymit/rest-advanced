package com.epam.esm.web.dto;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@Relation(collectionRelation = "users", itemRelation = "user")
public class UserDTO extends RepresentationModel<TagDTO> {

  private Long id;

  private String email;

  private String firstName;

  private String lastName;

  private Instant creationDate;

  private String lastModifiedBy;

  private Instant lastModifiedDate;
}
