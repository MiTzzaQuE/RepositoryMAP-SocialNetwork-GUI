package com.example.social_network_gui_v2.repository.paging;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Paginator<E> {
    private Pageable pageable;
    private Iterable<E> elements;

    public Paginator(Pageable pageable, Iterable<E> elements) {
        this.pageable = pageable;
        this.elements = elements;
    }

    public Pages<E> paginate() {
        Stream<E> result = StreamSupport.stream(elements.spliterator(), false)
                .skip(pageable.getPageNumber()  * pageable.getPageSize())
                .limit(pageable.getPageSize());
        return new PagesImplementation<>(pageable, result);
    }
}
