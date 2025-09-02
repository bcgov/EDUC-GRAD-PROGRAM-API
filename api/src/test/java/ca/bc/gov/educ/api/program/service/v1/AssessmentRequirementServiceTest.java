package ca.bc.gov.educ.api.program.service.v1;

import ca.bc.gov.educ.api.program.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.program.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.repository.v1.AssessmentRequirementRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssessmentRequirementService Tests")
class AssessmentRequirementServiceTest {

    @Mock
    private AssessmentRequirementRepository assessmentRequirementRepository;

    @InjectMocks
    private AssessmentRequirementService assessmentRequirementService;

    private AssessmentRequirementEntity testAssessmentRequirement;
    private UUID testAssessmentRequirementId;
    private String testAssessmentCode;
    private String testProgramRequirementCode;

    @BeforeEach
    void setUp() {
        testAssessmentRequirementId = UUID.randomUUID();
        testAssessmentCode = "MATH10";
        testProgramRequirementCode = "REQ001";
        
        testAssessmentRequirement = AssessmentRequirementEntity.builder()
                .assessmentRequirementId(testAssessmentRequirementId)
                .assessmentCode(testAssessmentCode)
                .programRequirementCode(testProgramRequirementCode)
                .build();
    }

    @Nested
    @DisplayName("getAllAssessmentRequirements Tests")
    class GetAllAssessmentRequirementsTests {

        @Test
        @DisplayName("Should return all assessment requirements when repository has data")
        void shouldReturnAllAssessmentRequirements_WhenRepositoryHasData() {
            // Arrange
            List<AssessmentRequirementEntity> expectedRequirements = Arrays.asList(
                    testAssessmentRequirement,
                    AssessmentRequirementEntity.builder()
                            .assessmentRequirementId(UUID.randomUUID())
                            .assessmentCode("ENGL10")
                            .programRequirementCode("REQ002")
                            .build()
            );
            when(assessmentRequirementRepository.findAll()).thenReturn(expectedRequirements);

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAllAssessmentRequirements();

            // Assert
            assertThat(result)
                    .isNotNull()
                    .hasSize(2)
                    .isEqualTo(expectedRequirements);
            
            verify(assessmentRequirementRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when repository has no data")
        void shouldReturnEmptyList_WhenRepositoryHasNoData() {
            // Arrange
            when(assessmentRequirementRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAllAssessmentRequirements();

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
            
            verify(assessmentRequirementRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getAssessmentRequirement Tests")
    class GetAssessmentRequirementTests {

        @Test
        @DisplayName("Should return assessment requirement when found by valid ID")
        void shouldReturnAssessmentRequirement_WhenFoundByValidId() {
            // Arrange
            when(assessmentRequirementRepository.findById(testAssessmentRequirementId))
                    .thenReturn(Optional.of(testAssessmentRequirement));

            // Act
            AssessmentRequirementEntity result = assessmentRequirementService.getAssessmentRequirement(testAssessmentRequirementId);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(testAssessmentRequirement);
            
            verify(assessmentRequirementRepository, times(1)).findById(testAssessmentRequirementId);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when assessment requirement not found")
        void shouldThrowEntityNotFoundException_WhenAssessmentRequirementNotFound() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            when(assessmentRequirementRepository.findById(nonExistentId))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> assessmentRequirementService.getAssessmentRequirement(nonExistentId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("AssessmentRequirementEntity was not found for parameters")
                    .hasMessageContaining("assessmentRequirementId")
                    .hasMessageContaining(nonExistentId.toString());

            verify(assessmentRequirementRepository, times(1)).findById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("getAssessmentRequirementsByAssessmentCode Tests")
    class GetAssessmentRequirementsByAssessmentCodeTests {

        @Test
        @DisplayName("Should return assessment requirements when found by valid assessment code")
        void shouldReturnAssessmentRequirements_WhenFoundByValidAssessmentCode() {
            // Arrange
            List<AssessmentRequirementEntity> expectedRequirements = Arrays.asList(
                    testAssessmentRequirement,
                    AssessmentRequirementEntity.builder()
                            .assessmentRequirementId(UUID.randomUUID())
                            .assessmentCode(testAssessmentCode)
                            .programRequirementCode("REQ003")
                            .build()
            );
            when(assessmentRequirementRepository.findByAssessmentCode(testAssessmentCode))
                    .thenReturn(expectedRequirements);

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAssessmentRequirementsByAssessmentCode(testAssessmentCode);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .hasSize(2)
                    .isEqualTo(expectedRequirements);
            
            verify(assessmentRequirementRepository, times(1)).findByAssessmentCode(testAssessmentCode);
        }

        @Test
        @DisplayName("Should return empty list when no assessment requirements found for assessment code")
        void shouldReturnEmptyList_WhenNoAssessmentRequirementsFoundForAssessmentCode() {
            // Arrange
            String nonExistentAssessmentCode = "NONEXISTENT";
            when(assessmentRequirementRepository.findByAssessmentCode(nonExistentAssessmentCode))
                    .thenReturn(Collections.emptyList());

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAssessmentRequirementsByAssessmentCode(nonExistentAssessmentCode);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
            
            verify(assessmentRequirementRepository, times(1)).findByAssessmentCode(nonExistentAssessmentCode);
        }

        @Test
        @DisplayName("Should handle null assessment code gracefully")
        void shouldHandleNullAssessmentCode_Gracefully() {
            // Arrange
            when(assessmentRequirementRepository.findByAssessmentCode(null))
                    .thenReturn(Collections.emptyList());

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAssessmentRequirementsByAssessmentCode(null);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
            
            verify(assessmentRequirementRepository, times(1)).findByAssessmentCode(null);
        }

        @Test
        @DisplayName("Should handle empty assessment code gracefully")
        void shouldHandleEmptyAssessmentCode_Gracefully() {
            // Arrange
            String emptyAssessmentCode = "";
            when(assessmentRequirementRepository.findByAssessmentCode(emptyAssessmentCode))
                    .thenReturn(Collections.emptyList());

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAssessmentRequirementsByAssessmentCode(emptyAssessmentCode);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
            
            verify(assessmentRequirementRepository, times(1)).findByAssessmentCode(emptyAssessmentCode);
        }
    }

    @Nested
    @DisplayName("getAssessmentRequirementsByProgramRequirementCode Tests")
    class GetAssessmentRequirementsByProgramRequirementCodeTests {

        @Test
        @DisplayName("Should return assessment requirements when found by valid program requirement code")
        void shouldReturnAssessmentRequirements_WhenFoundByValidProgramRequirementCode() {
            // Arrange
            List<AssessmentRequirementEntity> expectedRequirements = Arrays.asList(
                    testAssessmentRequirement,
                    AssessmentRequirementEntity.builder()
                            .assessmentRequirementId(UUID.randomUUID())
                            .assessmentCode("ENGL10")
                            .programRequirementCode(testProgramRequirementCode)
                            .build()
            );
            when(assessmentRequirementRepository.findByProgramRequirementCode(testProgramRequirementCode))
                    .thenReturn(expectedRequirements);

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAssessmentRequirementsByProgramRequirementCode(testProgramRequirementCode);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .hasSize(2)
                    .isEqualTo(expectedRequirements);
            
            verify(assessmentRequirementRepository, times(1)).findByProgramRequirementCode(testProgramRequirementCode);
        }

        @Test
        @DisplayName("Should return empty list when no assessment requirements found for program requirement code")
        void shouldReturnEmptyList_WhenNoAssessmentRequirementsFoundForProgramRequirementCode() {
            // Arrange
            String nonExistentProgramRequirementCode = "NONEXISTENT";
            when(assessmentRequirementRepository.findByProgramRequirementCode(nonExistentProgramRequirementCode))
                    .thenReturn(Collections.emptyList());

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAssessmentRequirementsByProgramRequirementCode(nonExistentProgramRequirementCode);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
            
            verify(assessmentRequirementRepository, times(1)).findByProgramRequirementCode(nonExistentProgramRequirementCode);
        }

        @Test
        @DisplayName("Should handle null program requirement code gracefully")
        void shouldHandleNullProgramRequirementCode_Gracefully() {
            // Arrange
            when(assessmentRequirementRepository.findByProgramRequirementCode(null))
                    .thenReturn(Collections.emptyList());

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAssessmentRequirementsByProgramRequirementCode(null);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
            
            verify(assessmentRequirementRepository, times(1)).findByProgramRequirementCode(null);
        }

        @Test
        @DisplayName("Should handle empty program requirement code gracefully")
        void shouldHandleEmptyProgramRequirementCode_Gracefully() {
            // Arrange
            String emptyProgramRequirementCode = "";
            when(assessmentRequirementRepository.findByProgramRequirementCode(emptyProgramRequirementCode))
                    .thenReturn(Collections.emptyList());

            // Act
            List<AssessmentRequirementEntity> result = assessmentRequirementService.getAssessmentRequirementsByProgramRequirementCode(emptyProgramRequirementCode);

            // Assert
            assertThat(result)
                    .isNotNull()
                    .isEmpty();
            
            verify(assessmentRequirementRepository, times(1)).findByProgramRequirementCode(emptyProgramRequirementCode);
        }
    }
}
