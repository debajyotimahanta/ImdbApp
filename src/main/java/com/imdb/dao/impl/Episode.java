package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;
import com.imdb.util.ImdbRecordUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Optional;

@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.ToString
@Entity
@Table(name = "episodes")
public class Episode implements ImdbBaseEntity {

    /**
     * alphanumeric identifier of episode
     */
    @Id
    private String tconst;

    /**
     * alphanumeric identifier of the parent TV Series
     */
    private String parentTconst;


    /**
     * season number the episode belongs to
     */
    private Integer seasonNumber;

    /**
     * episode number of the tconst in the TV series
     */
    private Integer episodeNumber;

    @Override
    public Optional<String> getYear() {
        return Optional.empty();
    }
}
