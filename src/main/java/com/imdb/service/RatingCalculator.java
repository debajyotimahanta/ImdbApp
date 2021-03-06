package com.imdb.service;

import com.imdb.dao.RatingRepository;
import com.imdb.dao.impl.Rating;
import com.imdb.exceptions.ItemNotFoundException;
import com.imdb.queue.WorkItem;
import com.imdb.queue.WorkQueue;
import com.imdb.util.rating.strategy.RatingStregety;
import com.sun.istack.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * The Service class which async calculates the rating for a given TV series.
 * This class is inject with {@link RatingStregety} which deifines how the Seassons rating
 * is calculated. When a request to read the ratting is performed, we return the current stored rating in
 * {@link Rating} and queue up a {@link WorkItem} to async recalcuate the rating and update the same.
 */
public class RatingCalculator {
    private final RatingStregety stratgey;
    private final RatingRepository ratingRepository;
    private WorkQueue queue;

    public RatingCalculator(RatingStregety stratgey,
                            RatingRepository ratingRepository,
                            WorkQueue queue) {
        this.stratgey = stratgey;
        this.ratingRepository = ratingRepository;
        this.queue = queue;
    }

    /**
     * Queue a task to update the Rating
     */
    public void updateRating(String tvSeriesId, int seassonNumber) {
        queue.put(new WorkItem(() -> updateRating(tvSeriesId, seassonNumber, null)));
    }

    void updateRating(String tvSeriesId, int seassonNumber, @Nullable Double currentValue) {
        List<Rating> ratings = ratingRepository.findRatingsForSeriesAndSeasson(tvSeriesId, seassonNumber);
        if (ratings.size() >= 0) {
            Double rate = stratgey.calculateSeasonRating(ratings);
            if (!rate.equals(currentValue)) {
                Optional<Rating> tvSeriesRating = ratingRepository.findById(tvSeriesId);
                if (!tvSeriesId.isEmpty()) {
                    Rating entity = tvSeriesRating.get();
                    Rating updatedEntity = entity.toBuilder()
                            .averageRating(rate)
                            .build();
                    ratingRepository.save(updatedEntity);
                }
            }
        }
    }

    /**
     * Return the current stored rating and quue a task to recalculate the rating
     */
    public Double calculateSeasonRating(String tvSeriesId, int seassonNumber) {
        Rating seseasonEntity = ratingRepository
                .findById(tvSeriesId)
                .orElseThrow(() -> new ItemNotFoundException("Cannot find tv series with id" + tvSeriesId));
        Double currentValue = seseasonEntity.getAverageRating();
        queue.put(new WorkItem(() -> updateRating(tvSeriesId, seassonNumber, currentValue)));

        return currentValue;

    }
}
