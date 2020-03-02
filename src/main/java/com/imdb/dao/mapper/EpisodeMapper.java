package com.imdb.dao.mapper;

import com.imdb.dao.impl.Episode;
import com.imdb.util.ImdbRecordUtil;

import java.util.Map;

public class EpisodeMapper implements ImdbMapper<Episode> {
    @Override
    public Episode map(Map<String, String> readMap) {
        Episode.EpisodeBuilder builder = Episode.builder()
                .tconst(readMap.get("tconst"))
                .parentTconst(readMap.get("parentTconst"));
        ImdbRecordUtil.paseInteger(readMap.get("episodeNumber")).ifPresent(i -> builder.episodeNumber(i));
        ImdbRecordUtil.paseInteger(readMap.get("seasonNumber")).ifPresent(i -> builder.seasonNumber(i));
        return builder.build();

    }
}
