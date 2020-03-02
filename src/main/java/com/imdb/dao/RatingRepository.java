package com.imdb.dao;

import com.imdb.dao.impl.Rating;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends PagingAndSortingRepository<Rating, String>,
        JpaSpecificationExecutor<Rating>, RatingRepositoryCustom {

    @Query("select r from Rating r join r.episode e where e.parentTconst= :id and e.seasonNumber=:num")
    List<Rating> findRatingsForSeriesAndSeasson(@Param("id")String tvSeriesId,
                                                @Param("num")int seassonNumber);

}
