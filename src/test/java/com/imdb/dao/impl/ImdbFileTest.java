package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;
import com.imdb.dao.ImdbFile;
import com.imdb.dao.impl.beta.ImdbFileWithPattern;
import com.imdb.dao.impl.beta.ImdbFileWithStatAndEnd;
import com.imdb.dao.mapper.ImdbMapper;
import com.imdb.exceptions.FileHandlerException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class ImdbFileTest {
    private final File tsvFile = new File("data/temnp.tsv");
    private int YEAR = 2037;
    private final String ID ="t10203";
    private final String IDMid ="t10204";

    @Before
    public void setUp() throws Exception {
        try (FileOutputStream writer = FileUtils.openOutputStream(tsvFile)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(writer));
            bw.write("tconst\tyear\n");
            String line;
            for (int i = 1; i <= 10; i++) {
                bw.write(String.format("%s\t20%s\n",ID,i));
            }
            for (int i = 11; i <= 20; i++) {
                bw.write(String.format("%s\t20%s\n",IDMid,i));
            }
            for (int i = 1; i < 100; i++) {
                if (i < 50) {
                    generateLine(bw, i, "2016");
                } else if (i > 75) {
                    generateLine(bw, i, "2018");
                } else {
                    generateLine(bw, i, YEAR + "");
                }
            }

            bw.close();
        }
    }

    private void generateLine(BufferedWriter bw, int i, String s) throws IOException {
        bw.write(String.format("%s\t%s\n", i, s));
    }

    @After
    public void tearDown() throws Exception {
        //tsvFile.delete();
    }

    class ImdbTestEntity implements ImdbBaseEntity {
        private String id;
        private String year;

        public ImdbTestEntity(String id, String year) {
            this.id = id;
            this.year = year;
        }

        @Override
        public Optional<String> getYear() {
            return Optional.of(year + "");
        }
    }

    class TestMapper implements ImdbMapper<ImdbTestEntity> {

        @Override
        public ImdbTestEntity map(Map<String, String> readMap) {
            return new ImdbTestEntity(
                    readMap.get("tconst"),
                    readMap.get("year")
            );
        }
    }


    @Test
    public void getAllLinesWithStartAndEnd() throws IOException {
        ImdbFile file = getImdbFile(50, 75);
        List<ImdbBaseEntity> results = file.getAllLines().collect(Collectors.toList());
        assertEquals(26, results.size());
        assertEquals(30, Integer.parseInt(((ImdbTestEntity) results.get(0)).id));
        assertEquals(55, Integer.parseInt(((ImdbTestEntity) results.get(results.size() - 1)).id));

    }

    @Test(expected = NoSuchElementException.class)
    public void getAllLinesWithLargeEnd() throws IOException {
        ImdbFile file = getImdbFile(91, 120);
        List<ImdbBaseEntity> results = file.getAllLines().collect(Collectors.toList());
    }

    @Test(expected = NoSuchElementException.class)
    public void getAllLinesWithLargeStart() throws IOException {
        ImdbFile file = getImdbFile(120, 123);
        List<ImdbBaseEntity> results = file.getAllLines().collect(Collectors.toList());
    }

    @Test(expected = FileHandlerException.class)
    public void getAllLinesWithInvalidStartAndENd() throws IOException {
        ImdbFile file = getImdbFile(20, 10);
        List<ImdbBaseEntity> results = file.getAllLines().collect(Collectors.toList());

    }

    private ImdbFile getImdbFile(int start, int end) throws IOException {
        ImdbMapper mapper = new TestMapper();
        return new ImdbFileWithStatAndEnd(new InputStreamReader(FileUtils.openInputStream(tsvFile)), mapper, start, end);

    }

    @Test
    public void readEntireFile() throws IOException {
        ImdbMapper mapper = new TestMapper();
        ImdbFile file = new ImdbFileImpl(new InputStreamReader(FileUtils.openInputStream(tsvFile)), mapper);
        List<ImdbBaseEntity> result = file.getAllLines().collect(Collectors.toList());
        assertEquals(119, result.size());

    }

    @Test
    public void readEntireFileUsingWhile() throws IOException {
        ImdbMapper mapper = new TestMapper();
        ImdbFile file = new ImdbFileImpl(new InputStreamReader(FileUtils.openInputStream(tsvFile)), mapper);
        Iterator<ImdbBaseEntity> itr = file.getAllLines().iterator();
        int count=0;
        ImdbBaseEntity entity;
        while (itr.hasNext()) {
            itr.next();
            count ++;
        }
        assertEquals(119, count);

    }

    @Test
    public void getAllLinesWithYear() throws IOException {
        ImdbMapper mapper = new TestMapper();
        ImdbFile file = new ImdbFileWithPattern(tsvFile, mapper, YEAR+"", 1);
        List<ImdbBaseEntity> results = file.getAllLines().collect(Collectors.toList());
        assertEquals(26, results.size());
        assertEquals(50, Integer.parseInt(((ImdbTestEntity) results.get(0)).id));
        assertEquals(75, Integer.parseInt(((ImdbTestEntity) results.get(results.size() - 1)).id));

    }

    @Test
    public void getAllLinesWithId() throws IOException {
        ImdbMapper mapper = new TestMapper();
        ImdbFile file = new ImdbFileWithPattern(tsvFile, mapper, ID, 0);
        List<ImdbBaseEntity> results = file.getAllLines().collect(Collectors.toList());
        assertEquals(10, results.size());
    }

    @Test
    public void getAllLinesWithStartAndEndId() throws IOException {
        ImdbMapper mapper = new TestMapper();
        ImdbFile file = new ImdbFileWithPattern(tsvFile, mapper,
                Pair.of(0, ID),Pair.of(0,"10"));

        List<ImdbBaseEntity> results = file.getAllLines().collect(Collectors.toList());
        assertEquals(30, results.size());
        assertEquals(10, results.stream().filter(p -> ((ImdbTestEntity) p).id.equals(ID)).count());
        assertEquals(10, Integer.parseInt(((ImdbTestEntity) results.get(results.size() - 1)).id));
    }

    @Test
    public void getAllLinesWithStartAndEndIdInTheMiddle() throws IOException {
        ImdbMapper mapper = new TestMapper();
        ImdbFile file = new ImdbFileWithPattern(tsvFile, mapper,
                Pair.of(0, IDMid),Pair.of(0,"10"));

        List<ImdbBaseEntity> results = file.getAllLines().collect(Collectors.toList());
        assertEquals(20, results.size());
        assertEquals(10, results.stream().filter(p -> ((ImdbTestEntity) p).id.equals(IDMid)).count());
        assertEquals(10, Integer.parseInt(((ImdbTestEntity) results.get(results.size() - 1)).id));
    }
}