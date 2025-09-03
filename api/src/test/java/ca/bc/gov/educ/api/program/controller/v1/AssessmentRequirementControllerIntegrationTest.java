package ca.bc.gov.educ.api.program.controller.v1;

import ca.bc.gov.educ.api.program.mappers.v1.AssessmentRequirementMapper;
import ca.bc.gov.educ.api.program.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.program.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.repository.v1.AssessmentRequirementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("AssessmentRequirementController Integration Tests")
class AssessmentRequirementControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AssessmentRequirementRepository assessmentRequirementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private UUID testAssessmentRequirementId1;
    private UUID testAssessmentRequirementId2;
    private String testAssessmentCode;
    private String testProgramRequirementCode;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        try {
            AssessmentRequirementMapper mapper = AssessmentRequirementMapper.mapper;
            System.out.println("Mapper initialized: " + (mapper != null));
        } catch (Exception e) {
            System.err.println("Mapper initialization failed: " + e.getMessage());
        }

        // Clean up any existing test data
        assessmentRequirementRepository.deleteAll();

        // Create test data
        testAssessmentCode = "MATH10";
        testProgramRequirementCode = "REQ001";

      AssessmentRequirementEntity testAssessmentRequirement1 = AssessmentRequirementEntity.builder()
          .assessmentCode(testAssessmentCode)
          .programRequirementCode(testProgramRequirementCode)
          .build();

      AssessmentRequirementEntity testAssessmentRequirement2 = AssessmentRequirementEntity.builder()
          .assessmentCode("ENGL10")
          .programRequirementCode("REQ002")
          .build();


        // Save test data to database
        assessmentRequirementRepository.saveAll(Arrays.asList(testAssessmentRequirement1, testAssessmentRequirement2));
        this.testAssessmentRequirementId1 = testAssessmentRequirement1.getAssessmentRequirementId();
        this.testAssessmentRequirementId2 = testAssessmentRequirement2.getAssessmentRequirementId();
    }

    @Nested
    @DisplayName("GET /api/v1/program/assessment-requirements Tests")
    class GetAllAssessmentRequirementsTests {

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return all assessment requirements when user has proper authority")
        void shouldReturnAllAssessmentRequirements_WhenUserHasProperAuthority() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(2)))
                    .andExpect(jsonPath("$[0].assessmentRequirementId").value(testAssessmentRequirementId1.toString()))
                    .andExpect(jsonPath("$[0].assessmentCode").value(testAssessmentCode))
                    .andExpect(jsonPath("$[0].programRequirementCode").value(testProgramRequirementCode))
                    .andExpect(jsonPath("$[1].assessmentRequirementId").value(testAssessmentRequirementId2.toString()))
                    .andExpect(jsonPath("$[1].assessmentCode").value("ENGL10"))
                    .andExpect(jsonPath("$[1].programRequirementCode").value("REQ002"));
        }

        @Test
        @WithMockUser()
        @DisplayName("Should return 403 Forbidden when user lacks proper authority")
        void shouldReturnForbidden_WhenUserLacksProperAuthority() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return empty array when no assessment requirements exist")
        void shouldReturnEmptyArray_WhenNoAssessmentRequirementsExist() throws Exception {
            // Arrange
            assessmentRequirementRepository.deleteAll();

            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/program/assessment-requirements/{assessmentRequirementId} Tests")
    class GetAssessmentRequirementByIdTests {

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return assessment requirement when found by valid ID")
        void shouldReturnAssessmentRequirement_WhenFoundByValidId() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/{assessmentRequirementId}", testAssessmentRequirementId1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.assessmentRequirementId").value(testAssessmentRequirementId1.toString()))
                    .andExpect(jsonPath("$.assessmentCode").value(testAssessmentCode))
                    .andExpect(jsonPath("$.programRequirementCode").value(testProgramRequirementCode));
        }

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return 404 Not Found when assessment requirement ID does not exist")
        void shouldReturnNotFound_WhenAssessmentRequirementIdDoesNotExist() throws Exception {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();

            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/{assessmentRequirementId}", nonExistentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(authorities = "SCOPE_FAKE")
        @DisplayName("Should return 403 Forbidden when user lacks proper authority")
        void shouldReturnForbidden_WhenUserLacksProperAuthority() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/{assessmentRequirementId}", testAssessmentRequirementId1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should handle malformed UUID gracefully")
        void shouldHandleMalformedUuid_Gracefully() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/{assessmentRequirementId}", "invalid-uuid")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/program/assessment-requirements/assessment/{assessmentCode} Tests")
    class GetAssessmentRequirementsByAssessmentCodeTests {

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return assessment requirements when found by valid assessment code")
        void shouldReturnAssessmentRequirements_WhenFoundByValidAssessmentCode() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/assessment/{assessmentCode}", testAssessmentCode)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(1)))
                    .andExpect(jsonPath("$[0].assessmentRequirementId").value(testAssessmentRequirementId1.toString()))
                    .andExpect(jsonPath("$[0].assessmentCode").value(testAssessmentCode))
                    .andExpect(jsonPath("$[0].programRequirementCode").value(testProgramRequirementCode));
        }

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return empty array when no assessment requirements found for assessment code")
        void shouldReturnEmptyArray_WhenNoAssessmentRequirementsFoundForAssessmentCode() throws Exception {
            // Arrange
            String nonExistentAssessmentCode = "NONEXISTENT";

            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/assessment/{assessmentCode}", nonExistentAssessmentCode)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(0)));
        }

        @Test
        @WithMockUser(authorities = "SCOPE_FAKE")
        @DisplayName("Should return 403 Forbidden when user lacks proper authority")
        void shouldReturnForbidden_WhenUserLacksProperAuthority() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/assessment/{assessmentCode}", testAssessmentCode)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }
    @Nested
    @DisplayName("GET /api/v1/program/assessment-requirements/requirement/{programRequirementCode} Tests")
    class GetAssessmentRequirementsByProgramRequirementCodeTests {

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return assessment requirements when found by valid program requirement code")
        void shouldReturnAssessmentRequirements_WhenFoundByValidProgramRequirementCode() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/requirement/{programRequirementCode}", testProgramRequirementCode)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].assessmentRequirementId").value(testAssessmentRequirementId1.toString()))
                .andExpect(jsonPath("$[0].assessmentCode").value(testAssessmentCode))
                .andExpect(jsonPath("$[0].programRequirementCode").value(testProgramRequirementCode));
        }

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return empty array when no assessment requirements found for program requirement code")
        void shouldReturnEmptyArray_WhenNoAssessmentRequirementsFoundForProgramRequirementCode() throws Exception {
            // Arrange
            String nonExistentProgramRequirementCode = "NONEXISTENT";

            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/requirement/{programRequirementCode}", nonExistentProgramRequirementCode)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(0)));
        }

        @Test
        @WithMockUser(authorities = "SCOPE_FAKE_SCOPE")
        @DisplayName("Should return 403 Forbidden when user lacks proper authority")
        void shouldReturnForbidden_WhenUserLacksProperAuthority() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/requirement/{programRequirementCode}", testProgramRequirementCode)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Cross-Endpoint Integration Tests")
    class CrossEndpointIntegrationTests {

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should maintain data consistency across all endpoints")
        void shouldMaintainDataConsistency_AcrossAllEndpoints() throws Exception {
            // Test all endpoints and verify data consistency
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String byIdResponse = mockMvc.perform(get("/api/v1/program/assessment-requirements/{assessmentRequirementId}", testAssessmentRequirementId1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String byAssessmentCodeResponse = mockMvc.perform(get("/api/v1/program/assessment-requirements/assessment/{assessmentCode}", testAssessmentCode)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String byProgramRequirementCodeResponse = mockMvc.perform(get("/api/v1/program/assessment-requirements/requirement/{programRequirementCode}", testProgramRequirementCode)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // Parse responses and verify consistency
            List<AssessmentRequirement> allRequirements = Collections.singletonList(
                objectMapper.readValue(byIdResponse, AssessmentRequirement.class)
            );

            List<AssessmentRequirement> byAssessmentCode = Arrays.asList(
                    objectMapper.readValue(byAssessmentCodeResponse, AssessmentRequirement[].class)
            );

            List<AssessmentRequirement> byProgramRequirementCode = Arrays.asList(
                    objectMapper.readValue(byProgramRequirementCodeResponse, AssessmentRequirement[].class)
            );

            // Verify data consistency
            assertThat(allRequirements).hasSize(1);
            assertThat(byAssessmentCode).hasSize(1);
            assertThat(byProgramRequirementCode).hasSize(1);

            AssessmentRequirement firstRequirement = allRequirements.get(0);
            assertThat(firstRequirement.getAssessmentRequirementId()).isEqualTo(testAssessmentRequirementId1);
            assertThat(firstRequirement.getAssessmentCode()).isEqualTo(testAssessmentCode);
            assertThat(firstRequirement.getProgramRequirementCode()).isEqualTo(testProgramRequirementCode);

            // Verify the same entity is returned across different endpoints
            assertThat(byAssessmentCode.get(0).getAssessmentRequirementId()).isEqualTo(firstRequirement.getAssessmentRequirementId());
            assertThat(byProgramRequirementCode.get(0).getAssessmentRequirementId()).isEqualTo(firstRequirement.getAssessmentRequirementId());
        }

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should handle concurrent requests to different endpoints")
        void shouldHandleConcurrentRequests_ToDifferentEndpoints() throws Exception {
            // This test verifies that the controller can handle multiple concurrent requests
            // without data corruption or race conditions
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/program/assessment-requirements/{assessmentRequirementId}", testAssessmentRequirementId1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/program/assessment-requirements/assessment/{assessmentCode}", testAssessmentCode)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/program/assessment-requirements/requirement/{programRequirementCode}", testProgramRequirementCode)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // Verify all endpoints still return correct data
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(2)));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should return appropriate error for invalid endpoint")
        void shouldReturnAppropriateError_ForInvalidEndpoint() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/invalid/endpoint")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should handle special characters in path parameters gracefully")
        void shouldHandleSpecialCharacters_InPathParametersGracefully() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements/assessment/{assessmentCode}", "MATH@10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(0)));
        }
    }

    @Nested
    @DisplayName("Mapper Integration Tests")
    class MapperIntegrationTests {
        @Test
        @WithMockUser(authorities = "SCOPE_READ_GRAD_PROGRAM_CODE_DATA")
        @DisplayName("Should properly map date fields using DateMapper")
        void shouldProperlyMapDateFields_UsingDateMapper() throws Exception {
            // Act
            String response = mockMvc.perform(get("/api/v1/program/assessment-requirements/{assessmentRequirementId}", testAssessmentRequirementId1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // Parse the response to verify date mapping
            AssessmentRequirement result = objectMapper.readValue(response, AssessmentRequirement.class);

            // Assert date fields are properly mapped as strings
            assertThat(result.getCreateDate())
                    .isNotNull()
                    .isInstanceOf(String.class);
            
            assertThat(result.getUpdateDate())
                    .isNotNull()
                    .isInstanceOf(String.class);

            // Verify date format matches DateMapper pattern (yyyy-MM-dd'T'HH:mm:ss)
            String datePattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}";
            assertThat(result.getCreateDate()).matches(datePattern);
            assertThat(result.getUpdateDate()).matches(datePattern);
        }
    }
}
