package ca.bc.gov.educ.api.program.model.dto;

import java.util.UUID;

import ca.bc.gov.educ.api.program.validator.constraint.IsAllowedValue;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class AssessmentRequirement extends BaseModel {

    private UUID assessmentRequirementId;
    @IsAllowedValue(enumName = "AssessmentTypeCodes", message = "Invalid assessment type code.")
    private String assessmentCode;
    private String programRequirementCode;
}