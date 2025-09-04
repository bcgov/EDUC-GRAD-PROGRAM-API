package ca.bc.gov.educ.api.program.mappers.v1;

import ca.bc.gov.educ.api.program.model.dto.external.algorithm.AssessmentRequirement;
import ca.bc.gov.educ.api.program.model.dto.external.algorithm.AssessmentRequirementCode;
import ca.bc.gov.educ.api.program.model.entity.ProgramAssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.model.entity.OptionalProgramAssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.model.entity.ProgramRequirementCodeEntity;
import ca.bc.gov.educ.api.program.model.entity.OptionalProgramRequirementCodeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateMapper.class, UUIDMapper.class})
public interface AssessmentRequirementMapper {

    AssessmentRequirementMapper mapper = Mappers.getMapper(AssessmentRequirementMapper.class);

    @Mapping(target = "assessmentRequirementId", source = "programAssessmentRequirementId")
    @Mapping(target = "ruleCode", source = "programRequirementCodeEntity")
    AssessmentRequirement toStructureFromProgram(ProgramAssessmentRequirementEntity entity);

    @Mapping(source = "optionalProgramAssessmentRequirementId", target = "assessmentRequirementId")
    @Mapping(source = "optionalProgramRequirementCodeEntity", target = "ruleCode")
    AssessmentRequirement toStructureFromOptional(OptionalProgramAssessmentRequirementEntity entity);

    @Mapping(source = "proReqCode", target = "assmtRequirementCode")
    @Mapping(target = "effectiveDate", ignore = true)
    @Mapping(target = "expiryDate", ignore = true)
    AssessmentRequirementCode map(ProgramRequirementCodeEntity entity);

    @Mapping(source = "optProReqCode", target = "assmtRequirementCode")
    @Mapping(target = "effectiveDate", ignore = true)
    @Mapping(target = "expiryDate", ignore = true)
    AssessmentRequirementCode map(OptionalProgramRequirementCodeEntity entity);
}
