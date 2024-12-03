package net.yc.race.track.exceptions;

public class CompetitionNotFoundException extends RuntimeException {
    public CompetitionNotFoundException(String message) {
        super(message);
    }
}
