package ca.bc.gov.educ.api.program.mappers.v1;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Date mapper.
 */
public class DateMapper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Map date to string.
     *
     * @param date the date
     * @return the string
     */
    public String map(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMAT.format(date);
    }

    /**
     * Map string to date.
     *
     * @param dateString the date string
     * @return the date
     */
    public Date map(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
}

