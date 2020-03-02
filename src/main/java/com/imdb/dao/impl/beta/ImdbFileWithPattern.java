package com.imdb.dao.impl.beta;

import com.imdb.dao.ImdbBaseEntity;
import com.imdb.dao.ImdbFile;
import com.imdb.dao.mapper.ImdbMapper;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.ICSVParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class ImdbFileWithPattern implements ImdbFile {
    private final ImdbFileWithStatAndEnd fileReader;
    private final CSVReaderHeaderAware csvReader;


    public ImdbFileWithPattern(File file, ImdbMapper mapper,
                               Pair<Integer, String> startValue, Pair<Integer, String> endValue) throws IOException {

        this.csvReader = buildReader(file);
        Integer[] matches = getFirstAndLastMatch(file, startValue, endValue);
        fileReader = new ImdbFileWithStatAndEnd(csvReader, mapper, matches[0], matches[1]);
    }

    public ImdbFileWithPattern(File file, ImdbMapper mapper, String value, int column) throws IOException {
        this.csvReader = buildReader(file);
        Integer[] matches = getRangeMatch(file, Pair.of(column, value));
        fileReader = new ImdbFileWithStatAndEnd(csvReader, mapper, matches[0], matches[1]);
    }

    CSVReaderHeaderAware buildReader(File file) throws IOException {
        InputStreamReader reader = new InputStreamReader(FileUtils.openInputStream(file));
        ICSVParser parser = new CSVParserBuilder()
                .withSeparator('\t')
                .withIgnoreQuotations(true)
                .build();
        return (CSVReaderHeaderAware) new CSVReaderHeaderAwareBuilder(reader)
                .withCSVParser(parser)
                .build();
    }

    private Integer[] getFirstAndLastMatch(File file,
                                           Pair<Integer, String> startValue,
                                           Pair<Integer, String> endValue) throws IOException {
        LineIterator lineIterator = FileUtils.lineIterator(file);
        Integer[] matches = new Integer[]{0, 0};
        matches[0] = findFirstMatch(lineIterator, startValue.getRight(), 0, startValue.getLeft());
        int temp = findFirstMatch(lineIterator, endValue.getRight(), matches[0], endValue.getLeft());
        matches[1] = findLastMatch(lineIterator, startValue.getRight(), temp + 1, startValue.getLeft());
        return matches;
    }


    private Integer[] getRangeMatch(File file, Pair<Integer,
            String> startValue) throws IOException {
        LineIterator lineIterator = FileUtils.lineIterator(file);
        Integer[] matches = new Integer[]{0, 0};
        matches[0] = findFirstMatch(lineIterator, startValue.getRight(), 0, startValue.getLeft());
        matches[1] = findLastMatch(lineIterator, startValue.getRight(), matches[0], startValue.getLeft());
        return matches;

    }

    private int findFirstMatch(LineIterator lineIterator, String value, int index, int column) throws IOException {
        while (lineIterator.hasNext()) {
            index++;
            String line = lineIterator.next();
            String[] values = csvReader.getParser().parseLine(line);
            if (values[column].equals(value)) {
                index--;
                break;
            }
        }
        return index;
    }

    private int findLastMatch(LineIterator lineIterator, String value, int index, int column) throws IOException {
        while (lineIterator.hasNext()) {
            index++;
            String line = lineIterator.next();
            String[] values = csvReader.getParser().parseLine(line);
            if (!values[column].equals(value)) {
                return --index;
            }
        }
        return index;
    }


    @Override
    public Stream<ImdbBaseEntity> getAllLines() {
        return fileReader.getAllLines();
    }

    @Override
    public void close() throws Exception {
        fileReader.close();

    }
}
