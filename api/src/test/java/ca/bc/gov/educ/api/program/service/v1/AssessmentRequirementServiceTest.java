package ca.bc.gov.educ.api.program.service.v1;

import ca.bc.gov.educ.api.program.model.dto.external.algorithm.AssessmentRequirement;
import ca.bc.gov.educ.api.program.model.entity.ProgramAssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.model.entity.OptionalProgramAssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.repository.v1.ProgramAssessmentRequirementRepository;
import ca.bc.gov.educ.api.program.repository.v1.OptionalProgramAssessmentRequirementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssessmentRequirementService Tests")
class AssessmentRequirementServiceTest {

    @Mock
    private ProgramAssessmentRequirementRepository programAssessmentRequirementRepository;
    
    @Mock
    private OptionalProgramAssessmentRequirementRepository optionalProgramAssessmentRequirementRepository;

    @InjectMocks
    private AssessmentRequirementService assessmentRequirementService;

    private ProgramAssessmentRequirementEntity testProgramAssessmentRequirement;
    private OptionalProgramAssessmentRequirementEntity testOptionalAssessmentRequirement;

  @BeforeEach
    void setUp() {
    UUID testAssessmentRequirementId = UUID.randomUUID();
        
        testProgramAssessmentRequirement = ProgramAssessmentRequirementEntity.builder()
                .programAssessmentRequirementId(testAssessmentRequirementId)
                .assessmentCode("MATH10")
                .build();
        
        testOptionalAssessmentRequirement = OptionalProgramAssessmentRequirementEntity.builder()
                .optionalProgramAssessmentRequirementId(UUID.randomUUID())
                .assessmentCode("MATH10")
                .build();
    }

    @Nested
    @DisplayName("getAllAssessmentRequirements Tests")
    class GetAllAssessmentRequirementsTests {

        @Test
        @DisplayName("Should return all assessment requirements when repository has data")
        void shouldReturnAllAssessmentRequirements_WhenRepositoryHasData() {
            // Arrange
            List<ProgramAssessmentRequirementEntity> programRequirements = Arrays.asList(
                    testProgramAssessmentRequirement,
                    ProgramAssessmentRequirementEntity.builder()
                            .programAssessmentRequirementId(UUID.randomUUID())
                            .assessmentCode("ENGL10")
                            .build()
            );
            List<OptionalProgramAssessmentRequirementEntity> optionalRequirements = Collections.singletonList(
                testOptionalAssessmentRequirement
            );
            
            
            when(programAssessmentRequirementRepository.findAll()).thenReturn(programRequirements);
            when(optionalProgramAssessmentRequirementRepository.findAll()).thenReturn(optionalRequirements);

            // Act
            List<AssessmentRequirement> result = assessmentRequirementService.getAllAssessmentRequirements();

            // Assert
            assertThat(result)
                    .isNotNull()
                    .hasSize(3);
            
            verify(programAssessmentRequirementRepository, times(1)).findAll();
            verify(optionalProgramAssessmentRequirementRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when repository has no data")
        void shouldReturnEmptyList_WhenRepositoryHasNoData() {
            // Arrange
            when(programAssessmentRequirementRepository.findAll()).thenReturn(Collections.emptyList());
            when(optionalProgramAssessmentRequirementRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<AssessmentRequirement> result = assessmentRequirementService.getAllAssessmentRequirements();

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
            
            verify(programAssessmentRequirementRepository, times(1)).findAll();
            verify(optionalProgramAssessmentRequirementRepository, times(1)).findAll();
        }
    }
}