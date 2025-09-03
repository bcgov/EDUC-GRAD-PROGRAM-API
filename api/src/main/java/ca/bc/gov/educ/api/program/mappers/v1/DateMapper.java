package ca.bc.gov.educ.api.program.mappers.v1;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMapper {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public String map(Date date) {
        if (date == null) {
            return null;
        }
        return dateFormat.format(date);
    }

    public Date map(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
}

