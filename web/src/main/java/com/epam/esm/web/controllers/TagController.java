package com.epam.esm.web.controllers;

import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.payload.request.TagCreateRequest;
import com.epam.esm.web.dto.TagDTO;
import com.epam.esm.web.dto.Tags;
import com.epam.esm.web.dto.assembler.TagModelAssembler;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/tags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  private final TagModelAssembler tagModelAssembler;

  @GetMapping
  public Tags allTags() {
    List<Tag> tags = tagService.findAll();
    return new Tags(tags, tags.size());
  }

  @PostMapping
  public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagCreateRequest request) {
    TagDTO tagDTO = tagModelAssembler.toModel(tagService.create(request));
    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.LOCATION, tagDTO.getLink("self").orElseThrow().toUri().toString())
        .body(tagDTO);
  }

  @GetMapping("/{id}")
  public TagDTO tagById(@PathVariable Long id) {
    return tagModelAssembler.toModel(tagService.getById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteTag(@PathVariable Long id) {
    tagService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
