package com.imdb.server;

import com.imdb.dao.ImdbEntityDAO;
import com.imdb.dao.ImdbReader;
import com.imdb.dao.RatingRepository;
import com.imdb.queue.FakeQueue;
import com.imdb.service.ImdbImporter;
import com.imdb.service.RatingCalculator;
import com.imdb.util.WebClient;
import com.imdb.util.WebClientImpl;
import com.imdb.util.rating.strategy.SumBasedRatingStrategy;
import com.imdb.workers.ImdbEntityWorker;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.imdb.dao.impl", "com.imdb.dao"})
@EntityScan("com.imdb.dao.impl")
@ComponentScan("com.imdb.dao.impl")
public class ImdbJpaSampleRun {
    private static Log log = LogFactory.getLog(ImdbJpaSampleRun.class);

    public static void main(String[] args) {
        SpringApplication.run(ImdbJpaSampleRun.class, args);
    }

    @Bean
    public CommandLineRunner demo(ImdbEntityDAO dao,
                                  RatingRepository ratingRepository) {
        return (args) -> {

            log.info("Starting application");

            FakeQueue queue = new FakeQueue();
            ImdbEntityWorker importWorker = new ImdbEntityWorker(queue, 1);
            importWorker.start();
            RatingCalculator calculator = new RatingCalculator(
                    new SumBasedRatingStrategy(),
                    ratingRepository,
                    queue
            );
            //Not args send when run from IDE
            if(args == null || args.length ==0) {
                importImdb(dao, ratingRepository);
            } else {
                switch (args[0]) {
                    case "import":
                        importImdb(dao, ratingRepository);
                        break;
                    case "getRating":
                        calculator.calculateSeasonRating("tt10110314", 2);
                        break;
                    case "updateRating":
                        calculator.updateRating("tt10110314", 5);
                        break;
                }
            }


        };
    }

    private void updateRating() {

    }

    private void getRating() {
    }

    private void importImdb(ImdbEntityDAO dao, RatingRepository ratingRepository) throws IOException, CsvValidationException {

        WebClient w = new WebClientImpl(1000000, 1000000, true);

        ImdbReader imdbReader = new ImdbReader(w, dao);
        FakeQueue importQueue = new FakeQueue();
        ImdbEntityWorker importWorker = new ImdbEntityWorker(importQueue, 1);

        ImdbImporter importer = new ImdbImporter(imdbReader, importQueue);
        importer.importAll();
        importWorker.start();
    }
}
