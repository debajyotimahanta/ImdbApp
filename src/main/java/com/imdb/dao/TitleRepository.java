package com.imdb.dao;

import com.imdb.dao.impl.Title;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TitleRepository extends PagingAndSortingRepository<Title, String>, JpaSpecificationExecutor<Title> {
}
