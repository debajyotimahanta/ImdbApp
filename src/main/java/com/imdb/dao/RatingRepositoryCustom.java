package com.imdb.dao;

import com.imdb.dao.impl.Rating;

import java.util.List;

public interface RatingRepositoryCustom  {

    List<Rating> findRatingsForSeriesAndSeasson1(String tvSeriesId, int seassonNumber);
}
