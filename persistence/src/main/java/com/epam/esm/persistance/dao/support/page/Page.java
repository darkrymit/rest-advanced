//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.epam.esm.persistance.dao.support.page;

import com.epam.esm.persistance.dao.support.Sort;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Page<T> extends Iterable<T> {

  static <T> Page<T> empty() {
    return empty(Pageable.unpaged());
  }

  static <T> Page<T> empty(Pageable pageable) {
    return new PageImpl<>(Collections.emptyList(), pageable, 0L);
  }

  int getTotalPages();

  long getTotalElements();

  Stream<T> stream();

  <U> Page<U> map(Function<? super T, ? extends U> converter);

  int getNumber();

  int getSize();

  int getNumberOfElements();

  List<T> getContent();

  boolean hasContent();

  Sort getSort();

  boolean isFirst();

  boolean isLast();

  boolean hasNext();

  boolean hasPrevious();

  default Pageable getPageable() {
    return PageRequest.of(this.getNumber(), this.getSize(), this.getSort());
  }

  Pageable nextPageable();

  Pageable previousPageable();

  default Pageable nextOrLastPageable() {
    return this.hasNext() ? this.nextPageable() : this.getPageable();
  }

  default Pageable previousOrFirstPageable() {
    return this.hasPrevious() ? this.previousPageable() : this.getPageable();
  }
}
