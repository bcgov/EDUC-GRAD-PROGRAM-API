package ca.bc.gov.educ.api.program.service.v1;

import ca.bc.gov.educ.api.program.model.dto.external.algorithm.AssessmentRequirement;
import ca.bc.gov.educ.api.program.model.entity.ProgramAssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.model.entity.OptionalProgramAssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.repository.v1.ProgramAssessmentRequirementRepository;
import ca.bc.gov.educ.api.program.repository.v1.OptionalProgramAssessmentRequirementRepository;
import ca.bc.gov.educ.api.program.mappers.v1.AssessmentRequirementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssessmentRequirementService {

    private final ProgramAssessmentRequirementRepository programAssessmentRequirementRepository;
    private final OptionalProgramAssessmentRequirementRepository optionalProgramAssessmentRequirementRepository;
    private final AssessmentRequirementMapper assessmentRequirementMapper = AssessmentRequirementMapper.mapper;

    public List<AssessmentRequirement> getAllAssessmentRequirements() {
        List<AssessmentRequirement> allRequirements = new ArrayList<>();

        List<ProgramAssessmentRequirementEntity> programRequirements = programAssessmentRequirementRepository.findAll();
        for (ProgramAssessmentRequirementEntity entity : programRequirements) {
            allRequirements.add(assessmentRequirementMapper.toStructureFromProgram(entity));
        }
        List<OptionalProgramAssessmentRequirementEntity> optionalRequirements = optionalProgramAssessmentRequirementRepository.findAll();
        for (OptionalProgramAssessmentRequirementEntity entity : optionalRequirements) {
            allRequirements.add(assessmentRequirementMapper.toStructureFromOptional(entity));
        }
        return allRequirements;
    }

} 