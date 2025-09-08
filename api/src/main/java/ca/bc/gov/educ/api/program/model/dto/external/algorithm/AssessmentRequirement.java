package ca.bc.gov.educ.api.program.model.dto.external.algorithm;

import java.util.UUID;

import ca.bc.gov.educ.api.program.model.dto.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AssessmentRequirement extends BaseModel {

    private UUID assessmentRequirementId;
    private String assessmentCode;
    private AssessmentRequirementCode ruleCode;
}