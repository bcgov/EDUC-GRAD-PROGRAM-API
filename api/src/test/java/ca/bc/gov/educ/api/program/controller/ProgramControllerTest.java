package ca.bc.gov.educ.api.program.controller;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

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
import ca.bc.gov.educ.api.program.util.GradValidation;
import ca.bc.gov.educ.api.program.util.MessageHelper;
import ca.bc.gov.educ.api.program.util.ResponseHelper;


@ExtendWith(MockitoExtension.class)
public class ProgramControllerTest {

	@Mock
	private ProgramService programService;
	
	@Mock
	ResponseHelper response;
	
	@InjectMocks
	private ProgramController programController;
	
	@Mock
	GradValidation validation;
	
	@Mock
	MessageHelper messagesHelper;
	
	@Mock
	ResponseEntity<List<GraduationProgramCode>> ent;
	
	@Test
	public void testGetAllProgramList() {
		List<GraduationProgramCode> gradProgramList = new ArrayList<>();
		GraduationProgramCode obj = new GraduationProgramCode();
		obj.setProgramCode("AB");
		obj.setProgramName("Autobody");
		gradProgramList.add(obj);
		obj = new GraduationProgramCode();
		obj.setProgramCode("AC");
		obj.setProgramName("Autobody");
		gradProgramList.add(obj);
		
		Mockito.when(programService.getAllProgramList()).thenReturn(gradProgramList);
		programController.getAllPrograms();
		Mockito.verify(programService).getAllProgramList();
	}
	
	@Test
	public void testGetAllProgramList_emptyList() {		
		Mockito.when(programService.getAllProgramList()).thenReturn(new ArrayList<>());
		ResponseEntity<List<GraduationProgramCode>> programList = programController.getAllPrograms();
		Mockito.verify(programService).getAllProgramList();
		assertNull(programList);
	}
	
	@Test
	public void testGetSpecificProgram() {
		String programCode="AB";
		GraduationProgramCode obj = new GraduationProgramCode();
		obj.setProgramCode("AB");
		obj.setProgramName("Autobody");
		Mockito.when(programService.getSpecificProgram(programCode)).thenReturn(obj);
		programController.getSpecificProgram(programCode);
		Mockito.verify(programService).getSpecificProgram(programCode);
	}
	
	@Test
	public void testGetSpecificProgram_noObject() {
		String programCode="AB";
		Mockito.when(programService.getSpecificProgram(programCode)).thenReturn(null);
		ResponseEntity<GraduationProgramCode> obj = programController.getSpecificProgram(programCode);
		Mockito.verify(programService).getSpecificProgram(programCode);
		assertNull(obj);
	}
	
	@Test
	public void testdeleteGradPrograms() {
		String programCode = "DC";
		Mockito.when(programService.deleteGradPrograms(programCode)).thenReturn(1);
		programController.deleteGradPrograms(programCode);
		Mockito.verify(programService).deleteGradPrograms(programCode);
	}
	
	@Test
	public void testCreateGradPrograms() {
		GraduationProgramCode obj = new GraduationProgramCode();
		obj.setProgramCode("AB");
		obj.setProgramName("Autobody");
		Mockito.when(programService.createGradProgram(obj)).thenReturn(obj);
		programController.createGradPrograms(obj);
		Mockito.verify(programService).createGradProgram(obj);
	}
	
	@Test
	public void testCreateGradPrograms_error() {
		GraduationProgramCode obj = new GraduationProgramCode();
		obj.setProgramName("Autobody");
		Mockito.when(programService.createGradProgram(obj)).thenReturn(obj);
		programController.createGradPrograms(obj);
		Mockito.verify(programService).createGradProgram(obj);
	}
	
	@Test
	public void testUpdateGradPrograms() {
		GraduationProgramCode obj = new GraduationProgramCode();
		obj.setProgramCode("AB");
		obj.setProgramName("Autobody");
		Mockito.when(programService.updateGradProgram(obj)).thenReturn(obj);
		programController.updateGradPrograms(obj);
		Mockito.verify(programService).updateGradProgram(obj);
	}
	
	@Test
	public void testUpdateGradPrograms_error() {
		GraduationProgramCode obj = new GraduationProgramCode();
		obj.setProgramName("Autobody");
		Mockito.when(programService.updateGradProgram(obj)).thenReturn(obj);
		programController.updateGradPrograms(obj);
		Mockito.verify(programService).updateGradProgram(obj);
	}
	
	@Test
	public void testCreateGradProgramRules() {		
		ProgramRequirement obj = new ProgramRequirement();
		obj.setGraduationProgramCode("AB");
		ProgramRequirementCode code = new ProgramRequirementCode();
		code.setProReqCode("100");
		obj.setProgramRequirementCode(code);
		Mockito.when(programService.createGradProgramRules(obj)).thenReturn(obj);
		programController.createGradProgramRules(obj);
		Mockito.verify(programService).createGradProgramRules(obj);
	}
	
	@Test
	public void testCreateGradProgramRules_error() { 
		ProgramRequirement obj = new ProgramRequirement();
		obj.setGraduationProgramCode("AB");
		ProgramRequirementCode code = new ProgramRequirementCode();
		code.setProReqCode("100");
		obj.setProgramRequirementCode(code);
		Mockito.when(programService.createGradProgramRules(obj)).thenReturn(obj);
		programController.createGradProgramRules(obj);
		Mockito.verify(programService).createGradProgramRules(obj);
	}
	
	@Test
	public void testUpdateGradProgramRules() {
		
		ProgramRequirement obj = new ProgramRequirement();
		obj.setGraduationProgramCode("AB");
		ProgramRequirementCode code = new ProgramRequirementCode();
		code.setProReqCode("100");
		obj.setProgramRequirementCode(code);
		Mockito.when(programService.updateGradProgramRules(obj)).thenReturn(obj);
		programController.updateGradProgramRules(obj);
		Mockito.verify(programService).updateGradProgramRules(obj);
	}
	
	@Test
	public void testUpdateGradProgramRules_error() {		
		ProgramRequirement obj = new ProgramRequirement();
		obj.setGraduationProgramCode("AB");
		ProgramRequirementCode code = new ProgramRequirementCode();
		code.setProReqCode("100");
		obj.setProgramRequirementCode(code);
		Mockito.when(programService.updateGradProgramRules(obj)).thenReturn(obj);
		programController.updateGradProgramRules(obj);
		Mockito.verify(programService).updateGradProgramRules(obj);
	}
	
	@Test
	public void testDeleteGradProgramRules() {
		UUID ruleID = new UUID(1, 1);
		Mockito.when(programService.deleteGradProgramRules(ruleID)).thenReturn(1);
		programController.deleteGradProgramRules(ruleID.toString());
		Mockito.verify(programService).deleteGradProgramRules(ruleID);
		
	}	
	
	
	@Test
	public void testGetSpecificRuleDetails() {
		String ruleCode="200";
		List<GradRuleDetails> gradRuleDetails = new ArrayList<>();
		GradRuleDetails ruleEntity = new GradRuleDetails();
        ruleEntity.setProgramCode("2018-EN");
        ruleEntity.setRequirementName("Match");
        ruleEntity.setRuleCode("200");
        ruleEntity.setOptionalProgramCode("FI");
        gradRuleDetails.add(ruleEntity);
        GradRuleDetails ruleEntity2 = new GradRuleDetails();
        ruleEntity.setProgramCode("2018-EN");
        ruleEntity.setRequirementName("Match");
        ruleEntity.setRuleCode("200");
        ruleEntity.setOptionalProgramCode("FI");
        gradRuleDetails.add(ruleEntity2);
        
        Mockito.when(programService.getSpecificRuleDetails(ruleCode)).thenReturn(gradRuleDetails);
        programController.getSpecificRuleDetails(ruleCode);
		Mockito.verify(programService).getSpecificRuleDetails(ruleCode);
	}

	@Test
	public void testGetSpecificRuleDetailsByTraxReqNumber() {
		String traxReqNumber="5";
		List<GradRuleDetails> gradRuleDetails = new ArrayList<>();
		GradRuleDetails ruleEntity = new GradRuleDetails();
		ruleEntity.setProgramCode("2018-EN");
		ruleEntity.setRequirementName("Match");
		ruleEntity.setRuleCode("200");
		ruleEntity.setTraxReqNumber(traxReqNumber);
		ruleEntity.setOptionalProgramCode("FI");
		gradRuleDetails.add(ruleEntity);
		GradRuleDetails ruleEntity2 = new GradRuleDetails();
		ruleEntity2.setProgramCode("2018-EN");
		ruleEntity2.setRequirementName("Match");
		ruleEntity2.setRuleCode("200");
		ruleEntity2.setTraxReqNumber(traxReqNumber);
		ruleEntity2.setOptionalProgramCode("FI");
		gradRuleDetails.add(ruleEntity2);

		Mockito.when(programService.getSpecificRuleDetailsByTraxReqNumber(traxReqNumber)).thenReturn(gradRuleDetails);
		programController.getSpecificRuleDetailsByTraxReqNumber(traxReqNumber);
		Mockito.verify(programService).getSpecificRuleDetailsByTraxReqNumber(traxReqNumber);
	}

	@Test
	public void testGetOptionalProgramsByID() {
		String optionalProgramID=new UUID(1, 1).toString();
		OptionalProgram optionalProgram = new OptionalProgram();
		optionalProgram.setGraduationProgramCode("ABCD");
		optionalProgram.setOptionalProgramID(new UUID(1, 1));
		optionalProgram.setOptProgramCode("FI");
		optionalProgram.setOptionalProgramName("EFGH");
		Mockito.when(programService.getOptionalProgramByID(UUID.fromString(optionalProgramID))).thenReturn(optionalProgram);
		programController.getOptionalProgramByID(optionalProgramID);
		Mockito.verify(programService).getOptionalProgramByID(UUID.fromString(optionalProgramID));
	}
	
	@Test
	public void testGetAllOptionalPrograms() {
		List<OptionalProgram> list = new ArrayList<OptionalProgram>();
		OptionalProgram optionalProgram = new OptionalProgram();
		optionalProgram.setGraduationProgramCode("ABCD");
		optionalProgram.setOptionalProgramID(new UUID(1, 1));
		optionalProgram.setOptProgramCode("FI");
		optionalProgram.setOptionalProgramName("EFGH");
		list.add(optionalProgram);
		optionalProgram = new OptionalProgram();
		optionalProgram.setGraduationProgramCode("ABCD");
		optionalProgram.setOptionalProgramID(new UUID(1, 1));
		optionalProgram.setOptProgramCode("FI");
		optionalProgram.setOptionalProgramName("EFGH");
		list.add(optionalProgram);
		Mockito.when(programService.getAllOptionalProgramList()).thenReturn(list);
		programController.getAllOptionalPrograms();
		Mockito.verify(programService).getAllOptionalProgramList();
	}
	
	
	@Test
	public void testGetAllOptionalPrograms_emptyList() {
		Mockito.when(programService.getAllOptionalProgramList()).thenReturn(new ArrayList<>());
		programController.getAllOptionalPrograms();
		Mockito.verify(programService).getAllOptionalProgramList();
	}
	
		
	@Test
	public void testetOptionalProgramRulesByProgramCodeAndOptionalProgramCode() {
		String programCode = "2018-EN";
		String optionalProgramCode="FI";
		OptionalProgram optionalProgram = new OptionalProgram();
		optionalProgram.setGraduationProgramCode("ABCD");
		optionalProgram.setOptionalProgramID(new UUID(1, 1));
		optionalProgram.setOptProgramCode("FI");
		optionalProgram.setOptionalProgramName("EFGH");
		Mockito.when(programService.getOptionalProgram(programCode,optionalProgramCode)).thenReturn(optionalProgram);
		programController.getOptionalPrograms(programCode,optionalProgramCode);
		Mockito.verify(programService).getOptionalProgram(programCode,optionalProgramCode);
	}
	
	@Test
	public void testCreateGradOptionalPrograms() {
		OptionalProgram obj = new OptionalProgram();
		obj.setGraduationProgramCode("AB");
		obj.setOptionalProgramID(new UUID(1, 1));
		obj.setOptProgramCode("FI");
		obj.setOptionalProgramName("French Immersion");
		Mockito.when(programService.createGradOptionalProgram(obj)).thenReturn(obj);
		programController.createGradOptionalPrograms(obj);
		Mockito.verify(programService).createGradOptionalProgram(obj);
	}
	
	@Test
	public void testCreateGradOptionalPrograms_error() {
		OptionalProgram obj = new OptionalProgram();
		obj.setGraduationProgramCode("AB");
		obj.setOptionalProgramID(new UUID(1, 1));
		obj.setOptionalProgramName("French Immersion");
		Mockito.when(programService.createGradOptionalProgram(obj)).thenReturn(obj);
		programController.createGradOptionalPrograms(obj);
		Mockito.verify(programService).createGradOptionalProgram(obj);
	}
	
	@Test
	public void testUpdateGradOptionalPrograms() {
		OptionalProgram obj = new OptionalProgram();
		obj.setGraduationProgramCode("AB");
		obj.setOptionalProgramID(new UUID(1, 1));
		obj.setOptProgramCode("FI");
		obj.setOptionalProgramName("French Immersion");
		Mockito.when(programService.updateGradOptionalPrograms(obj)).thenReturn(obj);
		programController.updateGradOptionalPrograms(obj);
		Mockito.verify(programService).updateGradOptionalPrograms(obj);
	}
	
	@Test
	public void testUpdateGradOptionalPrograms_error() {
		OptionalProgram obj = new OptionalProgram();
		obj.setGraduationProgramCode("AB");
		obj.setOptionalProgramID(new UUID(1, 1));
		obj.setOptionalProgramName("French Immersion");
		Mockito.when(programService.updateGradOptionalPrograms(obj)).thenReturn(obj);
		programController.updateGradOptionalPrograms(obj);
		Mockito.verify(programService).updateGradOptionalPrograms(obj);
	}
	
	@Test
	public void testDeleteOptionalPrograms() {
		String optionalProgramID = new UUID(1, 1).toString();
		Mockito.when(programService.deleteGradOptionalPrograms(UUID.fromString(optionalProgramID))).thenReturn(1);
		programController.deleteGradOptionalPrograms(optionalProgramID);
		Mockito.verify(programService).deleteGradOptionalPrograms(UUID.fromString(optionalProgramID));
	}
	
	
	@Test
	public void testCreateGradOptionalProgramRules() {		
		OptionalProgramRequirement obj = new OptionalProgramRequirement();
		obj.setOptionalProgramRequirementID(new UUID(1, 1));
		OptionalProgramRequirementCode code = new OptionalProgramRequirementCode();
		code.setOptProReqCode("100");
		obj.setOptionalProgramRequirementCode(code);
		Mockito.when(programService.createGradOptionalProgramRules(obj)).thenReturn(obj);
		programController.createGradOptionalProgramRules(obj);
		Mockito.verify(programService).createGradOptionalProgramRules(obj);
	}
	
	@Test
	public void testCreateGradOptionalProgramRules_error() {		
		OptionalProgramRequirement obj = new OptionalProgramRequirement();
		obj.setOptionalProgramRequirementID(new UUID(1, 1));
		OptionalProgramRequirementCode code = new OptionalProgramRequirementCode();
		code.setOptProReqCode("100");
		obj.setOptionalProgramRequirementCode(code);
		Mockito.when(programService.createGradOptionalProgramRules(obj)).thenReturn(obj);
		programController.createGradOptionalProgramRules(obj);
		Mockito.verify(programService).createGradOptionalProgramRules(obj);
	}
	
	@Test
	public void testUpdateGradOptionalProgramRules() {
		OptionalProgramRequirement obj = new OptionalProgramRequirement();
		obj.setOptionalProgramRequirementID(new UUID(1, 1));
		OptionalProgramRequirementCode code = new OptionalProgramRequirementCode();
		code.setOptProReqCode("100");
		obj.setOptionalProgramRequirementCode(code);
		Mockito.when(programService.updateGradOptionalProgramRules(obj)).thenReturn(obj);
		programController.updateGradOptionalProgramRules(obj);
		Mockito.verify(programService).updateGradOptionalProgramRules(obj);
	}
	
	@Test
	public void testUpdateGradOptionalProgramRules_error() {
		OptionalProgramRequirement obj = new OptionalProgramRequirement();
		obj.setOptionalProgramRequirementID(new UUID(1, 1));
		OptionalProgramRequirementCode code = new OptionalProgramRequirementCode();
		code.setOptProReqCode("101");
		obj.setOptionalProgramRequirementCode(code);
		Mockito.when(programService.updateGradOptionalProgramRules(obj)).thenReturn(obj);
		programController.updateGradOptionalProgramRules(obj);
		Mockito.verify(programService).updateGradOptionalProgramRules(obj);
	}
	
	@Test
	public void testDeleteGradOptionalProgramRules() {
		UUID ruleID = new UUID(1, 1);
		Mockito.when(programService.deleteGradOptionalProgramRules(ruleID)).thenReturn(1);
		programController.deleteGradOptionalProgramRules(ruleID.toString());
		Mockito.verify(programService).deleteGradOptionalProgramRules(ruleID);
	}
	
	@Test
	public void testGetAllOptionalProgramRules() {
		List<OptionalProgramRequirement> list = new ArrayList<>();
		OptionalProgramRequirement obj = new OptionalProgramRequirement();
		obj.setOptionalProgramRequirementID(new UUID(1, 1));
		OptionalProgramRequirementCode code = new OptionalProgramRequirementCode();
		code.setOptProReqCode("101");
		obj.setOptionalProgramRequirementCode(code);
    	list.add(obj);    	
		
		Mockito.when(programService.getAllOptionalProgramRulesList()).thenReturn(list);
		programController.getAllOptionalProgramRules();
		Mockito.verify(programService).getAllOptionalProgramRulesList();
	}
	
	@Test
	public void testGetAllOptionalProgramRules_emptyList() {		
		Mockito.when(programService.getAllOptionalProgramRulesList()).thenReturn(new ArrayList<>());
		programController.getAllOptionalProgramRules();
		Mockito.verify(programService).getAllOptionalProgramRulesList();
	}
	
	@Test
	public void getAllProgramsRules() {
		List<ProgramRequirement> list = new ArrayList<>();
		ProgramRequirement gradProgramRule = new ProgramRequirement();
    	gradProgramRule.setGraduationProgramCode("2018-EN");
    	ProgramRequirementCode code = new ProgramRequirementCode();
		code.setProReqCode("100");
		gradProgramRule.setProgramRequirementCode(code);
    	list.add(gradProgramRule);
		
		Mockito.when(programService.getAllProgramRulesList()).thenReturn(list);
		programController.getAllProgramsRules();
		Mockito.verify(programService).getAllProgramRulesList();
	}
	
	@Test
	public void testGetAllProgramsRules_emptyList() {		
		Mockito.when(programService.getAllProgramRulesList()).thenReturn(new ArrayList<>());
		programController.getAllProgramsRules();
		Mockito.verify(programService).getAllProgramRulesList();
	}
	
	
	@Test
	public void testGetOptionalProgramRulesByProgramCodeAndOptionalProgramCode() {
		String optionalProgramCode= "FI";
		String programCode="2018-EN";
		
		List<OptionalProgramRequirement> list = new ArrayList<>();
		OptionalProgramRequirement gradProgramRule = new OptionalProgramRequirement();
		OptionalProgram op = new OptionalProgram();
		op.setOptionalProgramID(new UUID(1, 1));
    	gradProgramRule.setOptionalProgramID(op);
    	OptionalProgramRequirementCode code = new OptionalProgramRequirementCode();
		code.setOptProReqCode("100");
		gradProgramRule.setOptionalProgramRequirementCode(code);
    	list.add(gradProgramRule);
    	
		Mockito.when(programService.getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode,optionalProgramCode)).thenReturn(list);
		programController.getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode, optionalProgramCode);
		Mockito.verify(programService).getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode,optionalProgramCode);
	}
	
	@Test
	public void testGetOptionalProgramRulesByProgramCodeAndOptionalProgramCode_emptyList() {
		String optionalProgramCode= "FI";
		String programCode="2018-EN";
		Mockito.when(programService.getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode,optionalProgramCode)).thenReturn(new ArrayList<>());
		programController.getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode, optionalProgramCode);
		Mockito.verify(programService).getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode,optionalProgramCode);
	}
	
	@Test
	public void testgetAllProgramsRules() {		
		List<ProgramRequirement> list = new ArrayList<ProgramRequirement>();
		ProgramRequirement obj = new ProgramRequirement();
		obj.setGraduationProgramCode("AB");
		ProgramRequirementCode code = new ProgramRequirementCode();
		code.setProReqCode("100");
		obj.setProgramRequirementCode(code);
		list.add(obj);
		Mockito.when(programService.getAllProgramRulesList()).thenReturn(list);
		programController.getAllProgramsRules();
		Mockito.verify(programService).getAllProgramRulesList();
	}
	
	@Test
	public void testgetAllProgramsRules_empty() {		
		Mockito.when(programService.getAllProgramRulesList()).thenReturn(new ArrayList<ProgramRequirement>());
		programController.getAllProgramsRules();
		Mockito.verify(programService).getAllProgramRulesList();
	}
	
	@Test
	public void testgetAllProgramRequirementCode() {		
		List<ProgramRequirementCode> list = new ArrayList<ProgramRequirementCode>();
		ProgramRequirementCode obj = new ProgramRequirementCode();
		obj.setProReqCode("100");
		list.add(obj);
		Mockito.when(programService.getAllProgramRequirementCodeList()).thenReturn(list);
		programController.getAllProgramRequirementCode();
		Mockito.verify(programService).getAllProgramRequirementCodeList();
	}
	
	@Test
	public void testgetAllProgramRequirementCode_empty() {		
		Mockito.when(programService.getAllProgramRequirementCodeList()).thenReturn(new ArrayList<ProgramRequirementCode>());
		programController.getAllProgramRequirementCode();
		Mockito.verify(programService).getAllProgramRequirementCodeList();
	}
	
	@Test
	public void testgetAllOptionalProgramRequirementCode() {		
		List<OptionalProgramRequirementCode> list = new ArrayList<OptionalProgramRequirementCode>();
		OptionalProgramRequirementCode obj = new OptionalProgramRequirementCode();
		obj.setOptProReqCode("100");
		list.add(obj);
		Mockito.when(programService.getAllOptionalProgramRequirementCodeList()).thenReturn(list);
		programController.getAllOptionalProgramRequirementCode();
		Mockito.verify(programService).getAllOptionalProgramRequirementCodeList();
	}
	
	@Test
	public void testgetAllOptionalProgramRequirementCode_empty() {		
		Mockito.when(programService.getAllOptionalProgramRequirementCodeList()).thenReturn(new ArrayList<OptionalProgramRequirementCode>());
		programController.getAllOptionalProgramRequirementCode();
		Mockito.verify(programService).getAllOptionalProgramRequirementCodeList();
	}
	
	@Test
	public void testGetAllAlgorithmData() {
		String programCode="2018-EN";
		String optionalProgramCode="FI";
		GradProgramAlgorithmData data = new GradProgramAlgorithmData();
		List<ProgramRequirement> list = new ArrayList<ProgramRequirement>();
		ProgramRequirement obj = new ProgramRequirement();
		obj.setGraduationProgramCode("AB");
		ProgramRequirementCode code = new ProgramRequirementCode();
		code.setProReqCode("100");
		obj.setProgramRequirementCode(code);
		list.add(obj);
		data.setProgramRules(list);
		
		List<OptionalProgramRequirement> listOptional = new ArrayList<OptionalProgramRequirement>();
		OptionalProgramRequirement objs = new OptionalProgramRequirement();
		OptionalProgram op = new OptionalProgram();
		op.setOptionalProgramID(new UUID(1, 1));
		objs.setOptionalProgramID(op);
		OptionalProgramRequirementCode codes = new OptionalProgramRequirementCode();
		codes.setOptProReqCode("100");
		objs.setOptionalProgramRequirementCode(codes);
		listOptional.add(objs);
		
		data.setOptionalProgramRules(listOptional);
		
		Mockito.when(programService.getAllAlgorithmData(programCode,optionalProgramCode,null)).thenReturn(data);
		programController.getAllAlgorithmData(programCode, optionalProgramCode);
		Mockito.verify(programService).getAllAlgorithmData(programCode,optionalProgramCode,null);
	}
	
	@Test
	public void testGetAllCareerProgramCodeList() {
		List<CareerProgram> gradCareerProgramList = new ArrayList<>();
		CareerProgram obj = new CareerProgram();
		obj.setCode("DC");
		obj.setDescription("Data Correction by School");
		gradCareerProgramList.add(obj);
		obj = new CareerProgram();
		obj.setCode("CC");
		obj.setDescription("Courses not complete");
		gradCareerProgramList.add(obj);
		Mockito.when(programService.getAllCareerProgramCodeList()).thenReturn(gradCareerProgramList);
		programController.getAllCareerPrograms();
		Mockito.verify(programService).getAllCareerProgramCodeList();
	}
	
	@Test
	public void testGetSpecificCareerProgramCode() {
		String requirementType = "DC";
		CareerProgram obj = new CareerProgram();
		obj.setCode("DC");
		obj.setDescription("Data Correction by School");
		Mockito.when(programService.getSpecificCareerProgramCode(requirementType)).thenReturn(obj);
		programController.getSpecificCareerProgramCode(requirementType);
		Mockito.verify(programService).getSpecificCareerProgramCode(requirementType);
	}
	
	@Test
	public void testGetSpecificCareerProgramCode_noContent() {
		String requirementType = "AB";
		Mockito.when(programService.getSpecificCareerProgramCode(requirementType)).thenReturn(null);
		programController.getSpecificCareerProgramCode(requirementType);
		Mockito.verify(programService).getSpecificCareerProgramCode(requirementType);
	}
	
	@Test
	public void testGetAllRequirementTypesCodeList() {
		List<RequirementTypeCode> gradRequirementTypesList = new ArrayList<>();
		RequirementTypeCode obj = new RequirementTypeCode();
		obj.setReqTypeCode("DC");
		obj.setDescription("Data Correction by School");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		gradRequirementTypesList.add(obj);
		obj = new RequirementTypeCode();
		obj.setReqTypeCode("CC");
		obj.setDescription("Courses not complete");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		gradRequirementTypesList.add(obj);
		Mockito.when(programService.getAllRequirementTypeCodeList()).thenReturn(gradRequirementTypesList);
		programController.getAllRequirementTypeCodeList();
		Mockito.verify(programService).getAllRequirementTypeCodeList();
	}
	
	@Test
	public void testGetSpecificRequirementTypesCode() {
		String requirementType = "DC";
		RequirementTypeCode obj = new RequirementTypeCode();
		obj.setReqTypeCode("DC");
		obj.setDescription("Data Correction by School");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		Mockito.when(programService.getSpecificRequirementTypeCode(requirementType)).thenReturn(obj);
		programController.getSpecificRequirementTypeCode(requirementType);
		Mockito.verify(programService).getSpecificRequirementTypeCode(requirementType);
	}
	
	@Test
	public void testGetSpecificRequirementTypesCode_noContent() {
		String requirementType = "AB";
		Mockito.when(programService.getSpecificRequirementTypeCode(requirementType)).thenReturn(null);
		programController.getSpecificRequirementTypeCode(requirementType);
		Mockito.verify(programService).getSpecificRequirementTypeCode(requirementType);
	}
}
