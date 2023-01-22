package com.epam.esm.web.controllers;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.payload.request.OrderCreateRequest;
import com.epam.esm.web.dto.OrderDTO;
import com.epam.esm.web.dto.assembler.OrderModelAssembler;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class OrderController {

  private final OrderModelAssembler orderModelAssembler;

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderCreateRequest request,
      @AuthenticationPrincipal User user) {
    OrderDTO orderDTO = orderModelAssembler.toModel(
        orderService.create(request, user.getUsername()));
    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.LOCATION, orderDTO.getLink("self").orElseThrow().toUri().toString())
        .body(orderDTO);
  }

  @GetMapping("/{id}")
  public OrderDTO orderById(@PathVariable Long id) {
    return orderModelAssembler.toModel(orderService.getById(id));
  }

}
