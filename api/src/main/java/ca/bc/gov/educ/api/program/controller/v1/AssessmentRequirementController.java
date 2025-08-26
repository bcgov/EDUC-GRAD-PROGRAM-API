package ca.bc.gov.educ.api.program.controller.v1;

import ca.bc.gov.educ.api.program.mappers.v1.AssessmentRequirementMapper;
import ca.bc.gov.educ.api.program.service.v1.AssessmentRequirementService;
import ca.bc.gov.educ.api.program.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.program.util.PermissionsContants;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/program/assessment-requirements")
public class AssessmentRequirementController {
    private static final AssessmentRequirementMapper mapper = AssessmentRequirementMapper.mapper;

    @Getter(AccessLevel.PRIVATE)
    private final AssessmentRequirementService assessmentRequirementService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_GRAD_PROGRAM')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")
    })
    public List<AssessmentRequirement> getAllAssessmentRequirements() {
        return getAssessmentRequirementService().getAllAssessmentRequirements().stream().map(mapper::toStructure).toList();
    }

    @GetMapping("/{assessmentRequirementId}")
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND."),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")
    })
    public AssessmentRequirement getAssessmentRequirement(@PathVariable UUID assessmentRequirementId) {
        return mapper.toStructure(assessmentRequirementService.getAssessmentRequirement(assessmentRequirementId));
    }

    @GetMapping("/assessment/{assessmentCode}")
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND."),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")
    })
    public List<AssessmentRequirement> getAssessmentRequirementsByAssessmentCode(@PathVariable String assessmentCode) {
        return assessmentRequirementService.getAssessmentRequirementsByAssessmentCode(assessmentCode).stream().map(mapper::toStructure).toList();
    }

    @GetMapping("/requirement/{programRequirementCode}")
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND."),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")
    })
    public List<AssessmentRequirement> getAssessmentRequirementsByProgramRequirementCode(@PathVariable String programRequirementCode) {
        return assessmentRequirementService.getAssessmentRequirementsByProgramRequirementCode(programRequirementCode).stream().map(mapper::toStructure).toList();
    }
} 