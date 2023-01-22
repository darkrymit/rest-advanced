package com.epam.esm.web.controllers;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.web.dto.OrderDTO;
import com.epam.esm.web.dto.UserDTO;
import com.epam.esm.web.dto.assembler.OrderModelAssembler;
import com.epam.esm.web.dto.assembler.UserModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class UserController {

  private final UserModelAssembler userModelAssembler;

  private final UserService userService;

  private final OrderModelAssembler orderModelAssembler;

  private final OrderService orderService;

  @GetMapping
  public CollectionModel<UserDTO> allUsers() {
    return userModelAssembler.toCollectionModel(userService.findAll());
  }

  @GetMapping("/{id}")
  public UserDTO userById(@PathVariable Long id) {
    return userModelAssembler.toModel(userService.getById(id));
  }

  @GetMapping("/{id}/orders")
  public CollectionModel<OrderDTO> getOrders(@PathVariable Long id) {
    return orderModelAssembler.toCollectionModel(orderService.getAllByOwnerId(id));
  }

  @GetMapping("/me")
  public UserDTO me(@AuthenticationPrincipal User user) {
    return userModelAssembler.toModel(userService.getByEmail(user.getUsername()));
  }

}
