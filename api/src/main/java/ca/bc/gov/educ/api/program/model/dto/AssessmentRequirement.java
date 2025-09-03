package ca.bc.gov.educ.api.program.model.dto;

import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class AssessmentRequirement extends BaseModel {

    private UUID assessmentRequirementId;
    private String assessmentCode;
    private String programRequirementCode;
}