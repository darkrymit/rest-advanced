package com.epam.esm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.esm.persistance.dao.TagRepository;
import com.epam.esm.persistance.dao.support.page.PageImpl;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.persistance.entity.Tag;
import com.epam.esm.service.payload.request.TagCreateRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer1;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

  @Mock
  TagRepository tagRepository;

  @InjectMocks
  TagServiceImpl tagService;

  private List<Tag> getTags() {
    return List.of(new Tag(1L, "basic"), new Tag(2L, "advanced"));
  }

  private Tag getTag() {
    return new Tag(1L, "basic");
  }

  private static Answer1<Tag, Tag> getFakeSave(Long id) {
    return tag -> {
      tag.setId(id);
      return tag;
    };
  }

  @Test
  void findAllShouldReturnNotEmptyContentWhenEntriesExists() {
    List<Tag> preparedTags = getTags();
    Pageable pageable = Pageable.unpaged();

    when(tagRepository.findAll(pageable)).thenReturn(new PageImpl<>(preparedTags,pageable,preparedTags.size()));

    assertFalse(tagService.findAll(pageable).getContent().isEmpty());
  }

  @Test
  void getByIdShouldReturnNonNullWhenByExistingId() {
    Tag preparedTag = getTag();

    when(tagRepository.findById(preparedTag.getId())).thenReturn(Optional.of(preparedTag));

    assertNotNull(tagService.getById(1L));
  }

  @Test
  void deleteByIdShouldDeleteMetaWhenByExistingId() {
    Tag preparedTag = getTag();

    when(tagRepository.findById(preparedTag.getId())).thenReturn(Optional.of(preparedTag));
    doNothing().when(tagRepository).delete(preparedTag);

    tagService.deleteById(preparedTag.getId());

    verify(tagRepository, times(1)).delete(preparedTag);
  }

  @Test
  void createShouldReturnTagWithNameMatchingToRequestWhenNormalRequest() {
    Tag preparedTag = getTag();
    TagCreateRequest tagCreateRequest = new TagCreateRequest(preparedTag.getName());

    when(tagRepository.save(argThat(tag -> tag.getId() == null))).thenAnswer(answer(getFakeSave(preparedTag.getId())));

    assertEquals(tagCreateRequest.getName(),tagService.create(tagCreateRequest).getName());
  }
}