package com.imdb.dao.mapper;

import com.imdb.dao.impl.Title;
import com.imdb.util.ImdbRecordUtil;

import java.util.Map;

public class TitleMapper implements ImdbMapper<Title> {
    @Override
    public Title map(Map<String, String> readMap) {
        Title.TitleBuilder builder = Title.builder()
                .tconst(readMap.get("tconst"))
                .titleType(readMap.get("titleType"))
                .primaryTitle(readMap.get("primaryTitle"))
                .originalTitle(readMap.get("originalTitle"))
                .isAdult("1".equals(readMap.get("isAdult")))
                .startYear(readMap.get("startYear"))
                .genres(readMap.get("genres"))
                .endYear(readMap.get("endYear"));

        ImdbRecordUtil.paseInteger(readMap.get("runtimeMinutes")).ifPresent(i -> builder.runtimeMinutes(i));
        return builder.build();
    }
}
