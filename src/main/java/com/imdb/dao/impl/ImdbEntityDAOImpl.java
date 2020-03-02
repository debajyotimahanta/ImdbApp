package com.imdb.dao.impl;

import com.imdb.dao.ImdbBaseEntity;
import com.imdb.dao.ImdbEntityDAO;
import com.imdb.dao.ImdbFile;
import com.imdb.dao.mapper.CrewMapper;
import com.imdb.dao.mapper.ImdbMapper;
import com.imdb.dao.mapper.NameMapper;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class ImdbEntityDAOImpl implements ImdbEntityDAO {
    private static Log log = LogFactory.getLog(ImdbEntityDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void createFromFile(ImdbFile file, ImdbMapper mapper) {
        Iterator<ImdbBaseEntity> itr = file.getAllLines()
                //TODO WE can apply this filter by since the data is no sorted it doesnt add much value
                //.filter(onlyMoveFromYear("2017"))
                .iterator();

        int i = 0;
        ImdbBaseEntity entity;
        while (itr.hasNext()) {
            entity = itr.next();
            if (entity != null) entityManager.persist(entity);

            i++;
            if (i % 30 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            //if (i == 10000) break;
        }
        log.info("Successfully imported " + file);

    }


    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void createNameFromFile(ImdbFile file, NameMapper mapper) {
        Iterator<ImdbBaseEntity> itr = file.getAllLines()
                .iterator();

        int i = 0;
        Name entity;
        while (itr.hasNext()) {
            try {
                try {
                    entity = (Name) itr.next();
                } catch (NoSuchElementException ex) {
                    log.info("Reached the end");
                    break;
                }
                Name.NameBuilder builder = entity.toBuilder();
                Set<Title> titles = entity.getKnownForTitles().stream()
                        .map(t -> entityManager.find(Title.class, t.getTconst()))
                        .collect(Collectors.toSet());
                builder.knownForTitles(titles);

                entityManager.persist(entity);


                i++;
                if (i % 30 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
                //if (i == 10000) break;
            } catch (Exception ex) {
                throw ex;
            }

        }

        log.info("Successfully imported " + file);

    }

    @Override
    public void createCrewFromFile(ImdbFile file, CrewMapper mapper) throws IOException, CsvValidationException {
        Iterator<ImdbBaseEntity> itr = file.getAllLines()
                .iterator();

        int i = 0;
        Crew entity;
        while ((entity = (Crew) itr.next()) != null) {
            try {

                Title.TitleBuilder builder = entityManager.find(Title.class, entity.getTconst()).toBuilder();

                builder.directors(
                        entity.getDirectors()
                                .stream().map(d -> entityManager.find(Name.class, d))
                                .collect(Collectors.toSet())
                );

                builder.writers(
                        entity.getWriters()
                                .stream().map(d -> entityManager.find(Name.class, d))
                                .collect(Collectors.toSet())
                );

                entityManager.persist(builder.build());


                i++;
                if (i % 30 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
                if (i == 10000) break;
            } catch (Exception ex) {
                throw ex;
            }

        }

        log.info("Successfully imported " + file);
    }


    private Predicate<ImdbBaseEntity> onlyMoveFromYear(String year) {
        return e -> {
            if (e == null) return false;
            if (e.getYear().isPresent()) {
                return e.getYear().equals(year);
            } else {
                return true;
            }
        };
    }

}
