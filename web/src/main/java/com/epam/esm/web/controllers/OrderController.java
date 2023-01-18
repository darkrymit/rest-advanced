
package com.epam.esm.web.controllers;

import com.epam.esm.service.OrderService;
import com.epam.esm.web.dto.OrderDTO;
import com.epam.esm.web.dto.assembler.OrderModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class OrderController {

  private final OrderModelAssembler orderModelAssembler;

  private final OrderService orderService;

  @GetMapping("/{id}")
  public OrderDTO orderById(@PathVariable Long id) {
    return orderModelAssembler.toModel(orderService.getById(id));
  }

}
