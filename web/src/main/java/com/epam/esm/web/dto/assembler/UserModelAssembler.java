package com.epam.esm.web.dto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.service.UserInfo;
import com.epam.esm.web.controllers.UserController;
import com.epam.esm.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@RequiredArgsConstructor
public class UserModelAssembler implements RepresentationModelAssembler<UserInfo, UserDTO> {

  private final ModelMapper modelMapper;

  @Override
  public UserDTO toModel(UserInfo entity) {
    UserDTO userDTO = modelMapper.map(entity, UserDTO.class);
    userDTO.add(linkTo(methodOn(UserController.class).userById(entity.getId())).withSelfRel());
    userDTO.add(linkTo(
        methodOn(UserController.class).getOrders(entity.getId(), Pageable.unpaged())).withRel(
        "orders"));
    return userDTO;
  }

  @Override
  public CollectionModel<UserDTO> toCollectionModel(Iterable<? extends UserInfo> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }
}