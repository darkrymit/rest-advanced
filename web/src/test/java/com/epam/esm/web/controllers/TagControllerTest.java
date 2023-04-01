package com.epam.esm.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exceptions.NoSuchTagException;
import com.epam.esm.service.payload.request.TagCreateRequest;
import com.epam.esm.web.exceptions.handler.TagControllerAdvice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import security.ControllerIntegrationTestConfig;
import security.WithCustomJwtToken;

@WebMvcTest(controllers = TagController.class)
@ContextConfiguration(classes = {ControllerIntegrationTestConfig.class, TagController.class,
    TagControllerAdvice.class})
class TagControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TagService tagService;

  @Test
  @WithCustomJwtToken
  void allTagsShouldReturnPageWhenEntriesExists() throws Exception {

    Pageable pageable = PageRequest.of(0, 10);
    Page<Tag> tags = new PageImpl<>(Collections.singletonList(new Tag("tag")));

    when(tagService.findAll(pageable)).thenReturn(tags);

    mockMvc.perform(get("/tags").contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", String.valueOf(pageable.getPageNumber()))
            .queryParam("size", String.valueOf(pageable.getPageSize())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$._embedded.tags", hasSize(1)));

    verify(tagService, times(1)).findAll(pageable);
  }

  @Test
  @WithCustomJwtToken
  void tagByIdShouldReturnTagWhenByExistingId() throws Exception {
    Tag tag = new Tag("tag");

    when(tagService.getById(1L)).thenReturn(tag);

    mockMvc.perform(get("/tags/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.name", is("tag")));

    verify(tagService, times(1)).getById(1L);
  }

  @Test
  @WithCustomJwtToken
  void tagByIdShouldReturnNotFoundWhenByNonExistingId() throws Exception {

    when(tagService.getById(1L)).thenThrow(new NoSuchTagException(1L));

    mockMvc.perform(get("/tags/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errorMessage", not(blankOrNullString())));
  }

  @Test
  @WithCustomJwtToken
  void tagByIdShouldReturnInternalErrorWhenUnknownException() throws Exception {

    when(tagService.getById(1L)).thenThrow(new RuntimeException());

    mockMvc.perform(get("/tags/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errorMessage", startsWith("Unknown exception")));
  }

  @Test
  @WithCustomJwtToken
  void tagByIdShouldReturnInternalErrorWhenDataAccessException() throws Exception {

    when(tagService.getById(1L)).thenThrow(getDataAccessException());

    mockMvc.perform(get("/tags/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errorMessage", startsWith("Internal data")));
  }

  @Test
  @WithCustomJwtToken
  void createTagShouldCreateTagWhenRequestValid() throws Exception {

    String name = "test";
    TagCreateRequest request = new TagCreateRequest(name);
    Tag tag = new Tag(10L, name);

    when(tagService.create(request)).thenReturn(tag);

    mockMvc.perform(
            post("/tags").contentType(MediaType.APPLICATION_JSON_VALUE).content(asJson(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/tags/" + tag.getId()))
        .andExpect(jsonPath("$.name", is(name)));

    verify(tagService, times(1)).create(request);
  }

  @Test
  @WithCustomJwtToken
  void createTagShouldReturnBadRequestWhenRequestNotValid() throws Exception {
    String name = "tt";
    TagCreateRequest request = new TagCreateRequest(name);

    mockMvc.perform(
            post("/tags").contentType(MediaType.APPLICATION_JSON_VALUE).content(asJson(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errorMessage", startsWith("Invalid")));

  }

  @Test
  @WithCustomJwtToken
  void deleteTagShouldDeleteTagWhenByExistingId() throws Exception {
    mockMvc.perform(delete("/tags/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNoContent());

    verify(tagService, times(1)).deleteById(1L);
    verifyNoMoreInteractions(tagService);
  }

  @Test
  @WithCustomJwtToken
  void deleteTagShouldReturnForbiddenWhenNoAccess() throws Exception {
    doThrow(new AccessDeniedException("test access denied")).when(tagService).deleteById(1L);

    mockMvc.perform(delete("/tags/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errorMessage", startsWith("Access Denied")));
  }

  private static DataAccessException getDataAccessException() {
    return new DataAccessException("test exception"){};
  }

  private String asJson(Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }
}