package com.epam.esm.persistance.dao.support.page;

import com.epam.esm.persistance.dao.support.Sort;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageImpl<T> implements Page<T>, Serializable {
    private static final long serialVersionUID = 867755909294344406L;
    private final long total;
    private final List<T> content = new ArrayList<>();
    private final Pageable pageable;

    public PageImpl(Collection<T> content, Pageable pageable, long total) {
        this.content.addAll(content);
        this.pageable = pageable;
        this.total = pageable.toOptional()
                .filter(it -> !content.isEmpty())
                .filter(it -> it.getOffset() + it.getPageSize() > total)
                .map(it -> it.getOffset() + content.size())
                .orElse(total);
    }

    public PageImpl(Collection<T> content) {
        this(content, Pageable.unpaged(), null == content ? 0L : (long) content.size());
    }

    @Override
    public int getTotalPages() {
        return this.getSize() == 0 ? 1 : (int) Math.ceil((double) this.total / (double) this.getSize());
    }

    @Override
    public long getTotalElements() {
        return this.total;
    }

    @Override
    public Stream<T> stream() {
        return content.stream();
    }

    @Override
    public boolean hasNext() {
        return this.getNumber() + 1 < this.getTotalPages();
    }

    @Override
    public boolean isLast() {
        return !this.hasNext();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new PageImpl<>(this.getConvertedContent(converter), this.getPageable(), this.total);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageImpl<?> page = (PageImpl<?>) o;

        if (total != page.total) return false;
        if (!content.equals(page.content)) return false;
        return Objects.equals(pageable, page.pageable);
    }

    @Override
    public int hashCode() {
        int result = (int) (total ^ (total >>> 32));
        result = 31 * result + content.hashCode();
        result = 31 * result + (pageable != null ? pageable.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PageImpl{" + "total=" + total + ", content=" + content + ", pageable=" + pageable + '}';
    }

    @Override
    public int getNumber() {
        return this.pageable.isPaged() ? this.pageable.getPageNumber() : 0;
    }

    @Override
    public int getSize() {
        return this.pageable.isPaged() ? this.pageable.getPageSize() : this.content.size();
    }

    @Override
    public int getNumberOfElements() {
        return this.content.size();
    }

    @Override
    public boolean hasPrevious() {
        return this.getNumber() > 0;
    }

    @Override
    public boolean isFirst() {
        return !this.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return this.hasNext() ? this.pageable.next() : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return this.hasPrevious() ? this.pageable.previousOrFirst() : Pageable.unpaged();
    }

    @Override
    public boolean hasContent() {
        return !this.content.isEmpty();
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(this.content);
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public Sort getSort() {
        return this.pageable.getSort();
    }

    @Override
    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        return this.stream().map(converter).collect(Collectors.toList());
    }
}
