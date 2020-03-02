package com.imdb.util;

import com.imdb.dao.ImdbFile;
import com.imdb.dao.mapper.ImdbMapper;

import java.io.IOException;

public interface WebClient {
    ImdbFile download(String url, ImdbMapper mapper) throws IOException;
}
