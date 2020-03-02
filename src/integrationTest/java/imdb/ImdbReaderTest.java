package imdb;

import com.imdb.dao.ImdbEntityDAO;
import com.imdb.dao.ImdbBaseEntity;
import com.imdb.dao.ImdbFile;
import com.imdb.dao.ImdbReader;
import com.imdb.dao.impl.ImdbFileImpl;
import com.imdb.dao.mapper.ImdbMapper;
import com.imdb.util.WebClient;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ImdbReaderTest {
    private File tsvFile = new File("data/title.basics.tsv");
    private ImdbReader imdbReader;
    private ImdbEntityDAO dao;
    private ImdbMapper mapper;

    @Before
    public void setUp() throws Exception {
        String[] sampleData = new String[]{
                "tconst\ttitleType\tprimaryTitle\toriginalTitle\tisAdult\tstartYear\tendYear\truntimeMinutes\tgenres\n",
                "tt0000001\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t1\tDocumentary,Short\n",
                "tt0000002\tshort\tLe clown et ses chiens\tLe clown et ses chiens\t0\t1892\t\\N\t5\tAnimation,Short"
        };
        try (FileWriter fw = new FileWriter(tsvFile)) {
            for (int i = 0; i < sampleData.length; i++) {
                fw.write(sampleData[i]);
            }

        }
        WebClient webClient = mock(WebClient.class);
        mapper = mock(ImdbMapper.class);
        when(mapper.map(any())).thenReturn(mock(ImdbBaseEntity.class));

        when(webClient.download(any(), any()))
                .thenReturn(
                        new ImdbFileImpl(
                                new InputStreamReader(new FileInputStream(tsvFile)),
                                mapper
                        )
                );
        dao = mock(ImdbEntityDAO.class);
        imdbReader = new ImdbReader(webClient, dao);
    }

    @After
    public void tearDown() throws Exception {
        tsvFile.delete();
    }

    @Test
    public void readUrl() throws IOException, CsvValidationException {


        ArgumentCaptor<HashMap> map = ArgumentCaptor.forClass(HashMap.class);

        imdbReader.readUrl("test", mapper);
        verify(dao, times(1)).createFromFile(any(), any());
    }

}
