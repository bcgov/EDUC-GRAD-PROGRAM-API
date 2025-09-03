package ca.bc.gov.educ.api.program.mappers.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DateMapper Unit Tests")
class DateMapperTest {

    private DateMapper dateMapper;

    @BeforeEach
    void setUp() {
        dateMapper = new DateMapper();
    }

    @Test
    @DisplayName("Should return null when date is null")
    void shouldReturnNull_WhenDateIsNull() {
        // Act
        String result = dateMapper.map((Date) null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should format valid date to string")
    void shouldFormatValidDate_ToString() throws ParseException {
        // Arrange
        SimpleDateFormat testFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date testDate = testFormat.parse("2024-06-15 14:30:45");

        // Act
        String result = dateMapper.map(testDate);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("2024-06-15T14:30:45");
    }

    @Test
    @DisplayName("Should return null when dateString is null")
    void shouldReturnNull_WhenDateStringIsNull() {
        // Act
        Date result = dateMapper.map((String) null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return null when dateString is empty")
    void shouldReturnNull_WhenDateStringIsEmpty() {
        // Act
        Date result = dateMapper.map("");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should parse valid date string to Date")
    void shouldParseValidDateString_ToDate() {
        // Arrange
        String validDateString = "2024-06-15T14:30:45";

        // Act
        Date result = dateMapper.map(validDateString);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Date.class);
    }

    @Test
    @DisplayName("Should return null when dateString has invalid format")
    void shouldReturnNull_WhenDateStringHasInvalidFormat() {
        // Arrange
        String invalidDateString = "invalid-date-format";

        // Act
        Date result = dateMapper.map(invalidDateString);

        // Assert
        assertThat(result).isNull();
    }
}
