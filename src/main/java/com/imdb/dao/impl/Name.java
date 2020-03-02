package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@lombok.Builder(toBuilder = true)
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.ToString
@Entity
@Table(name = "names")
public class Name implements ImdbBaseEntity {
    /**
     * alphanumeric unique identifier of the name/person
     */
    @Id
    private String nconst;

    /**
     * name by which the person is most often credited
     */
    private String primaryName;

    private String birthYear;

    /**
     * in YYYY format if applicable, else '\N'
     */
    private String deathYear;

    /**
     * the top-3 professions of the person
     */
    //TODO make this array and enum based
    private String primaryProfession;

    /**
     * titles the person is known for
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable
            (
                    name = "name_title",
                    joinColumns = {@JoinColumn(name = "nconst", referencedColumnName = "nconst")},
                    inverseJoinColumns = {@JoinColumn(name = "tconst", referencedColumnName = "tconst")}
            )
    private Set<Title> knownForTitles;

    @Override
    public Optional<String> getYear() {
        return Optional.empty();
    }
}
