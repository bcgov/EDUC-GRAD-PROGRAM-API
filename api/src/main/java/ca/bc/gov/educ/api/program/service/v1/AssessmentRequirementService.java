package ca.bc.gov.educ.api.program.service.v1;

import ca.bc.gov.educ.api.program.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.program.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.repository.v1.AssessmentRequirementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssessmentRequirementService {

    private final AssessmentRequirementRepository assessmentRequirementRepository;

    public List<AssessmentRequirementEntity> getAllAssessmentRequirements() {
        return assessmentRequirementRepository.findAll();
    }

    public AssessmentRequirementEntity getAssessmentRequirement(UUID assessmentRequirementId) {
        Optional<AssessmentRequirementEntity> assessmentRequirementOptional = assessmentRequirementRepository.findById(assessmentRequirementId);
        return assessmentRequirementOptional.orElseThrow(() -> 
            new EntityNotFoundException(AssessmentRequirementEntity.class, "assessmentRequirementId", assessmentRequirementId.toString()));
    }

    public List<AssessmentRequirementEntity> getAssessmentRequirementsByAssessmentCode(String assessmentCode) {
        return assessmentRequirementRepository.findByAssessmentCode(assessmentCode);
    }

    public List<AssessmentRequirementEntity> getAssessmentRequirementsByProgramRequirementCode(String programRequirementCode) {
        return assessmentRequirementRepository.findByProgramRequirementCode(programRequirementCode);
    }
} 