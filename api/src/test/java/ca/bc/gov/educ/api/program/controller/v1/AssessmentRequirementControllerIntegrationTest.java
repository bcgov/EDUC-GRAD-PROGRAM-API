package ca.bc.gov.educ.api.program.controller.v1;

import ca.bc.gov.educ.api.program.model.dto.external.algorithm.AssessmentRequirement;
import ca.bc.gov.educ.api.program.model.entity.ProgramAssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.model.entity.OptionalProgramAssessmentRequirementEntity;
import ca.bc.gov.educ.api.program.model.entity.ProgramRequirementCodeEntity;
import ca.bc.gov.educ.api.program.model.entity.OptionalProgramRequirementCodeEntity;
import ca.bc.gov.educ.api.program.repository.v1.ProgramAssessmentRequirementRepository;
import ca.bc.gov.educ.api.program.repository.v1.OptionalProgramAssessmentRequirementRepository;
import ca.bc.gov.educ.api.program.repository.ProgramRequirementCodeRepository;
import ca.bc.gov.educ.api.program.repository.OptionalProgramRequirementCodeRepository;
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
    private ProgramAssessmentRequirementRepository programAssessmentRequirementRepository;

    @Autowired
    private OptionalProgramAssessmentRequirementRepository optionalProgramAssessmentRequirementRepository;

    @Autowired
    private ProgramRequirementCodeRepository programRequirementCodeRepository;

    @Autowired
    private OptionalProgramRequirementCodeRepository optionalProgramRequirementCodeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private ProgramAssessmentRequirementEntity testProgramAssessmentRequirement;
    private OptionalProgramAssessmentRequirementEntity testOptionalAssessmentRequirement;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        // Create test data with required related entities
        ProgramRequirementCodeEntity programRequirementCode = new ProgramRequirementCodeEntity();
        programRequirementCode.setProReqCode("REQ001");
        programRequirementCode.setLabel("Test Program Requirement");
        programRequirementCode.setDescription("Test Description");
        programRequirementCode.setTraxReqNumber("TRAX001");
        programRequirementCode = programRequirementCodeRepository.save(programRequirementCode);

        OptionalProgramRequirementCodeEntity optionalProgramRequirementCode = new OptionalProgramRequirementCodeEntity();
        optionalProgramRequirementCode.setOptProReqCode("OPT001");
        optionalProgramRequirementCode.setLabel("Test Optional Program Requirement");
        optionalProgramRequirementCode.setDescription("Test Optional Description");
        optionalProgramRequirementCode.setTraxReqNumber("TRAX002");
        optionalProgramRequirementCode = optionalProgramRequirementCodeRepository.save(optionalProgramRequirementCode);

        testProgramAssessmentRequirement = ProgramAssessmentRequirementEntity.builder()
                .programAssessmentRequirementId(UUID.randomUUID())
                .assessmentCode("MATH10")
                .programRequirementCodeEntity(programRequirementCode)
                .build();

        testOptionalAssessmentRequirement = OptionalProgramAssessmentRequirementEntity.builder()
                .optionalProgramAssessmentRequirementId(UUID.randomUUID())
                .assessmentCode("ENGL10")
                .optionalProgramRequirementCodeEntity(optionalProgramRequirementCode)
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/program/assessment-requirements Tests")
    class GetAllAssessmentRequirementsTests {

        @Test
        @WithMockUser(authorities = {"SCOPE_READ_GRAD_PROGRAM_CODE_DATA"})
        @DisplayName("Should return all assessment requirements when data exists")
        void shouldReturnAllAssessmentRequirements_WhenDataExists() throws Exception {
            // Arrange
            programAssessmentRequirementRepository.save(testProgramAssessmentRequirement);
            optionalProgramAssessmentRequirementRepository.save(testOptionalAssessmentRequirement);

            // Act & Assert
            String response = mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // Verify response structure
            List<AssessmentRequirement> result = Arrays.asList(objectMapper.readValue(response, AssessmentRequirement[].class));

            assertThat(result).hasSize(2);

            // Verify first result (program assessment requirement)
            AssessmentRequirement firstResult = result.get(0);
            assertThat(firstResult.getAssessmentRequirementId()).isNotNull();
            assertThat(firstResult.getAssessmentCode()).isIn("MATH10", "ENGL10");
            assertThat(firstResult.getRuleCode()).isNotNull();
            assertThat(firstResult.getRuleCode().getAssmtRequirementCode()).isIn("REQ001", "OPT001");
        }

        @Test
        @WithMockUser(authorities = {"SCOPE_READ_GRAD_PROGRAM_CODE_DATA"})
        @DisplayName("Should return empty list when no data exists")
        void shouldReturnEmptyList_WhenNoDataExists() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Should return 401 when user is not authenticated")
        void shouldReturn401_WhenUserNotAuthenticated() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(authorities = {"SCOPE_OTHER"})
        @DisplayName("Should return 403 when user lacks required permissions")
        void shouldReturn403_WhenUserLacksRequiredPermissions() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/program/assessment-requirements")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }
}