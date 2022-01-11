package com.example.social_network_gui_v2.repository.paging;

import java.util.stream.StreamSupport;

public class Paginator<E> {

    private Pageable pageable;
    private Iterable<E> elements;

    public Paginator(Pageable pageable, Iterable<E> elements) {
        this.pageable = pageable;
        this.elements = elements;
    }

    public Pages<E> paginate() {
        return new PageImplementation<>(pageable, StreamSupport.stream(elements.spliterator(), false));
    }
}
