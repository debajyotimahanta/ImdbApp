package com.imdb.dao.impl;

import com.imdb.dao.RatingRepository;
import com.imdb.dao.RatingRepositoryCustom;
import com.imdb.dao.impl.Episode;
import com.imdb.dao.impl.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class RatingRepositoryImpl implements RatingRepositoryCustom  {

    @Override
    public List<Rating> findRatingsForSeriesAndSeasson1(String tvSeriesId, int seassonNumber) {
        Specification<Rating> forSeriesWithSeason = new Specification<Rating>() {
            @Override
            public Predicate toPredicate(Root<Rating> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Episode, Rating> joinRating = root.join("episode", JoinType.INNER);
                return cb.and(
                        cb.equal(joinRating.<String>get("parentTconst"), tvSeriesId),
                        cb.equal(joinRating.<Integer>get("seasonNumber"), seassonNumber)
                );
            }
        };

        //return ratingRepository.findAll(forSeriesWithSeason);
        return null;


    }
}
