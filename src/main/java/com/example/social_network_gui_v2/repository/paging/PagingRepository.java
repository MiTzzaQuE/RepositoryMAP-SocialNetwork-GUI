package com.example.social_network_gui_v2.repository.paging;

import com.example.social_network_gui_v2.domain.Entity;
import com.example.social_network_gui_v2.repository.Repository;

public interface PagingRepository<ID ,E extends Entity<ID>> extends Repository<ID, E> {

    Pages<E> findAll(Pageable pageable);   // Pageable e un fel de paginator
}
