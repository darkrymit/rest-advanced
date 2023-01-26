package com.epam.esm.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@Relation(collectionRelation = "tags", itemRelation = "tag")
public class TagDTO extends RepresentationModel<TagDTO> {
  private Long id;
  private String name;

}

