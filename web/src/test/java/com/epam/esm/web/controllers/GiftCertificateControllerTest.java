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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exceptions.NoSuchGiftCertificateException;
import com.epam.esm.service.payload.request.GiftCertificateCreateRequest;
import com.epam.esm.service.payload.request.GiftCertificatePriceUpdateRequest;
import com.epam.esm.service.payload.request.GiftCertificateSearchRequest;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import com.epam.esm.web.exceptions.handler.GiftCertificateControllerAdvice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(controllers = GiftCertificateController.class)
@ContextConfiguration(classes = {ControllerIntegrationTestConfig.class,
    GiftCertificateController.class, GiftCertificateControllerAdvice.class})
class GiftCertificateControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private GiftCertificateService giftCertificateService;

  @Test
  @WithCustomJwtToken
  void allGiftCertificatesShouldReturnPageWhenEntriesExists() throws Exception {

    GiftCertificateSearchRequest request = new GiftCertificateSearchRequest(List.of("test"), null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<GiftCertificate> giftCertificates = new PageImpl<>(List.of(
        new GiftCertificate(1L, "certificate", "test certificate", BigDecimal.TEN, 10,
            Instant.now(), Instant.now(), List.of(new Tag(1L, "test")))));

    when(giftCertificateService.findAll(request, pageable)).thenReturn(giftCertificates);

    mockMvc.perform(get("/certificates").contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("tags", request.getTags().toArray(String[]::new))
            .queryParam("page", String.valueOf(pageable.getPageNumber()))
            .queryParam("size", String.valueOf(pageable.getPageSize()))).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$._embedded.giftCertificates", hasSize(1)));

    verify(giftCertificateService, times(1)).findAll(request, pageable);
  }

  @Test
  @WithCustomJwtToken
  void giftCertificateByIdShouldReturnGiftCertificatesWhenByExistingId() throws Exception {
    GiftCertificate giftCertificate = new GiftCertificate(1L, "certificate", "test certificate",
        BigDecimal.TEN, 10, Instant.now(), Instant.now(), List.of(new Tag(1L, "test")));

    when(giftCertificateService.getById(1L)).thenReturn(giftCertificate);

    mockMvc.perform(get("/certificates/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.name", is("certificate")));

    verify(giftCertificateService, times(1)).getById(1L);
  }
  @Test
  @WithCustomJwtToken
  void giftCertificateByIdShouldReturnNotFoundWhenByNonExistingId() throws Exception {
    when(giftCertificateService.getById(1L)).thenThrow(new NoSuchGiftCertificateException(1L));

    mockMvc.perform(get("/certificates/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errorMessage", not(blankOrNullString())));
  }

  @Test
  @WithCustomJwtToken
  void createGiftCertificateShouldCreateGiftCertificateWhenRequestValid() throws Exception {
    GiftCertificate certificate = new GiftCertificate(1L, "certificate", "test certificate",
        BigDecimal.TEN, 10, Instant.now(), Instant.now(), List.of(new Tag(1L, "test")));
    GiftCertificateCreateRequest request = new GiftCertificateCreateRequest(certificate.getName(),
        certificate.getDescription(), certificate.getPrice(), certificate.getDuration(),
        certificate.getTags().stream().map(Tag::getName).collect(Collectors.toList()));

    when(giftCertificateService.create(request)).thenReturn(certificate);

    mockMvc.perform(post("/certificates").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJson(request))).andExpect(status().isCreated()).andExpect(
            header().string(HttpHeaders.LOCATION,
                "http://localhost/certificates/" + certificate.getId()))
        .andExpect(jsonPath("$.name", is(certificate.getName())));

    verify(giftCertificateService, times(1)).create(request);
  }

  @Test
  @WithCustomJwtToken
  void updateGiftCertificate() throws Exception {
    GiftCertificate certificate = new GiftCertificate(1L, "certificate", "update test certificate",
        BigDecimal.ONE, 10, Instant.now(), Instant.now(), List.of(new Tag(1L, "test")));

    GiftCertificateUpdateRequest request = new GiftCertificateUpdateRequest(null,
        "update test certificate", BigDecimal.ONE, null, null);

    when(giftCertificateService.update(1L, request)).thenReturn(certificate);

    mockMvc.perform(patch("/certificates/1").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJson(request))).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.price", is(BigDecimal.ONE),BigDecimal.class));

    verify(giftCertificateService, times(1)).update(1L,request);
  }

  @Test
  @WithCustomJwtToken
  void updateGiftCertificatePrice() throws Exception {
    GiftCertificate certificate = new GiftCertificate(1L, "certificate", "update test certificate",
        BigDecimal.ONE, 10, Instant.now(), Instant.now(), List.of(new Tag(1L, "test")));

    GiftCertificatePriceUpdateRequest request = new GiftCertificatePriceUpdateRequest(BigDecimal.ONE);

    when(giftCertificateService.update(1L, request)).thenReturn(certificate);

    mockMvc.perform(patch("/certificates/1/price").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJson(request))).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.price", is(BigDecimal.ONE),BigDecimal.class));

    verify(giftCertificateService, times(1)).update(1L,request);
  }

  @Test
  @WithCustomJwtToken
  void deleteGiftCertificateShouldDeleteGiftCertificateWhenByExistingId() throws Exception {
    mockMvc.perform(delete("/certificates/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNoContent());

    verify(giftCertificateService, times(1)).deleteById(1L);
    verifyNoMoreInteractions(giftCertificateService);
  }

  @Test
  @WithCustomJwtToken
  void deleteGiftCertificateShouldReturnForbiddenWhenNoAccess() throws Exception {
    doThrow(new AccessDeniedException("test access denied")).when(giftCertificateService)
        .deleteById(1L);

    mockMvc.perform(delete("/certificates/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errorMessage", startsWith("Access Denied")));
  }

  private String asJson(Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }
}