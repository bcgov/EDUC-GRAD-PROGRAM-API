package ca.bc.gov.educ.api.program.repository.v1;

import ca.bc.gov.educ.api.program.model.entity.ProgramAssessmentRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgramAssessmentRequirementRepository extends JpaRepository<ProgramAssessmentRequirementEntity, UUID> {
}
