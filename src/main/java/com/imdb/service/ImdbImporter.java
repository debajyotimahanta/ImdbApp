package com.imdb.service;

import com.imdb.dao.ImdbEntityDAO;
import com.imdb.dao.ImdbReader;
import com.imdb.dao.impl.Episode;
import com.imdb.dao.impl.Rating;
import com.imdb.dao.impl.Title;
import com.imdb.dao.mapper.*;
import com.imdb.exceptions.RetryException;
import com.imdb.queue.WorkItem;
import com.imdb.queue.WorkQueue;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class us used to async import {@link com.imdb.dao.ImdbFile}.
 * The actual task of import is handled async by {@link com.imdb.workers.ImdbEntityWorker}
 * which pools the {@link WorkQueue}.
 */
public class ImdbImporter {
    public static final String IMDB_TITLE_URL = "https://datasets.imdbws.com/title.basics.tsv.gz";
    public static final String IMDB_EPISODE_URL = "https://datasets.imdbws.com/title.episode.tsv.gz";
    public static final String IMDB_RATING_URL = "https://datasets.imdbws.com/title.ratings.tsv.gz";
    public static final String IMDB_NAME_URL = "https://datasets.imdbws.com/name.basics.tsv.gz";
    public static final String IMDB_PRINCIPAL_URL = "https://datasets.imdbws.com/title.principals.tsv.gz";
    public static final String IMDB_CREW_URL = "https://datasets.imdbws.com/title.crew.tsv.gz";
    //TODO Add actors data also
    private static Log log = LogFactory.getLog(ImdbImporter.class);


    private ImdbReader imdbReader;
    private WorkQueue importerQueue;


    public ImdbImporter(ImdbReader imdbReader, WorkQueue importerQueue) {

        this.imdbReader = imdbReader;
        this.importerQueue = importerQueue;
    }

    public void importAll() throws IOException, CsvValidationException {
        QueueImportUrlFor(IMDB_TITLE_URL, new TitleMapper());
        QueueImportUrlFor(IMDB_NAME_URL, new NameMapper());
        QueueImportUrlFor(IMDB_PRINCIPAL_URL, new PrincipalMapper());
        QueueImportUrlFor(IMDB_EPISODE_URL, new EpisodeMapper());
        QueueImportUrlFor(IMDB_RATING_URL, new RatingMapper());
        QueueImportUrlFor(IMDB_CREW_URL, new CrewMapper());
    }

    private void QueueImportUrlFor(String url, ImdbMapper titleMapper) throws IOException, CsvValidationException {

        importerQueue.put(new WorkItem(new Runnable() {
            @Override
            public void run() {

                try {
                    imdbReader.readUrl(url, titleMapper);
                } catch (Exception e) {
                    log.error(e);
                    throw new RetryException(e);
                }
            }
        }));
    }
}


