package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;
import com.imdb.util.ImdbRecordUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

@lombok.Builder(toBuilder = true)
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.ToString
@Entity
@Table(name = "titles")
public class Title implements ImdbBaseEntity, Serializable {
    /**
     * alphanumeric unique identifier of the title
     */
    @Id
    private String tconst;


    /**
     * the type/format of the title (e.g. movie, short, tvseries, tvepisode, video, etc)
     */
    private String titleType;

    /**
     * primaryTitle (string) – the more popular title / the title used by the filmmakers on promotional
     * materials at the point of release
     */
    private String primaryTitle;

    /**
     * originalTitle (string) - original title, in the original language
     */
    private String originalTitle;

    /**
     * isAdult (boolean) - 0: non-adult title; 1: adult title
     */
    private boolean isAdult;

    /**
     * startYear (YYYY) – represents the release year of a title. In the case of TV Series, it is the series start year
     */
    private String startYear;

    /**
     * endYear (YYYY) – TV Series end year. ‘\N’ for all other title types
     */
    private String endYear;

    /**
     * runtimeMinutes – primary runtime of the title, in minutes
     */
    private Integer runtimeMinutes;

    /**
     * genres (string array) – includes up to three genres associated with the title
     */
    private String genres;

    @Override
    public Optional<String> getYear() {
        return Optional.ofNullable(startYear);
    }

    /**
     * director(s) of the given title
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable
            (
                    name = "director_name",
                    joinColumns = {@JoinColumn(name = "tconst", referencedColumnName = "tconst")},
                    inverseJoinColumns = {@JoinColumn(name = "nconst", referencedColumnName = "nconst")}
            )
    private Set<Name> directors;

    /**
     * writer(s) of the given title
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable
            (
                    name = "writer_name",
                    joinColumns = {@JoinColumn(name = "tconst", referencedColumnName = "tconst")},
                    inverseJoinColumns = {@JoinColumn(name = "nconst", referencedColumnName = "nconst")}
            )
    private Set<Name> writers;
}
