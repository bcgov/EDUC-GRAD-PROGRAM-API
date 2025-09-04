package ca.bc.gov.educ.api.program.mappers.v1;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public class UUIDMapper {

    public UUID map(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return UUID.fromString(value);
    }

    public String map(UUID value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}

