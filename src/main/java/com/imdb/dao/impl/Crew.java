package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This is a POJO use to map CSV director values. We dont need an entity because
 * its stored in a join table defined in {@link Title}
 */
@lombok.Builder
@lombok.Getter
@lombok.ToString
public class Crew implements ImdbBaseEntity {
    private String tconst;

    private List<String> directors;

    private List<String> writers;

    @Override
    public Optional<String> getYear() {
        return Optional.empty();
    }
}
