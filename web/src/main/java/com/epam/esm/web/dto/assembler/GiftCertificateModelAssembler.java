package com.epam.esm.web.dto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.web.controllers.GiftCertificateController;
import com.epam.esm.web.dto.GiftCertificateDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@RequiredArgsConstructor
public class GiftCertificateModelAssembler implements
    RepresentationModelAssembler<GiftCertificate, GiftCertificateDTO> {

  private final TagModelAssembler tagModelAssembler;

  private final ModelMapper modelMapper;

  @Override
  public GiftCertificateDTO toModel(GiftCertificate entity) {
    GiftCertificateDTO giftCertificateDTO = modelMapper.map(entity, GiftCertificateDTO.class);
    giftCertificateDTO.setTags(tagModelAssembler.toCollectionModel(entity.getTags()));
    Link selfLink = linkTo(methodOn(GiftCertificateController.class).giftCertificateById(
        entity.getId())).withSelfRel();
    giftCertificateDTO.add(selfLink);
    return giftCertificateDTO;
  }

  @Override
  public CollectionModel<GiftCertificateDTO> toCollectionModel(
      Iterable<? extends GiftCertificate> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }
}