package com.imdb.util.rating.strategy;

import com.imdb.dao.impl.Rating;

import java.util.List;

public class SumBasedRatingStrategy implements RatingStregety {
    @Override
    public Double calculateSeasonRating(List<Rating> episodRatings) {
        return episodRatings.stream()
                .mapToDouble(e -> e.getAverageRating())
                .sum();
    }
}
