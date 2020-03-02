package com.imdb.dao;

import java.util.stream.Stream;

public interface ImdbFile extends AutoCloseable {
    Stream<ImdbBaseEntity> getAllLines();
}
