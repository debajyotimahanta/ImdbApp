package com.imdb.dao;

import com.imdb.dao.impl.Episode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EpisodeRepository extends PagingAndSortingRepository<Episode, String>, JpaSpecificationExecutor<Episode> {
}
