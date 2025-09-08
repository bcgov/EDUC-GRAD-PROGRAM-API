package ca.bc.gov.educ.api.program.service;

import ca.bc.gov.educ.api.program.model.dto.*;
import ca.bc.gov.educ.api.program.model.entity.*;
import ca.bc.gov.educ.api.program.repository.*;
import ca.bc.gov.educ.api.program.util.GradBusinessRuleException;
import ca.bc.gov.educ.api.program.util.GradValidation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProgramServiceTest {

	@Autowired ProgramService programService;
	
	@MockBean GraduationProgramCodeRepository graduationProgramCodeRepository;
	@MockBean ProgramRequirementRepository programRequirementRepository;
	@MockBean OptionalProgramRequirementRepository optionalProgramRequirementRepository;
	@MockBean OptionalProgramRepository optionalProgramRepository;
	@MockBean ProgramRequirementCodeRepository programRequirementCodeRepository;
	@MockBean OptionalProgramRequirementCodeRepository optionalProgramRequirementCodeRepository;
	@MockBean CareerProgramRepository gradCareerProgramRepository;
	@MockBean RequirementTypeCodeRepository requirementTypeCodeRepository;
	@Autowired GradValidation validation;
	@Mock WebClient webClient;
	
	@Test
	public void testGetAllProgramList() {
		List<GraduationProgramCodeEntity> gradProgramList = new ArrayList<>();
		GraduationProgramCodeEntity obj = new GraduationProgramCodeEntity();
		obj.setProgramCode("2018-EN");
		obj.setProgramName("2018 Graduation Program");
		gradProgramList.add(obj);
		obj = new GraduationProgramCodeEntity();
		obj.setProgramCode("1950-EN");
		obj.setProgramName("1950 Graduation Program");
		gradProgramList.add(obj);
		Mockito.when(graduationProgramCodeRepository.findAll()).thenReturn(gradProgramList);
		List<GraduationProgramCode> res = programService.getAllProgramList();
		assertThat(res).isNotNull().hasSize(2);
	}
	
	@Test
	public void testGetSpecificProgramCode() {
		String programCode = "2018-EN";
		GraduationProgramCode obj = new GraduationProgramCode();
		obj.setProgramCode("2018-EN");
		obj.setProgramName("2018 Graduation Program");
		GraduationProgramCodeEntity objEntity = new GraduationProgramCodeEntity();
		objEntity.setProgramCode("1950-EN");
		objEntity.setProgramName("1950 Graduation Program");
		Optional<GraduationProgramCodeEntity> ent = Optional.of(objEntity);
		Mockito.when(graduationProgramCodeRepository.findById(programCode)).thenReturn(ent);
		GraduationProgramCode res = programService.getSpecificProgram(programCode);
		assertThat(res).isNotNull();
	}
	
	@Test
	public void testGetSpecificProgramCodeReturnsNull() {
		String programCode = "2018-EN";
		Mockito.when(graduationProgramCodeRepository.findById(programCode)).thenReturn(Optional.empty());
		programService.getSpecificProgram(programCode);
		Mockito.verify(graduationProgramCodeRepository).findById(programCode);
	}
	
	
	@Test
	public void testGetSpecificRuleDetails() {
		String ruleCode = "100";
		List<ProgramRequirementEntity> gradProgramRule = new ArrayList<ProgramRequirementEntity>();
		ProgramRequirementEntity ruleObj = new ProgramRequirementEntity();
		ruleObj.setGraduationProgramCode("2018-EN");
		ProgramRequirementCodeEntity code = new ProgramRequirementCodeEntity();
		code.setProReqCode("100");
		ruleObj.setProgramRequirementCode(code);
		gradProgramRule.add(ruleObj);
		ruleObj = new ProgramRequirementEntity();
		ruleObj.setGraduationProgramCode("2018-EN");
		ProgramRequirementCodeEntity code2 = new ProgramRequirementCodeEntity();
		code2.setProReqCode("100");
		ruleObj.setProgramRequirementCode(code2);
		gradProgramRule.add(ruleObj);
		
		List<OptionalProgramRequirementEntity> gradOptionalProgramRule = new ArrayList<OptionalProgramRequirementEntity>();
		OptionalProgramRequirementEntity optionalRuleObj = new OptionalProgramRequirementEntity();
		OptionalProgramEntity opE = new OptionalProgramEntity();
		opE.setOptionalProgramID(new UUID(1, 1));
		optionalRuleObj.setOptionalProgramID(opE);
		OptionalProgramRequirementCodeEntity code3 = new OptionalProgramRequirementCodeEntity();
		code3.setOptProReqCode("100");
		optionalRuleObj.setOptionalProgramRequirementCode(code3);
		gradOptionalProgramRule.add(optionalRuleObj);
		optionalRuleObj = new OptionalProgramRequirementEntity();
		OptionalProgramEntity opE2 = new OptionalProgramEntity();
		opE2.setOptionalProgramID(new UUID(1, 1));
		optionalRuleObj.setOptionalProgramID(opE2);
		OptionalProgramRequirementCodeEntity code4 = new OptionalProgramRequirementCodeEntity();
		code4.setOptProReqCode("100");
		optionalRuleObj.setOptionalProgramRequirementCode(code4);
		gradOptionalProgramRule.add(optionalRuleObj);
		
		OptionalProgramEntity optionalProgramObj = new OptionalProgramEntity();
		optionalProgramObj.setGraduationProgramCode("2018-EN");
		optionalProgramObj.setOptProgramCode("FI");
		optionalProgramObj.setOptionalProgramName("French Immersion");
		optionalProgramObj.setOptionalProgramID(new UUID(1, 1));
		
		UUID optionalProgramID = new UUID(1, 1);
		
		Mockito.when(programRequirementRepository.findByRuleCode(ruleCode)).thenReturn(gradProgramRule);
		Mockito.when(optionalProgramRequirementRepository.findByRuleCode(ruleCode)).thenReturn(gradOptionalProgramRule);
		Mockito.when(optionalProgramRepository.findById(optionalProgramID)).thenReturn(Optional.of(optionalProgramObj));
		List<GradRuleDetails> result = programService.getSpecificRuleDetails(ruleCode);
		assertEquals(4,result.size());
	}

	@Test
	public void testGetSpecificRuleDetailsByTraxReqNumber() {
		String traxReqNumber = "5";
		List<ProgramRequirementEntity> gradProgramRule = new ArrayList<ProgramRequirementEntity>();
		ProgramRequirementEntity ruleObj = new ProgramRequirementEntity();
		ruleObj.setGraduationProgramCode("2018-EN");
		ProgramRequirementCodeEntity code = new ProgramRequirementCodeEntity();
		code.setProReqCode("100");
		code.setTraxReqNumber("5");
		ruleObj.setProgramRequirementCode(code);
		gradProgramRule.add(ruleObj);
		ruleObj = new ProgramRequirementEntity();
		ruleObj.setGraduationProgramCode("2018-EN");
		ProgramRequirementCodeEntity code2 = new ProgramRequirementCodeEntity();
		code2.setProReqCode("100");
		code.setTraxReqNumber("5");
		ruleObj.setProgramRequirementCode(code2);
		gradProgramRule.add(ruleObj);

		List<OptionalProgramRequirementEntity> gradOptionalProgramRule = new ArrayList<OptionalProgramRequirementEntity>();
		OptionalProgramRequirementEntity optionalRuleObj = new OptionalProgramRequirementEntity();
		OptionalProgramEntity opE = new OptionalProgramEntity();
		opE.setOptionalProgramID(new UUID(1, 1));
		optionalRuleObj.setOptionalProgramID(opE);
		OptionalProgramRequirementCodeEntity code3 = new OptionalProgramRequirementCodeEntity();
		code3.setOptProReqCode("100");
		code3.setTraxReqNumber("5");
		optionalRuleObj.setOptionalProgramRequirementCode(code3);
		gradOptionalProgramRule.add(optionalRuleObj);
		optionalRuleObj = new OptionalProgramRequirementEntity();
		OptionalProgramEntity opE2 = new OptionalProgramEntity();
		opE2.setOptionalProgramID(new UUID(1, 1));
		optionalRuleObj.setOptionalProgramID(opE2);
		OptionalProgramRequirementCodeEntity code4 = new OptionalProgramRequirementCodeEntity();
		code4.setOptProReqCode("100");
		code4.setTraxReqNumber("5");
		optionalRuleObj.setOptionalProgramRequirementCode(code4);
		gradOptionalProgramRule.add(optionalRuleObj);

		OptionalProgramEntity optionalProgramObj = new OptionalProgramEntity();
		optionalProgramObj.setGraduationProgramCode("2018-EN");
		optionalProgramObj.setOptProgramCode("FI");
		optionalProgramObj.setOptionalProgramName("French Immersion");
		optionalProgramObj.setOptionalProgramID(new UUID(1, 1));

		UUID optionalProgramID = new UUID(1, 1);

		Mockito.when(programRequirementRepository.findByTraxReqNumber(traxReqNumber)).thenReturn(gradProgramRule);
		Mockito.when(optionalProgramRequirementRepository.findByTraxReqNumber(traxReqNumber)).thenReturn(gradOptionalProgramRule);
		Mockito.when(optionalProgramRepository.findById(optionalProgramID)).thenReturn(Optional.of(optionalProgramObj));
		List<GradRuleDetails> result = programService.getSpecificRuleDetailsByTraxReqNumber(traxReqNumber);
		assertEquals(4,result.size());
	}
	
	@Test
	public void testGetSpecificRuleDetails_noAssociatedRuleDetails() {
		String ruleCode = "100";
		Mockito.when(programRequirementRepository.findByRuleCode(ruleCode)).thenReturn(new ArrayList<ProgramRequirementEntity>());
		Mockito.when(optionalProgramRequirementRepository.findByRuleCode(ruleCode)).thenReturn(new ArrayList<OptionalProgramRequirementEntity>());
		List<GradRuleDetails> result = programService.getSpecificRuleDetails(ruleCode);
		assertEquals(0,result.size());
	}
	
	
	@Test(expected = GradBusinessRuleException.class)
	public void testCreateGradProgram_exception() {
		GraduationProgramCode gradProgram = new GraduationProgramCode();
		gradProgram.setProgramCode("ABCD");
		gradProgram.setProgramName("EFGH");
		
		GraduationProgramCodeEntity graduationProgramCodeEntity = new GraduationProgramCodeEntity();
		graduationProgramCodeEntity.setProgramCode("ABCD");
		graduationProgramCodeEntity.setProgramName("EFGH");
		Optional<GraduationProgramCodeEntity> ent = Optional.of(graduationProgramCodeEntity);
		Mockito.when(graduationProgramCodeRepository.findById(gradProgram.getProgramCode())).thenReturn(ent);
		programService.createGradProgram(gradProgram);
		
	}
	
	@Test
	public void testCreateGradProgram() {
		GraduationProgramCode gradProgram = new GraduationProgramCode();
		gradProgram.setProgramCode("ABCD");
		gradProgram.setProgramName("EFGH");	
		try {
			gradProgram.setEffectiveDate(new SimpleDateFormat("yyyy/MM/dd").parse("2021/07/15"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		GraduationProgramCodeEntity graduationProgramCodeEntity = new GraduationProgramCodeEntity();
		graduationProgramCodeEntity.setProgramCode("ABCD");
		graduationProgramCodeEntity.setProgramName("EFGH");
		graduationProgramCodeEntity.setDisplayOrder(0);
		
		try {
			graduationProgramCodeEntity.setExpiryDate(new SimpleDateFormat("yyyy/MM/dd").parse("2099/12/31"));
			graduationProgramCodeEntity.setEffectiveDate(new SimpleDateFormat("yyyy/MM/dd").parse("2021/07/15"));
		} catch (ParseException e) {
			e.getMessage();
		}
		Mockito.when(graduationProgramCodeRepository.findById(gradProgram.getProgramCode())).thenReturn(Optional.empty());
		Mockito.when(graduationProgramCodeRepository.save(graduationProgramCodeEntity)).thenReturn(graduationProgramCodeEntity);
		GraduationProgramCode res = programService.createGradProgram(gradProgram);
		assertThat(res).isNotNull();
		
	}
	
	@Test
	public void testUpdateGradProgram() {
		GraduationProgramCode gradProgram = new GraduationProgramCode();
		gradProgram.setProgramCode("ABCD");
		gradProgram.setProgramName("EFGHF");	
		GraduationProgramCodeEntity toBeSaved = new GraduationProgramCodeEntity();
		toBeSaved.setProgramCode("ABCD");
		toBeSaved.setProgramName("EFGHF");	
		
		GraduationProgramCodeEntity graduationProgramCodeEntity = new GraduationProgramCodeEntity();
		graduationProgramCodeEntity.setProgramCode("ABCD");
		graduationProgramCodeEntity.setProgramName("EFGH");
		Optional<GraduationProgramCodeEntity> ent = Optional.of(graduationProgramCodeEntity);
		Mockito.when(graduationProgramCodeRepository.findById(gradProgram.getProgramCode())).thenReturn(ent);
		Mockito.when(graduationProgramCodeRepository.save(graduationProgramCodeEntity)).thenReturn(toBeSaved);
		GraduationProgramCode res = programService.updateGradProgram(gradProgram);
		assertThat(res).isNotNull();
		
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void testUpdateGradProgram_excpetion() {
		GraduationProgramCode gradProgram = new GraduationProgramCode();
		gradProgram.setProgramCode("ABCD");
		gradProgram.setProgramName("EFGHF");
		Mockito.when(graduationProgramCodeRepository.findById(gradProgram.getProgramCode())).thenReturn(Optional.empty());
		programService.updateGradProgram(gradProgram);			
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void testUpdateGradProgram_exception() {
		GraduationProgramCode gradProgram = new GraduationProgramCode();
		gradProgram.setProgramCode("ABCD");
		gradProgram.setProgramName("EFGHF");
		Mockito.when(graduationProgramCodeRepository.findById(gradProgram.getProgramCode())).thenReturn(Optional.empty());
		programService.updateGradProgram(gradProgram);
		
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void deleteGradProgram_exception_childrecords() {
		String programCode="2018-EN";
		GraduationProgramCodeEntity graduationProgramCodeEntity = new GraduationProgramCodeEntity();
		graduationProgramCodeEntity.setProgramCode("ABCD");
		graduationProgramCodeEntity.setProgramName("EFGH");
		Optional<GraduationProgramCodeEntity> ent = Optional.of(graduationProgramCodeEntity);
		Mockito.when(graduationProgramCodeRepository.findIfChildRecordsExists(programCode)).thenReturn(ent);
		programService.deleteGradPrograms(programCode);
	}
	
	@Test
	public void testDeleteGradProgram() {
		String programCode="2018-EN";
		GraduationProgramCodeEntity graduationProgramCodeEntity = new GraduationProgramCodeEntity();
		graduationProgramCodeEntity.setProgramCode("ABCD");
		graduationProgramCodeEntity.setProgramName("EFGH");
		Mockito.when(graduationProgramCodeRepository.findIfChildRecordsExists(programCode)).thenReturn(Optional.empty());
		Mockito.when(graduationProgramCodeRepository.findIfOptionalProgramsExists(programCode)).thenReturn(Optional.empty());
		int count = programService.deleteGradPrograms(programCode);
		assertThat(count).isEqualTo(1);
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void testDeleteGradProgram_exception_optionalprogramcheck() {
		String programCode="2018-EN";
		GraduationProgramCodeEntity graduationProgramCodeEntity = new GraduationProgramCodeEntity();
		graduationProgramCodeEntity.setProgramCode("ABCD");
		graduationProgramCodeEntity.setProgramName("EFGH");
		Optional<GraduationProgramCodeEntity> ent = Optional.of(graduationProgramCodeEntity);
		Mockito.when(graduationProgramCodeRepository.findIfChildRecordsExists(programCode)).thenReturn(Optional.empty());
		Mockito.when(graduationProgramCodeRepository.findIfOptionalProgramsExists(programCode)).thenReturn(ent);
		programService.deleteGradPrograms(programCode);
	}
	
	@Test
	public void testDeleteGradProgramRules() {
		UUID ruleID=new UUID(1, 1);
		ProgramRequirementEntity gradProgramRulesEntity = new ProgramRequirementEntity();
		ProgramRequirementCodeEntity code = new ProgramRequirementCodeEntity();
		code.setProReqCode("100");
		gradProgramRulesEntity.setProgramRequirementCode(code);
		gradProgramRulesEntity.setGraduationProgramCode("2018-EN");
		Mockito.when(programRequirementRepository.findById(ruleID)).thenReturn(Optional.of(gradProgramRulesEntity));
		int count = programService.deleteGradProgramRules(ruleID);
		assertThat(count).isEqualTo(1);

	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void deleteGradProgram_exception_exception() {
		UUID ruleID=new UUID(1, 1);
		Mockito.when(programRequirementRepository.findById(ruleID)).thenReturn(Optional.empty());
		programService.deleteGradProgramRules(ruleID);
	}
	
	
	@Test(expected = GradBusinessRuleException.class)
	public void testCreateGradOptionalProgram_exception() {
		OptionalProgram gradOptionalProgram = new OptionalProgram();
		gradOptionalProgram.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgram.setOptProgramCode("FI");
		gradOptionalProgram.setGraduationProgramCode("ABCD");
		gradOptionalProgram.setOptionalProgramName("EFGH");
		
		OptionalProgramEntity gradOptionalProgramEntity = new OptionalProgramEntity();
		gradOptionalProgramEntity.setGraduationProgramCode("ABCD");
		gradOptionalProgramEntity.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramEntity.setOptProgramCode("FI");
		gradOptionalProgramEntity.setOptionalProgramName("EFGH");
		Optional<OptionalProgramEntity> ent = Optional.of(gradOptionalProgramEntity);
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(gradOptionalProgram.getGraduationProgramCode(),gradOptionalProgram.getOptProgramCode())).thenReturn(ent);
		programService.createGradOptionalProgram(gradOptionalProgram);
		
	}
	
	@Test
	public void testCreateGradOptionalProgram() {
		OptionalProgram gradOptionalProgram = new OptionalProgram();
		gradOptionalProgram.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgram.setOptProgramCode("FI");
		gradOptionalProgram.setGraduationProgramCode("ABCD");
		gradOptionalProgram.setOptionalProgramName("EFGH");
		
		OptionalProgramEntity gradOptionalProgramEntity = new OptionalProgramEntity();
		gradOptionalProgramEntity.setGraduationProgramCode("ABCD");
		gradOptionalProgramEntity.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramEntity.setOptProgramCode("FI");
		gradOptionalProgramEntity.setOptionalProgramName("EFGH");
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(gradOptionalProgram.getGraduationProgramCode(),gradOptionalProgram.getOptProgramCode())).thenReturn(Optional.empty());
		Mockito.when(optionalProgramRepository.save(gradOptionalProgramEntity)).thenReturn(gradOptionalProgramEntity);
		OptionalProgram res = programService.createGradOptionalProgram(gradOptionalProgram);
		assertThat(res).isNotNull();
		
	}
	
	@Test
	public void testUpdateGradOptionalProgram() {
		OptionalProgram gradOptionalProgram = new OptionalProgram();
		gradOptionalProgram.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgram.setOptProgramCode("FI");
		gradOptionalProgram.setGraduationProgramCode("ABCD");
		gradOptionalProgram.setOptionalProgramName("EFGH");
		OptionalProgramEntity toBeSaved = new OptionalProgramEntity();
		toBeSaved.setOptionalProgramID(new UUID(1, 1));
		toBeSaved.setOptProgramCode("FI");
		toBeSaved.setGraduationProgramCode("ABCD");
		toBeSaved.setOptionalProgramName("EFGH");
		
		OptionalProgramEntity gradOptionalProgramEntity = new OptionalProgramEntity();
		gradOptionalProgramEntity.setGraduationProgramCode("ABCD");
		gradOptionalProgramEntity.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramEntity.setOptProgramCode("FI");
		gradOptionalProgramEntity.setOptionalProgramName("EFGH");
		Optional<OptionalProgramEntity> ent = Optional.of(gradOptionalProgramEntity);
		Mockito.when(optionalProgramRepository.findById(gradOptionalProgram.getOptionalProgramID())).thenReturn(ent);
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(gradOptionalProgram.getGraduationProgramCode(),gradOptionalProgram.getOptProgramCode())).thenReturn(Optional.empty());
		Mockito.when(optionalProgramRepository.save(gradOptionalProgramEntity)).thenReturn(toBeSaved);
		OptionalProgram res = programService.updateGradOptionalPrograms(gradOptionalProgram);
		assertThat(res).isNotNull();
		
	}
	
	@Test
	public void testUpdateGradOptionalProgram_optionalProgramChanged() {
		validation.clear();
		OptionalProgram gradOptionalProgram = new OptionalProgram();
		gradOptionalProgram.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgram.setOptProgramCode("FI");
		gradOptionalProgram.setGraduationProgramCode("ABCD");
		gradOptionalProgram.setOptionalProgramName("EFGH");
		OptionalProgramEntity toBeSaved = new OptionalProgramEntity();
		toBeSaved.setOptionalProgramID(new UUID(1, 1));
		toBeSaved.setOptProgramCode("FI");
		toBeSaved.setGraduationProgramCode("ABCD");
		toBeSaved.setOptionalProgramName("EFGH");
		
		OptionalProgramEntity gradOptionalProgramEntity = new OptionalProgramEntity();
		gradOptionalProgramEntity.setGraduationProgramCode("ABCD");
		gradOptionalProgramEntity.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramEntity.setOptProgramCode("DD");
		gradOptionalProgramEntity.setOptionalProgramName("EFGH");
		Optional<OptionalProgramEntity> ent = Optional.of(gradOptionalProgramEntity);
		Mockito.when(optionalProgramRepository.findById(gradOptionalProgram.getOptionalProgramID())).thenReturn(ent);
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(gradOptionalProgram.getGraduationProgramCode(),gradOptionalProgram.getOptProgramCode())).thenReturn(Optional.of(gradOptionalProgramEntity));
		Mockito.when(optionalProgramRepository.save(gradOptionalProgramEntity)).thenReturn(toBeSaved);
		
		try {
			programService.updateGradOptionalPrograms(gradOptionalProgram);
		} catch (GradBusinessRuleException e) {
			List<String> errors = validation.getErrors();
			assertEquals(1, errors.size());
			return;
		}		
	}
	
	@Test
	public void testGetOptionalProgramRulesByProgramCodeAndOptionalProgramCode() {
		String programCode="2018-EN";
		String optionalProgramCode="FI";
		
		OptionalProgramEntity gradOptionalProgramEntity = new OptionalProgramEntity();
		gradOptionalProgramEntity.setGraduationProgramCode("2018-EN");
		gradOptionalProgramEntity.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramEntity.setOptProgramCode("FI");
		gradOptionalProgramEntity.setOptionalProgramName("French Immersion");
		
		List<OptionalProgramRequirementEntity> gradProgramRuleList = new ArrayList<OptionalProgramRequirementEntity>();
		OptionalProgramRequirementEntity ruleObj = new OptionalProgramRequirementEntity();
		OptionalProgramEntity opE = new OptionalProgramEntity();
		opE.setOptionalProgramID(new UUID(1, 1));
		ruleObj.setOptionalProgramID(opE);
		OptionalProgramRequirementCodeEntity code = new OptionalProgramRequirementCodeEntity();
		code.setOptProReqCode("100");
		ruleObj.setOptionalProgramRequirementCode(code);
		gradProgramRuleList.add(ruleObj);
		ruleObj = new OptionalProgramRequirementEntity();
		OptionalProgramEntity opE2 = new OptionalProgramEntity();
		opE2.setOptionalProgramID(new UUID(2, 2));
		ruleObj.setOptionalProgramID(opE2);
		OptionalProgramRequirementCodeEntity code2 = new OptionalProgramRequirementCodeEntity();
		code2.setOptProReqCode("100");
		ruleObj.setOptionalProgramRequirementCode(code2);
		gradProgramRuleList.add(ruleObj);
		
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(programCode, optionalProgramCode)).thenReturn(Optional.of(gradOptionalProgramEntity));
		Mockito.when(optionalProgramRequirementRepository.findByOptionalProgramID(gradOptionalProgramEntity.getOptionalProgramID())).thenReturn(gradProgramRuleList);
		
		List<OptionalProgramRequirement> res = programService.getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode, optionalProgramCode);
		assertThat(res).isNotNull().hasSize(2);
	}
	
	
	@Test(expected = GradBusinessRuleException.class)
	public void testUpdateGradOptionalProgram_excpetion() {
		OptionalProgram gradOptionalProgram = new OptionalProgram();
		gradOptionalProgram.setGraduationProgramCode("ABCD");
		gradOptionalProgram.setOptionalProgramName("EFGHF");
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(gradOptionalProgram.getGraduationProgramCode(),gradOptionalProgram.getOptProgramCode())).thenReturn(Optional.empty());
		programService.updateGradOptionalPrograms(gradOptionalProgram);			
	}
	
	
	@Test
	public void testDeleteGradOptionalProgram() {
		UUID ruleID=new UUID(1, 1);
		OptionalProgramEntity gradOptionalProgramEntity = new OptionalProgramEntity();
		gradOptionalProgramEntity.setGraduationProgramCode("ABCD");
		gradOptionalProgramEntity.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramEntity.setOptProgramCode("FI");
		gradOptionalProgramEntity.setOptionalProgramName("EFGH");
		Mockito.when(optionalProgramRepository.findById(ruleID)).thenReturn(Optional.of(gradOptionalProgramEntity));
		int count = programService.deleteGradOptionalPrograms(ruleID);
		assertThat(count).isEqualTo(1);
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void testDeleteGradOptionalProgram_exception() {
		UUID ruleID=new UUID(1, 1);
		OptionalProgramEntity gradOptionalProgramEntity = new OptionalProgramEntity();
		gradOptionalProgramEntity.setGraduationProgramCode("ABCD");
		gradOptionalProgramEntity.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramEntity.setOptProgramCode("FI");
		gradOptionalProgramEntity.setOptionalProgramName("EFGH");
		Mockito.when(optionalProgramRepository.findById(ruleID)).thenReturn(Optional.empty());
		programService.deleteGradOptionalPrograms(ruleID);
	}
	
		
	@Test
	public void testGetAllOptionalProgramList() {
		List<OptionalProgramEntity> gradProgramList = new ArrayList<>();
		OptionalProgramEntity obj = new OptionalProgramEntity();
		obj.setOptionalProgramID(new UUID(1, 1));
		obj.setOptionalProgramName("2018 Graduation Program");
		obj.setOptProgramCode("FI");
		gradProgramList.add(obj);
		obj = new OptionalProgramEntity();
		obj.setOptionalProgramID(new UUID(1, 1));
		obj.setOptionalProgramName("2018 Graduation Program");
		obj.setOptProgramCode("FI");
		gradProgramList.add(obj);
		Mockito.when(optionalProgramRepository.findAll()).thenReturn(gradProgramList);
		List<OptionalProgram> res = programService.getAllOptionalProgramList();
		assertThat(res).isNotNull().hasSize(2);

	}
	
	@Test
	public void testGetOptionalProgramByID() {
		UUID optionalProgramID = new UUID(1, 1);
		OptionalProgramEntity obj = new OptionalProgramEntity();
		obj.setOptionalProgramID(new UUID(1, 1));
		obj.setOptionalProgramName("2018 Graduation Program");
		obj.setOptProgramCode("FI");
		Mockito.when(optionalProgramRepository.findById(optionalProgramID)).thenReturn(Optional.of(obj));
		OptionalProgram opt = programService.getOptionalProgramByID(optionalProgramID);
		assertThat(opt).isNotNull();
	}	
	
	@Test
	public void testGetOptionalProgram() {
		String programCode = "2018-EN";
		String optionalProgramCode = "FI";
		OptionalProgramEntity obj = new OptionalProgramEntity();
		obj.setOptionalProgramID(new UUID(1, 1));
		obj.setGraduationProgramCode("2018-EN");
		obj.setOptionalProgramName("2018 Graduation Program");
		obj.setOptProgramCode("FI");        
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(programCode, optionalProgramCode)).thenReturn(Optional.of(obj));
		OptionalProgram res = programService.getOptionalProgram(programCode, optionalProgramCode);
		assertThat(res).isNotNull();
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void testGetOptionalProgram_exception() {
		String programCode = "2018-EN";
		String optionalProgramCode = "FI";
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(programCode, optionalProgramCode)).thenReturn(Optional.empty());
		programService.getOptionalProgram(programCode, optionalProgramCode);
	}
	
	@Test
	public void testDeleteGradOptionalProgramRules() {
		UUID ruleID=new UUID(1, 1);
		OptionalProgramRequirementEntity gradOptionalProgramRulesEntity = new OptionalProgramRequirementEntity();
		OptionalProgramRequirementCodeEntity code = new OptionalProgramRequirementCodeEntity();
		code.setOptProReqCode("100");
		gradOptionalProgramRulesEntity.setOptionalProgramRequirementCode(code);
		OptionalProgramEntity opE = new OptionalProgramEntity();
		opE.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramRulesEntity.setOptionalProgramID(opE);
		Mockito.when(optionalProgramRequirementRepository.findById(ruleID)).thenReturn(Optional.of(gradOptionalProgramRulesEntity));
		int count = programService.deleteGradOptionalProgramRules(ruleID);
		assertThat(count).isEqualTo(1);
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void deleteGradOptionalProgram_exception_exception() {
		UUID ruleID=new UUID(1, 1);
		Mockito.when(optionalProgramRequirementRepository.findById(ruleID)).thenReturn(Optional.empty());
		programService.deleteGradOptionalProgramRules(ruleID);
	}
	
	@Test
	public void testGetAllProgramRequirementCodeList() {
		List<ProgramRequirementCodeEntity> gradProgramList = new ArrayList<>();
		ProgramRequirementCodeEntity obj = new ProgramRequirementCodeEntity();
		obj.setProReqCode("300");
		obj.setDescription("2018 Graduation Program");
		gradProgramList.add(obj);
		obj = new ProgramRequirementCodeEntity();
		obj.setProReqCode("300");
		obj.setDescription("2018 Graduation Program");
		gradProgramList.add(obj);
		Mockito.when(programRequirementCodeRepository.findAll()).thenReturn(gradProgramList);
		List<ProgramRequirementCode> res = programService.getAllProgramRequirementCodeList();
		assertThat(res).isNotNull();
	}
	
	@Test
	public void testGetAllOptionalProgramRequirementCodeList() {
		List<OptionalProgramRequirementCodeEntity> gradProgramList = new ArrayList<>();
		OptionalProgramRequirementCodeEntity obj = new OptionalProgramRequirementCodeEntity();
		obj.setOptProReqCode("300");
		obj.setDescription("2018 Graduation Program");
		gradProgramList.add(obj);
		obj = new OptionalProgramRequirementCodeEntity();
		obj.setOptProReqCode("400");
		obj.setDescription("2018 Graduation Program");
		gradProgramList.add(obj);
		Mockito.when(optionalProgramRequirementCodeRepository.findAll()).thenReturn(gradProgramList);
		List<OptionalProgramRequirementCode> opt = programService.getAllOptionalProgramRequirementCodeList();
		assertThat(opt).isNotNull();
	}
	
	@Test
	public void testGetAllCareerProgramsCodeList() {
		List<CareerProgramEntity> gradCareerProgramList = new ArrayList<>();
		CareerProgramEntity obj = new CareerProgramEntity();
		obj.setCode("AY");
		obj.setDescription("Archaeology");
		gradCareerProgramList.add(obj);
		obj = new CareerProgramEntity();
		obj.setCode("BE");
		obj.setDescription("Business Education");
		gradCareerProgramList.add(obj);
		Mockito.when(gradCareerProgramRepository.findAll()).thenReturn(gradCareerProgramList);
		List<CareerProgram> opt = programService.getAllCareerProgramCodeList();
		assertThat(opt).isNotNull();
	}
	
	@Test
	public void testGetSpecificCareerProgramCode() {
		String cpcCode = "AY";
		CareerProgram obj = new CareerProgram();
		obj.setCode("AY");
		obj.setDescription("Archaeology");
		CareerProgramEntity objEntity = new CareerProgramEntity();
		objEntity.setCode("AY");
		objEntity.setDescription("Archaeology");
		Optional<CareerProgramEntity> ent = Optional.of(objEntity);
		Mockito.when(gradCareerProgramRepository.findById(cpcCode)).thenReturn(ent);
		CareerProgram opt = programService.getSpecificCareerProgramCode(cpcCode);
		assertThat(opt).isNotNull();
	}
	
	@Test
	public void testGetSpecificCareerProgramCodeReturnsNull() {
		String cpcCode = "AZ";
		Mockito.when(gradCareerProgramRepository.findById(cpcCode)).thenReturn(Optional.empty());
		CareerProgram opt = programService.getSpecificCareerProgramCode(cpcCode);
		assertThat(opt).isNull();
	}
	
	@Test
	public void testGetAllAlgorithmData() {
		String optionalProgramCode="FI";
		String programCode="2018-EN";
		
		GraduationProgramCodeEntity codeG = new GraduationProgramCodeEntity();
		codeG.setProgramCode("2018-EN");
		codeG.setProgramName("SRQQW");
		List<ProgramRequirementEntity> gradProgramRuleList = new ArrayList<ProgramRequirementEntity>();
		ProgramRequirementEntity ruleObj = new ProgramRequirementEntity();
		ruleObj.setGraduationProgramCode("2018-EN");
		ProgramRequirementCodeEntity code = new ProgramRequirementCodeEntity();
		code.setProReqCode("100");
		ruleObj.setProgramRequirementCode(code);
		gradProgramRuleList.add(ruleObj);
		ruleObj = new ProgramRequirementEntity();
		ruleObj.setGraduationProgramCode("2018-EN");
		ProgramRequirementCodeEntity code2 = new ProgramRequirementCodeEntity();
		code2.setProReqCode("200");
		ruleObj.setProgramRequirementCode(code2);
		gradProgramRuleList.add(ruleObj);
    	Mockito.when(programRequirementRepository.findByGraduationProgramCode(programCode)).thenReturn(gradProgramRuleList);
    	
    	OptionalProgramEntity gradOptionalProgramEntity = new OptionalProgramEntity();
		gradOptionalProgramEntity.setGraduationProgramCode("2018-EN");
		gradOptionalProgramEntity.setOptionalProgramID(new UUID(1, 1));
		gradOptionalProgramEntity.setOptProgramCode("FI");
		gradOptionalProgramEntity.setOptionalProgramName("French Immersion");
		
		List<OptionalProgramRequirementEntity> gradOptionalProgramRuleList = new ArrayList<OptionalProgramRequirementEntity>();
		OptionalProgramRequirementEntity ruleOptionalObj = new OptionalProgramRequirementEntity();
		OptionalProgramEntity opE = new OptionalProgramEntity();
		opE.setOptionalProgramID(new UUID(1, 1));
		ruleOptionalObj.setOptionalProgramID(opE);
		OptionalProgramRequirementCodeEntity codeOp2 = new OptionalProgramRequirementCodeEntity();
		codeOp2.setOptProReqCode("100");
		ruleOptionalObj.setOptionalProgramRequirementCode(codeOp2);
		gradOptionalProgramRuleList.add(ruleOptionalObj);
		ruleOptionalObj = new OptionalProgramRequirementEntity();
		OptionalProgramEntity opE2 = new OptionalProgramEntity();
		opE2.setOptionalProgramID(new UUID(2, 2));
		ruleOptionalObj.setOptionalProgramID(opE2);
		OptionalProgramRequirementCodeEntity codeOp3 = new OptionalProgramRequirementCodeEntity();
		codeOp3.setOptProReqCode("100");
		ruleOptionalObj.setOptionalProgramRequirementCode(codeOp3);
		gradOptionalProgramRuleList.add(ruleOptionalObj);
		Mockito.when(graduationProgramCodeRepository.findById(programCode)).thenReturn(Optional.of(codeG));
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(programCode, optionalProgramCode)).thenReturn(Optional.of(gradOptionalProgramEntity));
		Mockito.when(optionalProgramRequirementRepository.findByOptionalProgramID(gradOptionalProgramEntity.getOptionalProgramID())).thenReturn(gradOptionalProgramRuleList);
		
		GradProgramAlgorithmData data = programService.getAllAlgorithmData(programCode, optionalProgramCode,new UUID(1, 1));
		assertNotNull(data);
		assertEquals(2, data.getOptionalProgramRules().size());
    	
	}
	
	@Test
	public void testCreateRequirementTypeCode() {
		RequirementTypeCode obj = new RequirementTypeCode();
		obj.setReqTypeCode("DC");
		obj.setDescription("Data Correction by School");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		RequirementTypeCodeEntity objEntity = new RequirementTypeCodeEntity();
		objEntity.setReqTypeCode("DC");
		objEntity.setDescription("Data Correction by School");
		objEntity.setCreateUser("GRADUATION");
		objEntity.setUpdateUser("GRADUATION");
		objEntity.setCreateDate(new Date(System.currentTimeMillis()));
		objEntity.setUpdateDate(new Date(System.currentTimeMillis()));
		Mockito.when(requirementTypeCodeRepository.findById(obj.getReqTypeCode())).thenReturn(Optional.empty());
		Mockito.when(requirementTypeCodeRepository.save(objEntity)).thenReturn(objEntity);
		RequirementTypeCode opt = programService.createRequirementTypeCode(obj);
		assertThat(opt).isNotNull();
		
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void testCreateRequirementTypeCode_codeAlreadyExists() {
		RequirementTypeCode obj = new RequirementTypeCode();
		obj.setReqTypeCode("DC");
		obj.setDescription("Data Correction by School");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		RequirementTypeCodeEntity objEntity = new RequirementTypeCodeEntity();
		objEntity.setReqTypeCode("DC");
		objEntity.setDescription("Data Correction by School");
		objEntity.setCreateUser("GRADUATION");
		objEntity.setUpdateUser("GRADUATION");
		objEntity.setCreateDate(new Date(System.currentTimeMillis()));
		objEntity.setUpdateDate(new Date(System.currentTimeMillis()));
		Optional<RequirementTypeCodeEntity> ent = Optional.of(objEntity);
		Mockito.when(requirementTypeCodeRepository.findById(obj.getReqTypeCode())).thenReturn(ent);
		programService.createRequirementTypeCode(obj);
		
	}
	
	@Test
	public void testUpdateRequirementTypeCode() {
		RequirementTypeCode obj = new RequirementTypeCode();
		obj.setReqTypeCode("DC");
		obj.setDescription("Data Correction by Schools");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		RequirementTypeCodeEntity objEntity = new RequirementTypeCodeEntity();
		objEntity.setReqTypeCode("DC");
		objEntity.setDescription("Data Correction by School");
		objEntity.setCreateUser("GRADUATION");
		objEntity.setUpdateUser("GRADUATION");
		objEntity.setCreateDate(new Date(System.currentTimeMillis()));
		objEntity.setUpdateDate(new Date(System.currentTimeMillis()));
		Optional<RequirementTypeCodeEntity> ent = Optional.of(objEntity);
		Mockito.when(requirementTypeCodeRepository.findById(obj.getReqTypeCode())).thenReturn(ent);
		Mockito.when(requirementTypeCodeRepository.save(objEntity)).thenReturn(objEntity);
		RequirementTypeCode opt = programService.updateRequirementTypeCode(obj);
		assertThat(opt).isNotNull();
		
	}
	
	@Test(expected = GradBusinessRuleException.class)
	public void testUpdateRequirementTypeCode_codeAlreadyExists() {
		RequirementTypeCode obj = new RequirementTypeCode();
		obj.setReqTypeCode("DC");
		obj.setDescription("Data Correction by Schools");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		RequirementTypeCodeEntity objEntity = new RequirementTypeCodeEntity();
		objEntity.setReqTypeCode("DC");
		objEntity.setDescription("Data Correction by School");
		objEntity.setCreateUser("GRADUATION");
		objEntity.setUpdateUser("GRADUATION");
		objEntity.setCreateDate(new Date(System.currentTimeMillis()));
		objEntity.setUpdateDate(new Date(System.currentTimeMillis()));
		Mockito.when(requirementTypeCodeRepository.findById(obj.getReqTypeCode())).thenReturn(Optional.empty());
		programService.updateRequirementTypeCode(obj);
		
	}
	
	@Test
	public void testGetAllRequirementTypesCodeList() {
		List<RequirementTypeCodeEntity> requirementTypeCodeList = new ArrayList<>();
		RequirementTypeCodeEntity obj = new RequirementTypeCodeEntity();
		obj.setReqTypeCode("M");
		obj.setDescription("MATCH");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		requirementTypeCodeList.add(obj);
		obj = new RequirementTypeCodeEntity();
		obj.setReqTypeCode("MC");
		obj.setDescription("MINCREDITS");
		requirementTypeCodeList.add(obj);
		Mockito.when(requirementTypeCodeRepository.findAll()).thenReturn(requirementTypeCodeList);
		List<RequirementTypeCode> opt = programService.getAllRequirementTypeCodeList();
		assertThat(opt).isNotNull().hasSize(2);
	}
	
	@Test
	public void testGetSpecificRequirementTypesCode() {
		String reqType = "M";
		RequirementTypeCode obj = new RequirementTypeCode();
		obj.setReqTypeCode("M");
		obj.setDescription("MATCH");
		obj.setCreateUser("GRADUATION");
		obj.setUpdateUser("GRADUATION");
		obj.setCreateDate(new Date(System.currentTimeMillis()));
		obj.setUpdateDate(new Date(System.currentTimeMillis()));
		obj.toString();
		RequirementTypeCodeEntity objEntity = new RequirementTypeCodeEntity();
		objEntity.setReqTypeCode("M");
		objEntity.setDescription("MATCH");
		objEntity.setCreateUser("GRADUATION");
		objEntity.setUpdateUser("GRADUATION");
		objEntity.setCreateDate(new Date(System.currentTimeMillis()));
		objEntity.setUpdateDate(new Date(System.currentTimeMillis()));
		Optional<RequirementTypeCodeEntity> ent = Optional.of(objEntity);
		Mockito.when(requirementTypeCodeRepository.findById(reqType)).thenReturn(ent);
		RequirementTypeCode opt = programService.getSpecificRequirementTypeCode(reqType);
		assertThat(opt).isNotNull();
	}
	
	@Test
	public void testGetSpecificRequirementTypesCodeReturnsNull() {
		String reqType = "E";
		Mockito.when(requirementTypeCodeRepository.findById(reqType)).thenReturn(Optional.empty());
		RequirementTypeCode opt = programService.getSpecificRequirementTypeCode(reqType);
		assertThat(opt).isNull();
	}

	@Test
	public void testgetAllAlgorithmDataList() {
		List<GraduationProgramCodeEntity> gradProgramList = new ArrayList<>();
		GraduationProgramCodeEntity obj = new GraduationProgramCodeEntity();
		obj.setProgramCode("2018-EN");
		obj.setProgramName("2018 Graduation Program");
		gradProgramList.add(obj);

		OptionalProgramEntity opent = new OptionalProgramEntity();
		opent.setOptionalProgramID(UUID.randomUUID());
		opent.setGraduationProgramCode("2018-EN");
		opent.setOptProgramCode("FI");

		List<ProgramRequirementEntity> gradProgramRule = new ArrayList<ProgramRequirementEntity>();
		ProgramRequirementEntity ruleObj = new ProgramRequirementEntity();
		ruleObj.setGraduationProgramCode("2018-EN");
		ProgramRequirementCodeEntity code = new ProgramRequirementCodeEntity();
		code.setProReqCode("100");
		ruleObj.setProgramRequirementCode(code);

		Mockito.when(graduationProgramCodeRepository.findAll()).thenReturn(gradProgramList);
		Mockito.when(graduationProgramCodeRepository.findById("2018-EN")).thenReturn(Optional.of(obj));
		Mockito.when(optionalProgramRepository.findByGraduationProgramCode("2018-EN")).thenReturn(List.of(opent));
		Mockito.when(programRequirementRepository.findByGraduationProgramCode("2018-EN")).thenReturn(gradProgramRule);
		Mockito.when(optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode("2018-EN", "FI")).thenReturn(Optional.of(opent));
		List<GradProgramAlgorithmData> res = programService.getAllAlgorithmDataList();
		assertThat(res).isNotNull().hasSize(2);
	}
	
}
