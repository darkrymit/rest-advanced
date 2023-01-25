package com.epam.esm.web.dto.assembler;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import com.epam.esm.persistance.dao.support.page.Page;
import com.epam.esm.persistance.dao.support.page.PageRequest;
import com.epam.esm.persistance.dao.support.page.Pageable;
import com.epam.esm.web.resolver.PageableHandlerMethodArgumentResolver;
import com.epam.esm.web.resolver.SortHandlerMethodArgumentResolver;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class PagedResourcesAssembler<T> implements
    RepresentationModelAssembler<Page<T>, PagedModel<EntityModel<T>>> {

  private final PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  private boolean forceFirstAndLastRels = false;

  public PagedResourcesAssembler(PageableHandlerMethodArgumentResolver pageableArgumentResolver) {
    this.pageableArgumentResolver = pageableArgumentResolver;
  }

  private static String currentRequest() {
    return ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
  }

  public boolean isForceFirstAndLastRels() {
    return forceFirstAndLastRels;
  }

  public void setForceFirstAndLastRels(boolean forceFirstAndLastRels) {
    this.forceFirstAndLastRels = forceFirstAndLastRels;
  }

  @Override
  public PagedModel<EntityModel<T>> toModel(Page<T> entity) {
    return toModel(entity, EntityModel::of);
  }

  public <R extends RepresentationModel<?>> PagedModel<R> toModel(Page<T> page,
      RepresentationModelAssembler<T, R> assembler) {
    return createModel(page, assembler);
  }

  private <S, R extends RepresentationModel<?>> PagedModel<R> createModel(Page<S> page,
      RepresentationModelAssembler<S, R> assembler) {

    List<R> resources = new ArrayList<>(page.getNumberOfElements());

    for (S element : page) {
      resources.add(assembler.toModel(element));
    }

    PageMetadata metadata = asPageMetadata(page);
    PagedModel<R> resource = PagedModel.of(resources, metadata);

    return addPaginationLinks(resource, page, null);
  }

  private <R> PagedModel<R> addPaginationLinks(PagedModel<R> resources, Page<?> page, Link link) {

    UriTemplate base = getUriTemplate(link);

    boolean isNavigable = page.hasPrevious() || page.hasNext();

    if (isNavigable || forceFirstAndLastRels) {
      resources.add(createLink(base, PageRequest.of(0, page.getSize(), page.getSort()),
          IanaLinkRelations.FIRST));
    }

    if (page.hasPrevious()) {
      resources.add(createLink(base, page.previousPageable(), IanaLinkRelations.PREV));
    }

    Link selfLink = link != null ? link.withSelfRel()
        : createLink(base, page.getPageable(), IanaLinkRelations.SELF);

    resources.add(selfLink);

    if (page.hasNext()) {
      resources.add(createLink(base, page.nextPageable(), IanaLinkRelations.NEXT));
    }

    if (isNavigable || forceFirstAndLastRels) {

      int lastIndex = page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1;

      resources.add(createLink(base, PageRequest.of(lastIndex, page.getSize(), page.getSort()),
          IanaLinkRelations.LAST));
    }

    return resources;
  }

  private Link createLink(UriTemplate base, Pageable pageable, LinkRelation relation) {

    UriComponentsBuilder builder = fromUri(base.expand());

    enhance(builder, pageable);

    return Link.of(UriTemplate.of(builder.build().toString()), relation);
  }

  private void enhance(UriComponentsBuilder builder, Pageable pageable) {
    if (pageable.isUnpaged()) {
      return;
    }

    SortHandlerMethodArgumentResolver sortArgumentResolver = pageableArgumentResolver.getSortResolver();

    String pagePropertyName = pageableArgumentResolver.getPageParameterName();
    String sizePropertyName = pageableArgumentResolver.getSizeParameterName();
    String sortParameter = sortArgumentResolver.getSortParameter();

    int pageNumber = pageable.getPageNumber();

    builder.replaceQueryParam(pagePropertyName,
        pageableArgumentResolver.isOneIndexedParameters() ? pageNumber + 1 : pageNumber);

    builder.replaceQueryParam(sizePropertyName,
        Math.min(pageable.getPageSize(), pageableArgumentResolver.getMaxPageSize()));

    List<String> values = sortArgumentResolver.foldIntoExpressions(pageable.getSort());
    builder.replaceQueryParam(sortParameter, values);

  }

  private UriTemplate getUriTemplate(Link baseLink) {
    return UriTemplate.of(baseLink != null ? baseLink.getHref() : currentRequest());
  }

  private PageMetadata asPageMetadata(Page<?> page) {

    Assert.notNull(page, "Page must not be null");

    int number =
        pageableArgumentResolver.isOneIndexedParameters() ? page.getNumber() + 1 : page.getNumber();

    return new PageMetadata(page.getSize(), number, page.getTotalElements(), page.getTotalPages());
  }
}
