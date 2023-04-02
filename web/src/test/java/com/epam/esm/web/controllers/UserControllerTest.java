package com.epam.esm.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserInfo;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import security.ControllerIntegrationTestConfig;
import security.WithCustomJwtToken;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {ControllerIntegrationTestConfig.class, UserController.class})
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TagService tagService;

  @MockBean
  private UserService userService;

  @MockBean
  private OrderService orderService;

  private UserInfo getUserInfo() {
    return new UserInfo(UUID.fromString("e80113ae-bfc8-4673-befd-732197da81cd"),
        "test1@gmail.com",
        "FirstTest",
        "LastTest",
        Instant.now());
  }

  @Test
  @WithCustomJwtToken
  void allUsersShouldReturnPageWhenEntriesExists() throws Exception {
    Pageable pageable = PageRequest.of(0, 10);
    Page<UserInfo> users = new PageImpl<>(Collections.singletonList(getUserInfo()));

    when(userService.findAll(pageable)).thenReturn(users);

    // @formatter:off
    mockMvc.perform(get("/users")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", String.valueOf(pageable.getPageNumber()))
            .queryParam("size", String.valueOf(pageable.getPageSize())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$._embedded.users", hasSize(1)));
    // @formatter:on

    verify(userService, times(1)).findAll(pageable);
  }

  @Test
  @WithCustomJwtToken
  void userByIdShouldReturnUserInfoWhenByExistingId() throws Exception {
    UserInfo userInfo = getUserInfo();

    when(userService.getById(userInfo.getId())).thenReturn(userInfo);

    // @formatter:off
    mockMvc.perform(get("/users/"+userInfo.getId().toString())
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.email", is(userInfo.getEmail())));
    // @formatter:on

    verify(userService, times(1)).getById(userInfo.getId());
  }

  @Test
  @WithCustomJwtToken
  void meShouldReturnUserInfoWhenByExistingIdFromAuthorization() throws Exception {
    UserInfo userInfo = getUserInfo();

    when(userService.getById(userInfo.getId())).thenReturn(userInfo);

    // @formatter:off
    mockMvc.perform(get("/users/me")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.email", is(userInfo.getEmail())));
    // @formatter:on

    verify(userService, times(1)).getById(userInfo.getId());
  }

  private String asJson(Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }
}