package com.imdb.dao;

import com.imdb.dao.mapper.CrewMapper;
import com.imdb.dao.mapper.ImdbMapper;
import com.imdb.dao.mapper.NameMapper;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public interface ImdbEntityDAO {
    void createFromFile(ImdbFile file, ImdbMapper mapper) throws IOException, CsvValidationException;

    void createNameFromFile(ImdbFile file, NameMapper mapper) throws IOException, CsvValidationException;
    void createCrewFromFile(ImdbFile file, CrewMapper mapper) throws IOException, CsvValidationException;
}
