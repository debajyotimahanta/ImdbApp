package com.imdb.util.rating.strategy;

import com.imdb.dao.impl.Rating;

import java.util.List;

public interface RatingStregety {

    Double calculateSeasonRating(List<Rating> episodRatings);
}
