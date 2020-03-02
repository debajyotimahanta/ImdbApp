package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.ToString
@Entity
@Table(name = "principals")
public class Principal implements ImdbBaseEntity {

    @EmbeddedId
    private PrincipalId id;

    @ManyToOne
    @JoinColumn(name = "tconst", insertable = false, updatable = false)
    private Title title;

    /**
     *
     */
    @OneToOne
    @JoinColumn( name = "nconst")
    private Name name;

    /**
     * the category of job that person was in
     */
    private String category;

    /**
     * the specific job title if applicable, else '\N'
     */
    private String job;

    /**
     *  the name of the character played if applicable, else '\N'
     */
    private String characters;

    @Override
    public Optional<String> getYear() {
        return Optional.empty();
    }


}


