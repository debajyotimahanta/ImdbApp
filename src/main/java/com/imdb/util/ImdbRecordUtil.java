package com.imdb.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class ImdbRecordUtil {
    public static boolean isNull(String value) {
        return "\\N".equals(value);
    }

    public static Optional<Integer> paseInteger(String value) {
        if(!isNull(value)) {
            try {
                return Optional.of(Integer.valueOf(value));
            } catch (NumberFormatException ex) {
                //We dont do anything since we will return empty
            }
        }
        return Optional.empty();

    }

    public static Optional<Double> paseDouble(String value) {
        if(!isNull(value)) {
            try {
                return Optional.of(Double.valueOf(value));
            } catch (NumberFormatException ex) {
                //We dont do anything since we will return empty
            }
        }
        return Optional.empty();

    }
}
