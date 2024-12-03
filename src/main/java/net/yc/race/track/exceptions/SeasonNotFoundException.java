package net.yc.race.track.exceptions;

import net.yc.race.track.model.Season;

public class SeasonNotFoundException extends RuntimeException {
    public SeasonNotFoundException(String message) {
        super(message);
    }
}
