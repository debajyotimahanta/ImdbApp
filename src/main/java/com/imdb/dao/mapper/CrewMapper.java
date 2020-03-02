package com.imdb.dao.mapper;

import com.imdb.dao.impl.Crew;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CrewMapper implements ImdbMapper<Crew> {
    @Override
    public Crew map(Map<String, String> map) {
        Crew.CrewBuilder builder = Crew.builder()
                .tconst(map.get("tconst"));
        builder.directors(Arrays.stream(
                map.get("directors").split(","))
                .collect(Collectors.toList()));
        builder.writers(Arrays.stream(
                map.get("writers").split(","))
                .collect(Collectors.toList()));

        return builder.build();
    }
}
