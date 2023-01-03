package com.epam.esm.web.controllers;

import com.epam.esm.persistance.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.payload.request.GiftCertificateCreateRequest;
import com.epam.esm.service.payload.request.GiftCertificateSearchRequest;
import com.epam.esm.service.payload.request.GiftCertificateUpdateRequest;
import com.epam.esm.web.dto.GiftCertificates;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/certificates", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class GiftCertificateController {

  private final GiftCertificateService giftCertificateService;

  @Autowired
  public GiftCertificateController(GiftCertificateService giftCertificateService) {
    this.giftCertificateService = giftCertificateService;
  }


  @GetMapping
  public GiftCertificates allGiftCertificates(@Valid GiftCertificateSearchRequest searchRequest) {
    log.debug("Resolved search request {}",searchRequest);
    List<GiftCertificate> giftCertificates = giftCertificateService.findAll(searchRequest);
    return new GiftCertificates(giftCertificates,giftCertificates.size());
  }

  @PostMapping
  public ResponseEntity<GiftCertificate> createGiftCertificate(@Valid @RequestBody GiftCertificateCreateRequest request) {
    GiftCertificate giftCertificate = giftCertificateService.create(request);
    String location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(giftCertificate.getId())
        .toUriString();
    return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).body(giftCertificate);
  }

  @GetMapping("/{id}")
  public GiftCertificate giftCertificateById(@PathVariable Long id){
    return giftCertificateService.getById(id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteGiftCertificate(@PathVariable Long id) {
    giftCertificateService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<GiftCertificate> updateGiftCertificate(@PathVariable Long id,
      @RequestBody GiftCertificateUpdateRequest request) {
    GiftCertificate giftCertificate = giftCertificateService.update(id, request);
    return ResponseEntity.ok(giftCertificate);
  }
}
