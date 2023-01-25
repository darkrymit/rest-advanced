package com.epam.esm.persistance.dao.support.page;

import com.epam.esm.persistance.dao.support.Sort;
import java.io.Serializable;
import java.util.Optional;

public interface Pageable extends Serializable {
    static Pageable unpaged() {
        return Unpaged.INSTANCE;
    }

    static Pageable ofSize(int pageSize) {
        return PageRequest.of(0, pageSize);
    }

    default boolean isPaged() {
        return true;
    }

    default boolean isUnpaged() {
        return !this.isPaged();
    }

    int getPageNumber();

    int getPageSize();

    long getOffset();

    Sort getSort();

    default Sort getSortOr(Sort sort) {
        return this.getSort().isSorted() ? this.getSort() : sort;
    }

    Pageable next();

    Pageable previousOrFirst();

    Pageable first();

    Pageable withPage(int pageNumber);

    boolean hasPrevious();

    default Optional<Pageable> toOptional() {
        return this.isUnpaged() ? Optional.empty() : Optional.of(this);
    }
}
