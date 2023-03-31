package com.epam.esm.web.dto.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.persistance.entity.Order;
import com.epam.esm.web.controllers.OrderController;
import com.epam.esm.web.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@RequiredArgsConstructor
public class OrderModelAssembler implements RepresentationModelAssembler<Order, OrderDTO> {

  private final ModelMapper modelMapper;

  @Override
  public OrderDTO toModel(Order entity) {
    OrderDTO orderDTO = modelMapper.map(entity, OrderDTO.class);
    Link selfLink = linkTo(methodOn(OrderController.class).orderById(entity.getId(),null)).withSelfRel();
    orderDTO.add(selfLink);
    return orderDTO;
  }

  @Override
  public CollectionModel<OrderDTO> toCollectionModel(Iterable<? extends Order> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }
}