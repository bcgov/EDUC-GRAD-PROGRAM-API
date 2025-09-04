package ca.bc.gov.educ.api.program.controller.v1;

import ca.bc.gov.educ.api.program.service.v1.AssessmentRequirementService;
import ca.bc.gov.educ.api.program.model.dto.external.algorithm.AssessmentRequirement;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/program/assessment-requirements")
public class AssessmentRequirementController {

    @Getter(AccessLevel.PRIVATE)
    private final AssessmentRequirementService assessmentRequirementService;

    @GetMapping
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")
    })
    public List<AssessmentRequirement> getAllAssessmentRequirements() {
        return getAssessmentRequirementService().getAllAssessmentRequirements();
    }

} 