package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;
import com.imdb.util.ImdbRecordUtil;

import javax.persistence.*;
import java.util.Optional;

@lombok.Builder(toBuilder=true)
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.ToString
@Entity
@Table(name = "ratings")
public class Rating implements ImdbBaseEntity {

    /**
     * alphanumeric unique identifier of the title
     */
    @Id
    private String tconst;

    /**
     * weighted average of all the individual user ratings
     */

    private Double averageRating;
    /**
     * number of votes the title has received
     */
    private Integer numVotes;

    @OneToOne
    @JoinColumn(name = "tconst", referencedColumnName = "tconst")
    private Episode episode;

    @Override
    public Optional<String> getYear() {
        return Optional.empty();
    }
}
