package com.example.social_network_gui_v2.repository.paging;
import java.util.stream.Stream;

public interface Pages<E> {

    Pageable getPageable();
    Pageable nextPageable();
    Stream<E> getContent();
}
