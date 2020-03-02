package imdb;

import com.imdb.dao.ImdbEntityDAO;
import com.imdb.dao.ImdbFile;
import com.imdb.dao.ImdbReader;
import com.imdb.dao.impl.Episode;
import com.imdb.dao.impl.ImdbFileImpl;
import com.imdb.dao.impl.Rating;
import com.imdb.dao.impl.Title;
import com.imdb.dao.mapper.EpisodeMapper;
import com.imdb.dao.mapper.ImdbMapper;
import com.imdb.dao.mapper.RatingMapper;
import com.imdb.dao.mapper.TitleMapper;
import com.imdb.queue.WorkQueue;
import com.imdb.service.ImdbImporter;
import com.imdb.util.WebClient;
import com.opencsv.exceptions.CsvValidationException;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ImdbImporterTest {

    private File titleFile = new File("data/title.basics.tsv");
    private File episodesFile = new File("data/episode.basics.tsv");
    private File ratingsFile = new File("data/rating.basics.tsv");
    private ImdbReader imdbReader;
    private String id1 = "tt0000001";
    private String id2 = "tt0000002";
    private String id3 = "tt0000003";
    private String id4 = "tt0000004";
    Configuration configuration = new Configuration().configure("config/test.hibernate.cfg.xml");
    @Autowired
    private ImdbEntityDAO dao;

    @PersistenceContext
    private EntityManager entityManager;


    @Before
    public void setUp() throws Exception {

        String[] titleData = new String[]{
                "tconst\ttitleType\tprimaryTitle\toriginalTitle\tisAdult\tstartYear\tendYear\truntimeMinutes\tgenres\n",
                "tt10044194\ttvEpisode\tWhat to say to \"May I Take Your Order?\tWhat to say to \"May I Take Your Order?\t0\t2019\t\\N\t\\N\tAdventure,Animation\n",
                "title" + id1 + "\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t1\tDocumentary,Short\n",
                "title" + id2 + "\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t1\tDocumentary,Short\n",
                "tt0701219\ttvEpisode\tSummer of 4'2\"\tSummer of 4'2\"\t0\t1996\t\\N\t30\tAnimation,Comedy",
        };
        String[] episodesData = new String[]{"tconst\tparentTconst\tseasonNumber\tepisodeNumber\n",
                "episode" + id1 + "\ttt0041038\t1\t9\n",
                "episode" + id2 + "\ttt0989121\t1\t17\n",
                "episode" + id3 + "\ttt0989125\t1\t17"};

        String[] ratingsdata = new String[]{"tconst\taverageRating\tnumVotes\n",
                "title" + id1 + "\t5.6\t1587\n",
                "title" + id2 + "\t6.1\t192\n",
                "title" + id3 + "\t6.5\t1250\n",
                "title" + id4 + "\t6.2\t119\n"};


        WebClient webClient = mock(WebClient.class);
        mockImdbDownload(titleData, ImdbImporter.IMDB_TITLE_URL, webClient, titleFile, new TitleMapper());
        mockImdbDownload(episodesData, ImdbImporter.IMDB_EPISODE_URL, webClient, episodesFile, new EpisodeMapper());
        mockImdbDownload(ratingsdata, ImdbImporter.IMDB_RATING_URL, webClient, ratingsFile, new RatingMapper());
        imdbReader = new ImdbReader(webClient, dao);
/*

        WebClient w = new WebClientImpl(1000000, 1000000, true);

        imdbReader = new ImdbReader(w, dao);
        */


    }

    private WebClient mockImdbDownload(String[] sampleData,
                                       String url, WebClient webClient, File tsvFile, ImdbMapper mapper) throws IOException {
        try (FileWriter fw = new FileWriter(tsvFile)) {
            for (int i = 0; i < sampleData.length; i++) {
                fw.write(sampleData[i]);
            }

        }


        when(webClient.download(eq(url), isA(mapper.getClass())))
                .thenReturn(new ImdbFileImpl(
                        new InputStreamReader(new FileInputStream(tsvFile)),
                        mapper
                ));
        return webClient;
    }

    @After
    public void tearDown() throws Exception {
        titleFile.delete();
        episodesFile.delete();
        ratingsFile.delete();
    }


    @Test
    public void readUrl() throws IOException, CsvValidationException {

        FakeQueue importQueue = new FakeQueue();

        ImdbImporter importer = new ImdbImporter(imdbReader, importQueue);


        importer.importAll();
        assertEquals(3, importQueue.getQueue().size());
        processQueue(importQueue);


        assertNotNull(read("title" + id1, Title.class));
//        assertNotNull(read("title" + id2, Title.class));
        assertNull(read("title" + id3, Title.class));

        assertNotNull(read("episode" + id1, Episode.class));
        assertNotNull(read("episode" + id2, Episode.class));
        assertNotNull(read("episode" + id3, Episode.class));

        assertNotNull(read("title" + id1, Rating.class));
        assertNotNull(read("title" + id2, Rating.class));
        assertNotNull(read("title" + id3, Rating.class));
        assertNotNull(read("title" + id4, Rating.class));
    }

    private Object read(String key, Class classType) {
        return entityManager.find(classType, key);
    }

    private void processQueue(FakeQueue queue) {
        while (!queue.getQueue().isEmpty()) {
            queue.getQueue().poll().run();
        }
    }

}
