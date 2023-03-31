package com.epam.esm.web.dto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.web.controllers.TagController;
import com.epam.esm.web.dto.TagDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@RequiredArgsConstructor
public class TagModelAssembler implements RepresentationModelAssembler<Tag, TagDTO> {

  private final ModelMapper modelMapper;

  @Override
  public TagDTO toModel(Tag entity) {
    TagDTO tagDTO = modelMapper.map(entity, TagDTO.class);
    Link selfLink = linkTo(methodOn(TagController.class).tagById(entity.getId())).withSelfRel();
    tagDTO.add(selfLink);
    return tagDTO;
  }

  @Override
  public CollectionModel<TagDTO> toCollectionModel(Iterable<? extends Tag> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }
}