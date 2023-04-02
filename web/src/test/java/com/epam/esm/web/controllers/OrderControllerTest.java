package com.epam.esm.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Order;
import com.epam.esm.persistance.entity.OrderItem;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.persistance.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.payload.request.OrderCreateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import security.ControllerIntegrationTestConfig;
import security.WithCustomJwtToken;

@WebMvcTest(controllers = OrderController.class)
@ContextConfiguration(classes = {ControllerIntegrationTestConfig.class, OrderController.class})
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderService orderService;

  private User getUser(UUID uuid) {
    return new User(uuid, Instant.now());
  }

  private GiftCertificate getGiftCertificate() {
    return new GiftCertificate(4L, "second", "Second grade", BigDecimal.valueOf(40), 2,
        Instant.now(), Instant.now(), getTags());
  }

  private List<Tag> getTags() {
    return List.of(new Tag(1L, "basic"), new Tag(2L, "advanced"));
  }

  private Order getOrder(User owner) {
    Order order = new Order(1L, owner, Set.of(), Instant.now(), owner.getId().toString(),
        Instant.now());
    order.setItems(Set.of(new OrderItem(1L, order, getGiftCertificate(), BigDecimal.TEN)));
    return order;
  }

  @Test
  @WithCustomJwtToken
  void createOrderShouldCreateOrderWhenRequestValid() throws Exception {
    UUID uuid = UUID.fromString("e80113ae-bfc8-4673-befd-732197da81cd");
    User owner = getUser(uuid);
    Order order = getOrder(owner);
    OrderCreateRequest request = new OrderCreateRequest(List.of("second"));

    when(orderService.create(request, uuid)).thenReturn(order);

    // @formatter:off
    mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJson(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/orders/" + order.getId()))
        .andExpect(jsonPath("$.id", is(1L), Long.class));
    // @formatter:on

    verify(orderService, times(1)).create(request, uuid);
  }

  @Test
  @WithCustomJwtToken
  void orderByIdShouldReturnOrderWhenByExistingId() throws Exception {
    UUID uuid = UUID.fromString("e80113ae-bfc8-4673-befd-732197da81cd");
    User owner = getUser(uuid);
    Order order = getOrder(owner);

    when(orderService.getById(1L, uuid)).thenReturn(order);

    // @formatter:off
    mockMvc.perform(get("/orders/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", is(1L), Long.class));
    // @formatter:on

    verify(orderService, times(1)).getById(1L, uuid);
  }

  private String asJson(Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }
}