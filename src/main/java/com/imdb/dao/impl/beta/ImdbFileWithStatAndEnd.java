package com.imdb.dao.impl.beta;

import com.imdb.dao.ImdbBaseEntity;
import com.imdb.dao.ImdbFile;
import com.imdb.dao.mapper.ImdbMapper;
import com.imdb.exceptions.FileHandlerException;
import com.imdb.exceptions.ImdbReadLineException;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This file lets to specify scan based on a range defined by the start and end line
 * It useful if you dont want to read the whole file
 */
public class ImdbFileWithStatAndEnd implements ImdbFile {
    private static Log log = LogFactory.getLog(ImdbFileWithStatAndEnd.class);

    private final CSVReaderHeaderAware csvReader;
    private ImdbMapper mapper;
    private Integer startLine;
    private Integer endLine;

    public ImdbFileWithStatAndEnd(CSVReaderHeaderAware csvReader,
                                  ImdbMapper mapper, Integer startLine, Integer endLine) throws IOException {
        this.csvReader = csvReader;
        this.mapper = mapper;
        this.startLine = startLine;
        this.endLine = endLine;
        log.info(String.format("Will scan file from line %s to %s", startLine-1, endLine));
        csvReader.skip(startLine-1);
    }

    public ImdbFileWithStatAndEnd(Reader reader, ImdbMapper mapper, Integer startLine, Integer endLine) throws IOException {
        if (startLine > endLine) throw new FileHandlerException("Start date cannot be greater then end date");
        this.mapper = mapper;
        this.startLine = startLine;
        this.endLine = endLine;
        ICSVParser parser = new CSVParserBuilder()
                .withSeparator('\t')
                .withIgnoreQuotations(true)
                .build();
        this.csvReader = (CSVReaderHeaderAware) new CSVReaderHeaderAwareBuilder(reader)
                .withCSVParser(parser)
                .build();
        csvReader.skip(startLine - 1);
    }

    @Override
    public Stream<ImdbBaseEntity> getAllLines() {

        Iterator<Map<String, String>> imdbIterator = new Iterator<Map<String, String>>() {
            private Map<String, String> line = new HashMap<>();

            @Override
            public boolean hasNext() {
                return csvReader.getLinesRead() <= endLine && line != null;
            }

            @Override
            public Map<String, String> next() throws ImdbReadLineException {
                try {
                    line = csvReader.readMap();
                    if (line == null) throw new NoSuchElementException();
                    return line;
                } catch (CsvValidationException | IOException ex) {
                    log.error("Skipping record because of error", ex);
                    throw new ImdbReadLineException(ex);
                }
            }
        };
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(imdbIterator, Spliterator.ORDERED), false)
                .map(l -> convertToImdbEntity(l));

    }

    private ImdbBaseEntity convertToImdbEntity(Map<String, String> line) {
        if (line == null) return null;
        return mapper.map(line);

    }

    @Override
    public void close() throws Exception {
        csvReader.close();
    }
}
