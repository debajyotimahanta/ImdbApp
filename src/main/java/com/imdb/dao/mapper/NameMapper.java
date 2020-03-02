package com.imdb.dao.mapper;

import com.imdb.dao.impl.Name;
import com.imdb.dao.impl.Rating;
import com.imdb.dao.impl.Title;
import com.imdb.util.ImdbRecordUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NameMapper implements ImdbMapper<Name> {


    @Override
    public Name map(Map<String, String> readMap) {
        Name.NameBuilder builder = Name.builder()
                .nconst(readMap.get("nconst"))
                .birthYear(readMap.get("birthYear"))
                .primaryName(readMap.get("primaryName"));
        if(!ImdbRecordUtil.isNull(readMap.get("deathYear"))) builder.deathYear(readMap.get("deathYear"));
        Set<Title> titles = new HashSet<>();
        Arrays.stream(readMap.get("knownForTitles").split(",")).forEach(t -> {
            titles.add(Title.builder().tconst(t).build());
        });
        builder.knownForTitles(titles);
        return builder.build();
    }
}
