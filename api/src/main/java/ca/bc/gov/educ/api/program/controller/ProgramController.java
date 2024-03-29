package ca.bc.gov.educ.api.program.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.educ.api.program.model.dto.CareerProgram;
import ca.bc.gov.educ.api.program.model.dto.GradProgramAlgorithmData;
import ca.bc.gov.educ.api.program.model.dto.GradRuleDetails;
import ca.bc.gov.educ.api.program.model.dto.GraduationProgramCode;
import ca.bc.gov.educ.api.program.model.dto.OptionalProgram;
import ca.bc.gov.educ.api.program.model.dto.OptionalProgramRequirement;
import ca.bc.gov.educ.api.program.model.dto.OptionalProgramRequirementCode;
import ca.bc.gov.educ.api.program.model.dto.ProgramRequirement;
import ca.bc.gov.educ.api.program.model.dto.ProgramRequirementCode;
import ca.bc.gov.educ.api.program.model.dto.RequirementTypeCode;
import ca.bc.gov.educ.api.program.service.ProgramService;
import ca.bc.gov.educ.api.program.util.ApiResponseModel;
import ca.bc.gov.educ.api.program.util.EducGradProgramApiConstants;
import ca.bc.gov.educ.api.program.util.GradValidation;
import ca.bc.gov.educ.api.program.util.PermissionsContants;
import ca.bc.gov.educ.api.program.util.ResponseHelper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequestMapping(EducGradProgramApiConstants.GRAD_PROGRAM_API_ROOT_MAPPING)
@OpenAPIDefinition(info = @Info(title = "API for Program Management Data.", description = "This API contains endpoints for Program Management Functionalities.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_GRAD_PROGRAM_RULES_DATA","READ_GRAD_PROGRAM_CODE_DATA"})})
public class ProgramController {

    private static Logger logger = LoggerFactory.getLogger(ProgramController.class);

    @Autowired
    ProgramService programService;
    
    @Autowired
	GradValidation validation;
    
    @Autowired
	ResponseHelper response;
    
    private static final String PROGRAM_CODE="Program Code";
    private static final String PROGRAM_NAME="Program Name";
    private static final String OPTIONAL_PROGRAM_ID="Optional Program ID";
    private static final String RULE_CODE = "Rule Code";
    private static final String REQUIREMENT_TYPE_CODE="Requirement Type Code";

    @GetMapping(value=EducGradProgramApiConstants.GET_ALL_PROGRAM_MAPPING, produces= {"application/json"})
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM)
    @Operation(summary = "Find All Programs", description = "Get All Programs", tags = { "Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = GraduationProgramCode.class)))), @ApiResponse(responseCode = "204", description = "NO CONTENT.")})
    public ResponseEntity<List<GraduationProgramCode>> getAllPrograms() { 
    	logger.debug("getAllPrograms : ");
    	List<GraduationProgramCode> programList = programService.getAllProgramList();
    	if(!programList.isEmpty()) {
    		return response.GET(programList,new TypeToken<List<GraduationProgramCode>>() {}.getType());
    	}
    	return response.NO_CONTENT();
    }
    
    @GetMapping(value=EducGradProgramApiConstants.GET_PROGRAM_MAPPING, produces= {"application/json"})
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM)
    @Operation(summary = "Find Specific Program", description = "Get a Program", tags = { "Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = GraduationProgramCode.class)))), @ApiResponse(responseCode = "204", description = "NO CONTENT.")})
    public ResponseEntity<GraduationProgramCode> getSpecificProgram(@PathVariable String programCode) { 
    	logger.debug("getSpecificProgram : ");
    	GraduationProgramCode gradProgram = programService.getSpecificProgram(programCode);
    	if(gradProgram != null) {
    		return response.GET(gradProgram);
    	}
    	return response.NO_CONTENT();
    }
    
    @PostMapping(value=EducGradProgramApiConstants.GET_ALL_PROGRAM_MAPPING,produces= {"application/json"},consumes= {"application/json"})
    @PreAuthorize(PermissionsContants.CREATE_GRAD_PROGRAM)
    @Operation(summary = "Create a Program", description = "Create a Program", tags = { "Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST.")})
    public ResponseEntity<ApiResponseModel<GraduationProgramCode>> createGradPrograms(@Valid @RequestBody GraduationProgramCode gradProgram) { 
    	logger.debug("createGradPrograms : ");
    	validation.requiredField(gradProgram.getProgramCode(), PROGRAM_CODE);
       	validation.requiredField(gradProgram.getProgramName(), PROGRAM_NAME);
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
        return response.CREATED(programService.createGradProgram(gradProgram));
    }
    
    @PutMapping(value=EducGradProgramApiConstants.GET_ALL_PROGRAM_MAPPING ,produces= {"application/json"},consumes= {"application/json"})
    @PreAuthorize(PermissionsContants.UPDATE_GRAD_PROGRAM)
    @Operation(summary = "Update a Program", description = "Update a Program", tags = { "Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST.")})
    public ResponseEntity<ApiResponseModel<GraduationProgramCode>> updateGradPrograms(@Valid @RequestBody GraduationProgramCode gradProgram) { 
    	logger.debug("updateGradProgramsss : ");
    	validation.requiredField(gradProgram.getProgramCode(), PROGRAM_CODE);
      	validation.requiredField(gradProgram.getProgramName(), PROGRAM_NAME);
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
        return response.UPDATED(programService.updateGradProgram(gradProgram));
    }
    
    @DeleteMapping(EducGradProgramApiConstants.DELETE_PROGRAM_MAPPING)
    @PreAuthorize(PermissionsContants.DELETE_GRAD_PROGRAM)
    @Operation(summary = "Delete a Program", description = "Delete a Program", tags = { "Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND.")})
    public ResponseEntity<Void> deleteGradPrograms(@Valid @PathVariable String programCode) { 
    	logger.debug("deleteGradPrograms : ");
    	validation.requiredField(programCode, PROGRAM_CODE);
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
        return response.DELETE(programService.deleteGradPrograms(programCode));
    }
    
    @GetMapping(value=EducGradProgramApiConstants.GET_ALL_PROGRAM_RULES,produces= {"application/json"})
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM_RULES)
    @Operation(summary = "Get Program Rules", description = "Get Program Rules", tags = { "Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "204", description = "NO CONTENT")})
    public ResponseEntity<List<ProgramRequirement>> getAllProgramsRules(
    		@RequestParam(value = "programCode") String programCode) {
    	logger.debug("get All Program Rules : ");
    	List<ProgramRequirement> programRuleList = programService.getAllProgramRuleList(programCode);
    	if(!programRuleList.isEmpty()) {
    		return response.GET(programRuleList,new TypeToken<List<ProgramRequirement>>() {}.getType());
    	}
    	return response.NO_CONTENT();
    }
    
    @PostMapping(value=EducGradProgramApiConstants.GET_ALL_PROGRAM_RULES,produces= {"application/json"},consumes= {"application/json"})
    @PreAuthorize(PermissionsContants.CREATE_GRAD_PROGRAM_RULES)
    @Operation(summary = "Create Program Rules", description = "Create Program Rules", tags = { "Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<ApiResponseModel<ProgramRequirement>> createGradProgramRules(@Valid @RequestBody ProgramRequirement gradProgramRule) { 
    	logger.debug("createGradProgramRules : ");
    	validation.requiredField(gradProgramRule.getGraduationProgramCode(), PROGRAM_CODE);
    	validation.requiredField(gradProgramRule.getProgramRequirementCode().getProReqCode(), RULE_CODE);
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	return response.CREATED(programService.createGradProgramRules(gradProgramRule));
    }
    
    @PutMapping(value=EducGradProgramApiConstants.GET_ALL_PROGRAM_RULES,produces= {"application/json"},consumes= {"application/json"})
    @PreAuthorize(PermissionsContants.UPDATE_GRAD_PROGRAM_RULES)
    @Operation(summary = "Update a Program Rules", description = "Update a Program Rules", tags = { "Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST.")})
    public ResponseEntity<ApiResponseModel<ProgramRequirement>> updateGradProgramRules(@Valid @RequestBody ProgramRequirement gradProgramRule) { 
    	logger.debug("updateGradProgramRules : ");
    	validation.requiredField(gradProgramRule.getGraduationProgramCode(), PROGRAM_CODE);
    	validation.requiredField(gradProgramRule.getProgramRequirementCode().getProReqCode(), RULE_CODE);
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	return response.UPDATED(programService.updateGradProgramRules(gradProgramRule));
    }
    
    @DeleteMapping(EducGradProgramApiConstants.DELETE_PROGRAM_RULES_MAPPING)
    @PreAuthorize(PermissionsContants.DELETE_GRAD_PROGRAM_RULES)
    @Operation(summary = "Delete a Program Rule", description = "Delete a Program Rule", tags = { "Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND.")})
    public ResponseEntity<Void> deleteGradProgramRules(@PathVariable(value = "programRuleID") String programRuleID) {
    	logger.debug("deleteGradProgramRules : ");    	
        return response.DELETE(programService.deleteGradProgramRules(UUID.fromString(programRuleID)));
    }
    
    
    @GetMapping(EducGradProgramApiConstants.GET_ALL_SPECIFIC_PROGRAM_RULES_BY_RULE)
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM_RULES)
    @Operation(summary = "Find Specific Rule Details", description = "Get a Specific Rule Detail", tags = { "Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public List<GradRuleDetails> getSpecificRuleDetails(@PathVariable String ruleCode) { 
    	logger.debug("getSpecificRuleDetails : ");
        return programService.getSpecificRuleDetails(ruleCode);
    }

    @GetMapping(EducGradProgramApiConstants.GET_ALL_SPECIFIC_PROGRAM_RULES_BY_TRAX_SEQ_NUMBER)
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM_RULES)
    @Operation(summary = "Find Specific Rule Details by TraxReqNumber", description = "Get a Specific Rule Detail by TraxReqNumber", tags = { "Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public List<GradRuleDetails> getSpecificRuleDetailsByTraxReqNumber(@PathVariable String traxReqNumber) {
        logger.debug("getSpecificRuleDetails : ");
        return programService.getSpecificRuleDetailsByTraxReqNumber(traxReqNumber);
    }
    
    @GetMapping(EducGradProgramApiConstants.GET_ALL_OPTIONAL_PROGRAM_MAPPING_BY_ID)
    @PreAuthorize(PermissionsContants.READ_GRAD_OPTIONAL_PROGRAM)
    @Operation(summary = "Find Optional Program", description = "Get Optional Program By ID", tags = { "Optional Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<OptionalProgram> getOptionalProgramByID(@PathVariable String optionalProgramID) {
    	logger.debug("getAllOptionalProgramsByID : ");
        return response.GET(programService.getOptionalProgramByID(UUID.fromString(optionalProgramID)));
    }
    
    @GetMapping(value=EducGradProgramApiConstants.GET_ALL_OPTIONAL_PROGRAM_MAPPING, produces= {"application/json"})
    @PreAuthorize(PermissionsContants.READ_GRAD_OPTIONAL_PROGRAM)
    @Operation(summary = "Find All Optional Programs", description = "Get All Optional Programs", tags = { "Optional Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "204", description = "NO CONTENT.")})
    public ResponseEntity<List<OptionalProgram>> getAllOptionalPrograms() { 
    	logger.debug("getAllOptionalPrograms : ");
    	List<OptionalProgram> optionalProgramList = programService.getAllOptionalProgramList();
    	if(!optionalProgramList.isEmpty()) {
    		return response.GET(optionalProgramList,new TypeToken<List<OptionalProgram>>() {}.getType());
    	}
    	return response.NO_CONTENT();
    }
   
    
    @PostMapping(EducGradProgramApiConstants.GET_ALL_OPTIONAL_PROGRAM_MAPPING)
    @PreAuthorize(PermissionsContants.CREATE_GRAD_OPTIONAL_PROGRAM)
    @Operation(summary = "Create Optional Program", description = "Create Optional Program", tags = { "Optional Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<ApiResponseModel<OptionalProgram>> createGradOptionalPrograms(@Valid @RequestBody OptionalProgram optionalProgram) { 
    	logger.debug("createGradOptionalPrograms : ");
    	validation.requiredField(optionalProgram.getGraduationProgramCode(), PROGRAM_CODE);
       	validation.requiredField(optionalProgram.getOptProgramCode(), "Optional Program Code");
       	validation.requiredField(optionalProgram.getOptionalProgramName(), "Optional Program Name");
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
        return response.CREATED(programService.createGradOptionalProgram(optionalProgram));
    }
    
    @PutMapping(EducGradProgramApiConstants.GET_ALL_OPTIONAL_PROGRAM_MAPPING)
    @PreAuthorize(PermissionsContants.UPDATE_GRAD_OPTIONAL_PROGRAM)
    @Operation(summary = "Update Optional Program", description = "Update Optional Program", tags = { "Optional Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<ApiResponseModel<OptionalProgram>> updateGradOptionalPrograms(@Valid @RequestBody OptionalProgram optionalProgram) { 
    	logger.debug("updateGradOptionalPrograms : ");
    	validation.requiredField(optionalProgram.getGraduationProgramCode(), PROGRAM_CODE);
       	validation.requiredField(optionalProgram.getOptProgramCode(), "Optional Program Code");
       	validation.requiredField(optionalProgram.getOptionalProgramName(), "Optional Program Name");
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
        return response.UPDATED(programService.updateGradOptionalPrograms(optionalProgram));
    }
    
    @DeleteMapping(EducGradProgramApiConstants.DELETE_OPTIONAL_PROGRAM_MAPPING)
    @PreAuthorize(PermissionsContants.DELETE_GRAD_OPTIONAL_PROGRAM)
    @Operation(summary = "Delete Optional Program", description = "Delete Optional Program", tags = { "Optional Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<Void> deleteGradOptionalPrograms(@PathVariable(value = "optionalProgramID") String optionalProgramID) {
    	logger.debug("deleteGradPrograms : ");
    	validation.requiredField(optionalProgramID, OPTIONAL_PROGRAM_ID);
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
        return response.DELETE(programService.deleteGradOptionalPrograms(UUID.fromString(optionalProgramID)));
    }
    
    @PostMapping(EducGradProgramApiConstants.GET_ALL_OPTIONAL_PROGRAM_RULES)
    @PreAuthorize(PermissionsContants.CREATE_GRAD_OPTIONAL_PROGRAM_RULES)
    @Operation(summary = "Create Optional Program Rules", description = "Create Optional Program Rules", tags = { "Optional Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<ApiResponseModel<OptionalProgramRequirement>> createGradOptionalProgramRules(@Valid @RequestBody OptionalProgramRequirement optionalProgramRequirement) { 
    	logger.debug("createGradOptionalProgramRules : ");
    	validation.requiredField(optionalProgramRequirement.getOptionalProgramID(), OPTIONAL_PROGRAM_ID);
    	validation.requiredField(optionalProgramRequirement.getOptionalProgramRequirementCode().getOptProReqCode(), RULE_CODE);
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	return response.CREATED(programService.createGradOptionalProgramRules(optionalProgramRequirement));
    }
    
    @PutMapping(EducGradProgramApiConstants.GET_ALL_OPTIONAL_PROGRAM_RULES)
    @PreAuthorize(PermissionsContants.UPDATE_GRAD_OPTIONAL_PROGRAM_RULES)
    @Operation(summary = "Update Optional Program Rules", description = "Update Optional Program Rules", tags = { "Optional Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<ApiResponseModel<OptionalProgramRequirement>> updateGradOptionalProgramRules(@Valid @RequestBody OptionalProgramRequirement optionalProgramRequirement) { 
    	logger.debug("updateGradProgramRules : ");
    	validation.requiredField(optionalProgramRequirement.getOptionalProgramID(), OPTIONAL_PROGRAM_ID);
    	validation.requiredField(optionalProgramRequirement.getOptionalProgramRequirementCode().getOptProReqCode(), RULE_CODE);
    	if(validation.hasErrors()) {
    		validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	return response.UPDATED(programService.updateGradOptionalProgramRules(optionalProgramRequirement));
    }
    
    @DeleteMapping(EducGradProgramApiConstants.DELETE_OPTIONAL_PROGRAM_RULES_MAPPING)
    @PreAuthorize(PermissionsContants.DELETE_GRAD_OPTIONAL_PROGRAM_RULES)
    @Operation(summary = "Delete Optional Program Rule", description = "Delete Optional Program Rule", tags = { "Optional Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<Void> deleteGradOptionalProgramRules(@PathVariable(value = "programRuleID") String programRuleID) {
    	logger.debug("deleteGradProgramRules : ");    	
        return response.DELETE(programService.deleteGradOptionalProgramRules(UUID.fromString(programRuleID)));
    }
    
    @GetMapping(EducGradProgramApiConstants.GET_OPTIONAL_PROGRAM_RULES_BY_PROGRAM_CODE_AND_OPTIONAL_PROGRAM_CODE_ONLY)
    @PreAuthorize(PermissionsContants.READ_GRAD_OPTIONAL_PROGRAM_RULES)
    @Operation(summary = "Find Optional Program Rules by Program Code and Optional Program Code", description = "Get Optional Program Rules by Program Code and Optional Program Code", tags = { "Optional Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "204", description = "NO CONTENT")})
    public ResponseEntity<List<OptionalProgramRequirement>> getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(
    		@PathVariable(value = "programCode") String programCode,
    		@PathVariable(value = "optionalProgramCode") String optionalProgramCode) {
    	logger.debug("get Optional Program Rules By Program Code And Optional Program Code : ");
    	List<OptionalProgramRequirement> programRuleList = programService.getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode,optionalProgramCode);
    	if(!programRuleList.isEmpty()) {
    		return response.GET(programRuleList,new TypeToken<List<OptionalProgramRequirement>>() {}.getType());
    	}
    	return response.NO_CONTENT();
    }
    
    @GetMapping(value=EducGradProgramApiConstants.GET_PROGRAM_RULES,produces= {"application/json"})
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM_RULES)
    @Operation(summary = "Get all Program Rules", description = "Get all Program Rules", tags = { "Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "204", description = "NO CONTENT")})
    public ResponseEntity<List<ProgramRequirement>> getAllProgramsRules() { 
    	logger.debug("get All Program Rules : ");
    	List<ProgramRequirement> programRuleList = programService.getAllProgramRulesList();
    	if(!programRuleList.isEmpty()) {
    		return response.GET(programRuleList,new TypeToken<List<ProgramRequirement>>() {}.getType());
    	}
    	return response.NO_CONTENT();
    }
    
    
    @GetMapping(EducGradProgramApiConstants.GET_OPTIONAL_PROGRAM_RULES)
    @PreAuthorize(PermissionsContants.READ_GRAD_OPTIONAL_PROGRAM_RULES)
    @Operation(summary = "Get all Optional Program Rules", description = "Get all Optional Program Rules", tags = { "Optional Program Rules" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "204", description = "NO CONTENT")})
    public ResponseEntity<List<OptionalProgramRequirement>> getAllOptionalProgramRules() { 
    	logger.debug("get All Optional Program Rules : ");
    	List<OptionalProgramRequirement> programRuleList = programService.getAllOptionalProgramRulesList();
    	if(!programRuleList.isEmpty()) {
    		return response.GET(programRuleList,new TypeToken<List<OptionalProgramRequirement>>() {}.getType());
    	}
    	return response.NO_CONTENT();
    }
    
    @GetMapping(EducGradProgramApiConstants.GET_ALL_OPTIONAL_PROGRAM_BY_PROGRAM_CODE_AND_OPTIONAL_PROGRAM_MAPPING)
    @PreAuthorize(PermissionsContants.READ_GRAD_OPTIONAL_PROGRAM)
    @Operation(summary = "Find a Optional Programs by Program and Optional program", description = "Get a Optional Programs by Program Code and Optional program code", tags = { "Optional Programs" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<OptionalProgram> getOptionalPrograms(@PathVariable String programCode,@PathVariable String optionalProgramCode) { 
    	logger.debug("getOptionalPrograms : ");
        return response.GET(programService.getOptionalProgram(programCode,optionalProgramCode));
    }
    
    @GetMapping(value=EducGradProgramApiConstants.GET_ALL_PROGRAM_REQUIREMENT_CODES_MAPPING, produces= {"application/json"})
    @PreAuthorize(PermissionsContants.READ_GRAD_PROGRAM_RULES)
    @Operation(summary = "Find All Program Requirement Code", description = "Get All Program Requirement Code", tags = { "Program Requirement Codes" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = GraduationProgramCode.class)))), @ApiResponse(responseCode = "204", description = "NO CONTENT.")})
    public ResponseEntity<List<ProgramRequirementCode>> getAllProgramRequirementCode() { 
    	logger.debug("getAllProgramRequirementCode : ");
    	List<ProgramRequirementCode> programReqCodeList = programService.getAllProgramRequirementCodeList();
    	if(!programReqCodeList.isEmpty()) {
    		return response.GET(programReqCodeList,new TypeToken<List<ProgramRequirementCode>>() {}.getType());
    	}
    	return response.NO_CONTENT();
    }
    
    @GetMapping(value=EducGradProgramApiConstants.GET_ALL_OPTIONAL_PROGRAM_REQUIREMENT_CODES_MAPPING, produces= {"application/json"})
    @PreAuthorize(PermissionsContants.READ_GRAD_OPTIONAL_PROGRAM_RULES)
    @Operation(summary = "Find All Program Requirement Code", description = "Get All Program Requirement Code", tags = { "Program Requirement Codes" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = GraduationProgramCode.class)))), @ApiResponse(responseCode = "204", description = "NO CONTENT.")})
    public ResponseEntity<List<OptionalProgramRequirementCode>> getAllOptionalProgramRequirementCode() { 
    	logger.debug("getAllOptionalProgramRequirementCode : ");
    	List<OptionalProgramRequirementCode> programReqCodeList = programService.getAllOptionalProgramRequirementCodeList();
    	if(!programReqCodeList.isEmpty()) {
    		return response.GET(programReqCodeList,new TypeToken<List<OptionalProgramRequirementCode>>() {}.getType());
    	}
    	return response.NO_CONTENT();
    }
    
    @GetMapping(EducGradProgramApiConstants.GET_DATA_FOR_ALGORITHM_MAPPING)
    @PreAuthorize(PermissionsContants.READ_ALGORITHM_DATA)
    @Operation(summary = "Read All  Data required by Grad Algorithm", description = "Read All  Data required by Grad Algorithm", tags = { "Algorithm" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<GradProgramAlgorithmData> getAllAlgorithmData(@RequestParam(value = "programCode") String programCode,@RequestParam(value = "optionalProgramCode", required = false) String optionalProgramCode) {
    	logger.debug("getAllAlgorithmData : ");
        return response.GET(programService.getAllAlgorithmData(programCode,optionalProgramCode,null));
    }
    
    @GetMapping(EducGradProgramApiConstants.GET_ALL_GRAD_CAREER_PROGRAM_MAPPING)
    @PreAuthorize(PermissionsContants.READ_GRAD_CAREER_PROGRAM)
    @Operation(summary = "Find all Career Program", description = "Get all Career Program", tags = {"Career Program"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<CareerProgram>> getAllCareerPrograms() {
        logger.debug("getAllPrograms : ");
        return response.GET(programService.getAllCareerProgramCodeList());
    }

    @GetMapping(EducGradProgramApiConstants.GET_ALL_GRAD_CAREER_PROGRAM_BY_CODE_MAPPING)
    @PreAuthorize(PermissionsContants.READ_GRAD_CAREER_PROGRAM)
    @Operation(summary = "Find a Career Program", description = "Get a Career Program", tags = {"Career Program"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "NO CONTENT")})
    public ResponseEntity<CareerProgram> getSpecificCareerProgramCode(@PathVariable String cpCode) {
        logger.debug("getSpecificCareerProgramCode : ");
        CareerProgram gradResponse = programService.getSpecificCareerProgramCode(cpCode);
        if (gradResponse != null) {
            return response.GET(gradResponse);
        } else {
            return response.NO_CONTENT();
        }
    }
    
    @GetMapping(EducGradProgramApiConstants.GET_ALL_GRAD_REQUIREMENT_TYPE_CODE_MAPPING)
    @PreAuthorize(PermissionsContants.READ_GRAD_REQUIREMENT_TYPE_CODE)
    @Operation(summary = "Find all Requirement Types", description = "Get all Requirement Types", tags = {"Requirement Type"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<RequirementTypeCode>> getAllRequirementTypeCodeList() {
        logger.debug("getAllRequirementTypeCodeList : ");
        return response.GET(programService.getAllRequirementTypeCodeList());
    }

    @GetMapping(EducGradProgramApiConstants.GET_ALL_GRAD_REQUIREMENT_TYPE_CODE_BY_CODE_MAPPING)
    @PreAuthorize(PermissionsContants.READ_GRAD_REQUIREMENT_TYPE_CODE)
    @Operation(summary = "Find a Requirement Type", description = "Get a Requirement Type", tags = {"Requirement Type"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "NO CONTENT")})
    public ResponseEntity<RequirementTypeCode> getSpecificRequirementTypeCode(@PathVariable String typeCode) {
        logger.debug("getSpecificRequirementTypeCode : ");
        RequirementTypeCode gradResponse = programService.getSpecificRequirementTypeCode(typeCode);
        if (gradResponse != null) {
            return response.GET(gradResponse);
        } else {
            return response.NO_CONTENT();
        }
    }

    @PostMapping(EducGradProgramApiConstants.GET_ALL_GRAD_REQUIREMENT_TYPE_CODE_MAPPING)
    @PreAuthorize(PermissionsContants.CREATE_REQUIREMENT_TYPE)
    @Operation(summary = "Create a Requirement Type", description = "Create a Requirement Type", tags = {"Requirement Type"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<ApiResponseModel<RequirementTypeCode>> createRequirementTypeCode(
            @Valid @RequestBody RequirementTypeCode requirementTypeCode) {
        logger.debug("creatRequirementTypeCodes : ");
        validation.requiredField(requirementTypeCode.getReqTypeCode(), REQUIREMENT_TYPE_CODE);
        validation.requiredField(requirementTypeCode.getDescription(), "Requirement Type Description");
        if (validation.hasErrors()) {
            validation.stopOnErrors();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response.CREATED(programService.createRequirementTypeCode(requirementTypeCode));
    }

    @PutMapping(EducGradProgramApiConstants.GET_ALL_GRAD_REQUIREMENT_TYPE_CODE_MAPPING)
    @PreAuthorize(PermissionsContants.UPDATE_REQUIREMENT_TYPE)
    @Operation(summary = "Update a Requirement Type", description = "Update a Requirement Type", tags = {"Requirement Type"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<ApiResponseModel<RequirementTypeCode>> updateRequirementTypeCode(
            @Valid @RequestBody RequirementTypeCode requirementTypeCode) {
        logger.debug("updateRequirementTypeCode : ");
        validation.requiredField(requirementTypeCode.getReqTypeCode(), REQUIREMENT_TYPE_CODE);
        validation.requiredField(requirementTypeCode.getDescription(), "Requirement Type Description");
        if (validation.hasErrors()) {
            validation.stopOnErrors();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response.UPDATED(programService.updateRequirementTypeCode(requirementTypeCode));
    }

    @DeleteMapping(EducGradProgramApiConstants.GET_ALL_GRAD_REQUIREMENT_TYPE_CODE_BY_CODE_MAPPING)
    @PreAuthorize(PermissionsContants.DELETE_REQUIREMENT_TYPE)
    @Operation(summary = "Delete a Requirement Type", description = "Delete a Requirement Type", tags = {"Requirement Type"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<Void> deleteRequirementTypeCode(@Valid @PathVariable String typeCode) {
        logger.debug("deleteRequirementTypeCode : ");
        validation.requiredField(typeCode, REQUIREMENT_TYPE_CODE);
        if (validation.hasErrors()) {
            validation.stopOnErrors();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response.DELETE(programService.deleteRequirementTypeCode(typeCode));
    }

    @GetMapping(EducGradProgramApiConstants.GET_DATA_FOR_ALGORITHM_LIST_MAPPING)
    @PreAuthorize(PermissionsContants.READ_ALGORITHM_DATA)
    @Operation(summary = "Read All  Data required by Grad Algorithm", description = "Read All  Data required by Grad Algorithm", tags = { "Algorithm" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<GradProgramAlgorithmData>> getAllAlgorithmDataList() {
        logger.debug("getAllAlgorithmDataList : ");
        return response.GET(programService.getAllAlgorithmDataList());
    }
    
}