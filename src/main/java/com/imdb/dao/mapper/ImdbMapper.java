package com.imdb.dao.mapper;

import com.imdb.dao.ImdbBaseEntity;

import java.util.Map;
import java.util.regex.Pattern;

public interface ImdbMapper<T extends ImdbBaseEntity> {
    T map(Map<String, String> readMap);
}
