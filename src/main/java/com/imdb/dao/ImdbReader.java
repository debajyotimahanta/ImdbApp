package com.imdb.dao;

import com.imdb.dao.mapper.CrewMapper;
import com.imdb.dao.mapper.ImdbMapper;
import com.imdb.dao.mapper.NameMapper;
import com.imdb.util.WebClient;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class ImdbReader {
    private static Log log = LogFactory.getLog(ImdbReader.class);

    private WebClient webClient;
    private ImdbEntityDAO dao;

    public ImdbReader(WebClient webClient, ImdbEntityDAO dao) {
        this.webClient = webClient;
        this.dao = dao;
    }

    public <T extends ImdbBaseEntity> void readUrl(String url, ImdbMapper<T> mapper) throws IOException, CsvValidationException {
        ImdbFile file = webClient.download(url, mapper);
        log.info(String.format("Downloaded %s", url));
        if (mapper instanceof NameMapper) {
            dao.createNameFromFile(file, (NameMapper) mapper);
        } else if (mapper instanceof CrewMapper) {
            dao.createCrewFromFile(file, (CrewMapper) mapper);
        } else {
            dao.createFromFile(file, mapper);
        }


    }


}
