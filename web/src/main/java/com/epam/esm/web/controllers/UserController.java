package com.epam.esm.web.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.Order;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import com.epam.esm.web.dto.BestTagDTO;
import com.epam.esm.web.dto.OrderDTO;
import com.epam.esm.web.dto.UserDTO;
import com.epam.esm.web.dto.assembler.OrderModelAssembler;
import com.epam.esm.web.dto.assembler.PagedResourcesAssembler;
import com.epam.esm.web.dto.assembler.UserModelAssembler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  private final TagService tagService;

  private final ModelMapper modelMapper;

  private final PagedResourcesAssembler<User> userPagedResourcesAssembler;

  private final PagedResourcesAssembler<Order> orderPagedResourcesAssembler;

  @GetMapping
  public PagedModel<UserDTO> allUsers(Pageable pageable) {
    return userPagedResourcesAssembler.toModel(userService.findAll(pageable), userModelAssembler);
  }

  @GetMapping("/{id}")
  public UserDTO userById(@PathVariable Long id) {
    return userModelAssembler.toModel(userService.getById(id));
  }

  @GetMapping("/{id}/orders")
  public PagedModel<OrderDTO> getOrders(@PathVariable Long id, Pageable pageable) {
    return orderPagedResourcesAssembler.toModel(orderService.getAllByOwnerId(id, pageable),
        orderModelAssembler);
  }

  @GetMapping("/me")
  public UserDTO me(
      @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
    return userModelAssembler.toModel(userService.getByEmail(user.getUsername()));
  }

  @GetMapping("/best-buyer/most-used-tag")
  public BestTagDTO getMostUsedTagForBestBuyer() {
    BestTagDTO bestTagDTO = modelMapper.map(tagService.getMostUsedTagForBestBuyer(),
        BestTagDTO.class);
    bestTagDTO.add(
        linkTo(methodOn(UserController.class).getMostUsedTagForBestBuyer()).withSelfRel());
    return bestTagDTO;
  }

}
