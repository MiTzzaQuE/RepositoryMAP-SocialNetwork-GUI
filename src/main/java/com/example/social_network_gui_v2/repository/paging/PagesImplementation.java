package com.example.social_network_gui_v2.repository.paging;
import java.util.stream.Stream;

public class PagesImplementation<T> implements Pages<T> {
    private Pageable pageable;
    private Stream<T> content;

    PagesImplementation(Pageable pageable, Stream<T> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public Pageable nextPageable() {
        return new PageableImplementation(this.pageable.getPageNumber() + 1, this.pageable.getPageSize());
    }

    @Override
    public Stream<T> getContent() {
        return this.content;
    }
}
