package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;
import com.imdb.dao.ImdbFile;
import com.imdb.exceptions.ImdbReadLineException;
import com.imdb.dao.mapper.ImdbMapper;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ImdbFileImpl implements ImdbFile {
    private static Log log = LogFactory.getLog(ImdbFileImpl.class);

    private final Reader reader;
    private final CSVReaderHeaderAware csvReader;
    private ImdbMapper mapper;

    public ImdbFileImpl(Reader reader, ImdbMapper mapper) {

        this.reader = reader;
        this.mapper = mapper;
        ICSVParser parser = new CSVParserBuilder()
                .withSeparator('\t')
                .withIgnoreQuotations(true)
                .build();
        this.csvReader = (CSVReaderHeaderAware) new CSVReaderHeaderAwareBuilder(reader)
                .withCSVParser(parser)
                .build();
    }

    @Override
    public Stream<ImdbBaseEntity> getAllLines() {

        Iterator<Map<String, String>> imdbIterator = new Iterator<Map<String, String>>() {
            private Map<String, String> line;

            @Override
            public boolean hasNext() {
                try {
                    return csvReader.peek() != null;
                } catch (IOException ex) {
                    throw new ImdbReadLineException(ex);
                }
            }

            @Override
            public Map<String, String> next() throws ImdbReadLineException {
                try {
                    return csvReader.readMap();
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
        reader.close();
    }
}
