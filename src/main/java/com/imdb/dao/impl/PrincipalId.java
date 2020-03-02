package com.imdb.dao.impl;

import javax.persistence.Embeddable;
import java.io.Serializable;

@lombok.AllArgsConstructor
@Embeddable
public class PrincipalId implements Serializable {


    private String tconst;

    /**
     * a number to uniquely identify rows for a given titleId
     */
    private Integer ordering;
}