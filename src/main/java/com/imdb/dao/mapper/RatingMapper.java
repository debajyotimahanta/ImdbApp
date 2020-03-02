package com.imdb.dao.mapper;

import com.imdb.dao.impl.Rating;
import com.imdb.util.ImdbRecordUtil;

import java.util.Map;

public class RatingMapper implements ImdbMapper<Rating> {
    @Override
    public Rating map(Map<String, String> readMap) {
        Rating.RatingBuilder ratingBuilder = Rating.builder()
                .tconst(readMap.get("tconst"));
        ImdbRecordUtil.paseDouble(readMap.get("averageRating")).ifPresent(i -> ratingBuilder.averageRating(i));
        ImdbRecordUtil.paseInteger(readMap.get("numVotes")).ifPresent(i -> ratingBuilder.numVotes(i));
        return ratingBuilder.build();

    }
}
