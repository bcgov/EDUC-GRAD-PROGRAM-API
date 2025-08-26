package ca.bc.gov.educ.api.program.repository.v1;

import ca.bc.gov.educ.api.program.model.entity.AssessmentRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentRequirementRepository extends JpaRepository<AssessmentRequirementEntity, UUID> {

    List<AssessmentRequirementEntity> findByAssessmentCode(String assessmentCode);
    
    List<AssessmentRequirementEntity> findByProgramRequirementCode(String programRequirementCode);
} 