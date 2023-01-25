package com.epam.esm.persistance.dao.support.page;

import com.epam.esm.persistance.dao.support.Sort;
import java.io.Serializable;

public class PageRequest implements Pageable, Serializable {
    private static final long serialVersionUID = -4541509938956089562L;
    private final Sort sort;
    private final int page;
    private final int size;

    protected PageRequest(int page, int size, Sort sort) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        } else if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        } else {
            this.page = page;
            this.size = size;
        }
        this.sort = sort;
    }

    public static PageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static PageRequest of(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    public static PageRequest ofSize(int pageSize) {
        return of(0, pageSize);
    }

    public Sort getSort() {
        return this.sort;
    }

    public PageRequest next() {
        return new PageRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    public PageRequest previous() {
        return this.getPageNumber() == 0 ? this : new PageRequest(this.getPageNumber() - 1, this.getPageSize(), this.getSort());
    }

    public PageRequest first() {
        return new PageRequest(0, this.getPageSize(), this.getSort());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof PageRequest)) {
            return false;
        } else {
            PageRequest that = (PageRequest)obj;
            boolean result;
            if (this.getClass() == ((Object) that).getClass()) {
                result = this.page == that.page && this.size == (that).size;
            } else {
                result = false;
            }
            return result && this.sort.equals(that.sort);
        }
    }

    public PageRequest withPage(int pageNumber) {
        return new PageRequest(pageNumber, this.getPageSize(), this.getSort());
    }

    public PageRequest withSort(Sort sort) {
        return new PageRequest(this.getPageNumber(), this.getPageSize(), sort);
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + this.page;
        result = 31 * result + this.size;
        return 31 * result + this.sort.hashCode();
    }

    public String toString() {
        return String.format("Page request [number: %d, size %d, sort: %s]", this.getPageNumber(), this.getPageSize(), this.sort);
    }

    public int getPageSize() {
        return this.size;
    }

    public int getPageNumber() {
        return this.page;
    }

    public long getOffset() {
        return (long)this.page * (long)this.size;
    }

    public boolean hasPrevious() {
        return this.page > 0;
    }

    public Pageable previousOrFirst() {
        return this.hasPrevious() ? this.previous() : this.first();
    }
}