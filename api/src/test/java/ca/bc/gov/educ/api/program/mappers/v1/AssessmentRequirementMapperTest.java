package ca.bc.gov.educ.api.program.mappers.v1;

import ca.bc.gov.educ.api.program.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.program.model.entity.AssessmentRequirementEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AssessmentRequirementMapperTest {

    @Test
    void testToStructure() {
        // Given
        UUID id = UUID.randomUUID();
        AssessmentRequirementEntity entity = AssessmentRequirementEntity.builder()
                .assessmentRequirementId(id)
                .assessmentCode("LTE10")
                .programRequirementCode("115")
                .build();

        // When
        AssessmentRequirement result = AssessmentRequirementMapper.mapper.toStructure(entity);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getAssessmentRequirementId());
        assertEquals("LTE10", result.getAssessmentCode());
        assertEquals("115", result.getProgramRequirementCode());
    }

    @Test
    void testToEntity() {
        // Given
        UUID id = UUID.randomUUID();
        AssessmentRequirement dto = new AssessmentRequirement();
        dto.setAssessmentRequirementId(id);
        dto.setAssessmentCode("NME");
        dto.setProgramRequirementCode("116");

        // When
        AssessmentRequirementEntity result = AssessmentRequirementMapper.mapper.toEntity(dto);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getAssessmentRequirementId());
        assertEquals("NME", result.getAssessmentCode());
        assertEquals("116", result.getProgramRequirementCode());
    }
}

