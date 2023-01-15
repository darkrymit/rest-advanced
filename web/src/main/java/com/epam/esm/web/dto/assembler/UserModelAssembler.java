package com.epam.esm.web.dto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.persistance.entity.User;
import com.epam.esm.web.controllers.UserController;
import com.epam.esm.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModelAssembler implements RepresentationModelAssembler<User, UserDTO> {

  private final ModelMapper modelMapper;

  @Override
  public UserDTO toModel(User entity) {
    UserDTO userDTO = modelMapper.map(entity, UserDTO.class);
    Link selfLink = linkTo(methodOn(UserController.class).userById(entity.getId())).withSelfRel();
    userDTO.add(selfLink);
    return userDTO;
  }

  @Override
  public CollectionModel<UserDTO> toCollectionModel(Iterable<? extends User> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }
}