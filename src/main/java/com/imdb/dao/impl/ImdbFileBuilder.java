package com.imdb.dao.impl;

import com.imdb.dao.mapper.ImdbMapper;

import java.io.Reader;

public class ImdbFileBuilder {
    private Reader reader;
    private ImdbMapper mapper;

    public ImdbFileBuilder reader(Reader reader) {
        this.reader = reader;
        return this;
    }

    public ImdbFileBuilder mapper(ImdbMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public ImdbFileImpl build() {
        return new ImdbFileImpl(reader, mapper);
    }
}