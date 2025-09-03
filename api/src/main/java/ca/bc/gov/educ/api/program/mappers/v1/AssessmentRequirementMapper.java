package ca.bc.gov.educ.api.program.mappers.v1;

import ca.bc.gov.educ.api.program.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.program.model.entity.AssessmentRequirementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateMapper.class})
public interface AssessmentRequirementMapper {

    AssessmentRequirementMapper mapper = Mappers.getMapper(AssessmentRequirementMapper.class);

    AssessmentRequirement toStructure(AssessmentRequirementEntity entity);
}
