package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

//@@author parnikkapore-reused
// Taken from Parnikkapore's ip. Note for graders: The test code is new

/**
 * A simple JChronic-based date + time parser.
 */
public class NaturalDateParser {
    /**
     * Parses the given string, assumed to be in the local time zone, into a LocalDateTime.
     *
     * @param input The string to be parsed.
     * @param currentTime The current time to be used while parsing.
     * @return The parsed string.
     * @throws DateTimeNotFoundException if neither a date nor a time can be resolved.
     */
    public static LocalDateTime parse(String input, Instant currentTime) throws DateTimeNotFoundException {
        requireNonNull(input);

        List<Date> dates = new PrettyTimeParser().parse(input, Date.from(currentTime));
        if (dates.isEmpty()) {
            throw new DateTimeNotFoundException(input);
        }

        Instant result = dates.get(0).toInstant();
        return LocalDateTime.ofInstant(result, ZoneId.systemDefault());
    }

    /**
     * Parses the given string, assumed to be in the local time zone, into a LocalDateTime.
     *
     * @param input The string to be parsed.
     * @return The parsed string.
     * @throws DateTimeNotFoundException if neither a date nor a time can be resolved.
     */
    public static LocalDateTime parse(String input) throws DateTimeNotFoundException {
        return parse(input, Instant.now());
    }

    /**
     * Neither a date nor a time can be found in the given string.
     */
    public static class DateTimeNotFoundException extends IllegalArgumentException {
        private final String parsedString;

        private DateTimeNotFoundException(String parsedString) {
            super("Cannot find a date in " + parsedString);
            this.parsedString = parsedString;
        }

        public String getParsedString() {
            return parsedString;
        }
    }

    /**
     * For testing purposes only.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        LocalDateTime test = parse("2 Jan 2006 15:04:05");
        System.out.println(test);
    }
}
