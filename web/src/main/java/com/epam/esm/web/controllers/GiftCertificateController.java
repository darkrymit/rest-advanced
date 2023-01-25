package com.epam.esm.web.controllers;

import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.payload.request.GiftCertificateCreateRequest;
import com.epam.esm.service.payload.request.GiftCertificateSearchRequest;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import com.epam.esm.web.dto.GiftCertificateDTO;
import com.epam.esm.web.dto.assembler.GiftCertificateModelAssembler;
import com.epam.esm.web.dto.assembler.PagedResourcesAssembler;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/certificates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class GiftCertificateController {

  private final GiftCertificateService giftCertificateService;

  private final GiftCertificateModelAssembler giftCertificateModelAssembler;

  private final PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler;

  @GetMapping
  public PagedModel<GiftCertificateDTO> allGiftCertificates(
      @Valid GiftCertificateSearchRequest searchRequest, Pageable pageable) {
    log.debug("Resolved search request {}", searchRequest);
    return pagedResourcesAssembler.toModel(giftCertificateService.findAll(searchRequest, pageable),
        giftCertificateModelAssembler);
  }

  @PostMapping
  public ResponseEntity<GiftCertificateDTO> createGiftCertificate(
      @Valid @RequestBody GiftCertificateCreateRequest request) {
    GiftCertificateDTO giftCertificateDTO = giftCertificateModelAssembler.toModel(
        giftCertificateService.create(request));
    return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION,
            giftCertificateDTO.getLink("self").orElseThrow().toUri().toString())
        .body(giftCertificateDTO);
  }

  @GetMapping("/{id}")
  public GiftCertificateDTO giftCertificateById(@PathVariable Long id) {
    return giftCertificateModelAssembler.toModel(giftCertificateService.getById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteGiftCertificate(@PathVariable Long id) {
    giftCertificateService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<GiftCertificateDTO> updateGiftCertificate(@PathVariable Long id,
      @RequestBody GiftCertificateUpdateRequest request) {
    GiftCertificateDTO giftCertificateDTO = giftCertificateModelAssembler.toModel(
        giftCertificateService.update(id, request));
    return ResponseEntity.ok(giftCertificateDTO);
  }
}
