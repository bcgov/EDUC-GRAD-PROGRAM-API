package ca.bc.gov.educ.api.program.service;


import ca.bc.gov.educ.api.program.model.dto.*;
import ca.bc.gov.educ.api.program.model.entity.*;
import ca.bc.gov.educ.api.program.model.transformer.*;
import ca.bc.gov.educ.api.program.repository.*;
import ca.bc.gov.educ.api.program.util.GradValidation;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProgramService {

    @Autowired
    private GraduationProgramCodeRepository graduationProgramCodeRepository;

	@Autowired
	private OptionalProgramCodeRepository optionalProgramCodeRepository;

	@Autowired
    private GraduationProgramCodeTransformer graduationProgramCodeTransformer;    
    
    @Autowired
    private ProgramRequirementRepository programRequirementRepository;     
    
    @Autowired
    private ProgramRequirementTransformer programRequirementTransformer;
    
    @Autowired
    private ProgramRequirementCodeRepository programRequirementCodeRepository; 
    
    @Autowired
    private OptionalProgramRequirementCodeRepository optionalProgramRequirementCodeRepository; 
    
    
    @Autowired
    private OptionalProgramRequirementRepository optionalProgramRequirementRepository;  

    @Autowired
    private OptionalProgramRequirementTransformer optionalProgramRequirementTransformer; 
    
    @Autowired
    private ProgramRequirementCodeTransformer programRequirementCodeTransformer; 
    
    @Autowired
    private OptionalProgramRequirementCodeTransformer optionalProgramRequirementCodeTransformer; 
    
    @Autowired
    private OptionalProgramRepository optionalProgramRepository;  

    @Autowired
    private OptionalProgramTransformer optionalProgramTransformer;  
    
    @Autowired
	private CareerProgramRepository gradCareerProgramRepository;

	@Autowired
	private CareerProgramTransformer gradCareerProgramTransformer;
	
	@Autowired
	private RequirementTypeCodeRepository requirementTypeCodeRepository;

	@Autowired
	private RequirementTypeCodeTransformer requirementTypeCodeTransformer;
    
    @Autowired
	GradValidation validation;
    
    @Value("${validation.value.requirementType}")
	String errorStringRequirementTypeInvalid;
    
    @Value("${validation.value.ruleCode_programCode}")
	String errorStringRuleCodeProgramCodeAssociated;
    
    @Value("${validation.value.programcode}")
	String errorStringProgramCodeExists;
    
    @Value("${validation.value.programcode_rule_check}")
   	String errorStringProgramCodeRuleCheck;
    
    @Value("${validation.value.programcode_optionalprogram_check}")
   	String errorStringProgramCodeOptionalProgramCheck;
    
	private static final String CREATE_USER="createUser";
	private static final String CREATE_DATE="createDate";
    
    @SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ProgramService.class);

	@Transactional(readOnly = true)
    public List<GraduationProgramCode> getAllProgramList() {
        return graduationProgramCodeTransformer.transformToDTO(graduationProgramCodeRepository.findAll()); 
    }

	@Transactional(readOnly = true)
	public List<GradRuleDetails> getSpecificRuleDetails(String ruleCode) {
		List<GradRuleDetails> detailList = new ArrayList<>();
		List<ProgramRequirement> gradProgramRule = programRequirementTransformer.transformToDTO(programRequirementRepository.findByRuleCode(ruleCode));
		if(!gradProgramRule.isEmpty()) {
			gradProgramRule.forEach(gpR -> {
				GradRuleDetails details = new GradRuleDetails();
				details.setRuleCode(gpR.getProgramRequirementCode().getProReqCode());
				details.setRequirementName(gpR.getProgramRequirementCode().getLabel());			
				details.setProgramCode(gpR.getGraduationProgramCode());
				details.setTraxReqNumber(gpR.getProgramRequirementCode().getTraxReqNumber());
				detailList.add(details);
			});			
		}
		List<OptionalProgramRequirement> gradOptionalProgramRule = optionalProgramRequirementTransformer.transformToDTO(optionalProgramRequirementRepository.findByRuleCode(ruleCode));
		if(!gradOptionalProgramRule.isEmpty()) {
			gradOptionalProgramRule.forEach(gpR -> {
				GradRuleDetails details = new GradRuleDetails();
				details.setRuleCode(gpR.getOptionalProgramRequirementCode().getOptProReqCode());
				details.setRequirementName(gpR.getOptionalProgramRequirementCode().getLabel());	
				OptionalProgram gradOptionalProgram = optionalProgramTransformer.transformToDTO(optionalProgramRepository.findById(gpR.getOptionalProgramID().getOptionalProgramID()));
				details.setProgramCode(gradOptionalProgram.getGraduationProgramCode());
				details.setOptionalProgramCode(gradOptionalProgram.getOptProgramCode());
				detailList.add(details);
			});			
		}
		return detailList;
	}

	@Transactional(readOnly = true)
	public List<GradRuleDetails> getSpecificRuleDetailsByTraxReqNumber(String traxReqNumber) {
		List<GradRuleDetails> detailList = new ArrayList<>();
		List<ProgramRequirement> gradProgramRule = programRequirementTransformer.transformToDTO(programRequirementRepository.findByTraxReqNumber(traxReqNumber));
		if(!gradProgramRule.isEmpty()) {
			gradProgramRule.forEach(gpR -> {
				GradRuleDetails details = new GradRuleDetails();
				details.setRuleCode(gpR.getProgramRequirementCode().getProReqCode());
				details.setRequirementName(gpR.getProgramRequirementCode().getLabel());
				details.setProgramCode(gpR.getGraduationProgramCode());
				details.setTraxReqNumber(gpR.getProgramRequirementCode().getTraxReqNumber());
				detailList.add(details);
			});
		}
		List<OptionalProgramRequirement> gradOptionalProgramRule = optionalProgramRequirementTransformer.transformToDTO(optionalProgramRequirementRepository.findByTraxReqNumber(traxReqNumber));
		if(!gradOptionalProgramRule.isEmpty()) {
			gradOptionalProgramRule.forEach(gpR -> {
				GradRuleDetails details = new GradRuleDetails();
				details.setRuleCode(gpR.getOptionalProgramRequirementCode().getOptProReqCode());
				details.setRequirementName(gpR.getOptionalProgramRequirementCode().getLabel());
				OptionalProgram gradOptionalProgram = optionalProgramTransformer.transformToDTO(optionalProgramRepository.findById(gpR.getOptionalProgramID().getOptionalProgramID()));
				details.setProgramCode(gradOptionalProgram.getGraduationProgramCode());
				details.setOptionalProgramCode(gradOptionalProgram.getOptProgramCode());
				detailList.add(details);
			});
		}
		return detailList;
	}

	@Transactional
	public GraduationProgramCode createGradProgram(GraduationProgramCode gradProgram) {
		GraduationProgramCodeEntity toBeSavedObject = graduationProgramCodeTransformer.transformToEntity(gradProgram);
		toBeSavedObject.setDisplayOrder(0);
		try {
			toBeSavedObject.setExpiryDate(new SimpleDateFormat("yyyy/MM/dd").parse("2099/12/31"));
		} catch (ParseException e) {
			logger.debug(e.getMessage());
		}
		Optional<GraduationProgramCodeEntity> existingObjectCheck = graduationProgramCodeRepository.findById(gradProgram.getProgramCode());
		if(existingObjectCheck.isPresent()) {
			validation.addErrorAndStop(String.format(errorStringProgramCodeExists,gradProgram.getProgramCode()));
			return null;			
		}else {
			return graduationProgramCodeTransformer.transformToDTO(graduationProgramCodeRepository.save(toBeSavedObject));
		}		
	}
	
	@Transactional
	public GraduationProgramCode updateGradProgram(GraduationProgramCode gradProgram) {
		Optional<GraduationProgramCodeEntity> gradProgramOptional = graduationProgramCodeRepository.findById(gradProgram.getProgramCode());
		GraduationProgramCodeEntity sourceObject = graduationProgramCodeTransformer.transformToEntity(gradProgram);
		if(gradProgramOptional.isPresent()) {			
			GraduationProgramCodeEntity gradEnity = gradProgramOptional.get();			
			BeanUtils.copyProperties(sourceObject,gradEnity,CREATE_USER,CREATE_DATE,"effectiveDate","expiryDate");
			return graduationProgramCodeTransformer.transformToDTO(graduationProgramCodeRepository.save(gradEnity));
		}else {
			validation.addErrorAndStop(String.format(errorStringProgramCodeExists,gradProgram.getProgramCode()));
			return gradProgram;
		}
	}

	@Transactional
	public int deleteGradPrograms(@Valid String programCode) {
		Optional<GraduationProgramCodeEntity> gradProgramOptional = graduationProgramCodeRepository.findIfChildRecordsExists(programCode);
		if(gradProgramOptional.isPresent()) {
			validation.addErrorAndStop(String.format(errorStringProgramCodeRuleCheck,gradProgramOptional.get().getProgramCode()));
			return 0;
		}else {
			Optional<GraduationProgramCodeEntity> gradOptionalProgramOptional = graduationProgramCodeRepository.findIfOptionalProgramsExists(programCode);
			if(gradOptionalProgramOptional.isPresent()) {
				validation.addErrorAndStop(String.format(errorStringProgramCodeOptionalProgramCheck,programCode));
				return 0;
			}else {
				graduationProgramCodeRepository.deleteById(programCode);
				return 1;
			}			
		}		
	}

	@Transactional
	public ProgramRequirement createGradProgramRules(@Valid ProgramRequirement gradProgramRule) {
		ProgramRequirementEntity toBeSavedObject = programRequirementTransformer.transformToEntity(gradProgramRule);
		UUID existingObjectCheck = programRequirementRepository.findIdByRuleCode(gradProgramRule.getProgramRequirementCode().getProReqCode(),gradProgramRule.getGraduationProgramCode());
		if(existingObjectCheck != null) {
			validation.addErrorAndStop(String.format(errorStringRuleCodeProgramCodeAssociated,gradProgramRule.getProgramRequirementCode().getProReqCode(),gradProgramRule.getGraduationProgramCode()));
			return gradProgramRule;			
		}else {
			Optional<ProgramRequirementCodeEntity> opt = programRequirementCodeRepository.findById(gradProgramRule.getProgramRequirementCode().getProReqCode());
			opt.ifPresent(toBeSavedObject::setProgramRequirementCode);
			return programRequirementTransformer.transformToDTO(programRequirementRepository.save(toBeSavedObject));					
		}
	}

	@Transactional
	public ProgramRequirement updateGradProgramRules(@Valid ProgramRequirement gradProgramRule) {
		ProgramRequirementEntity sourceObject = programRequirementTransformer.transformToEntity(gradProgramRule);
		Optional<ProgramRequirementEntity> gradProgramRulesOptional = programRequirementRepository.findById(gradProgramRule.getProgramRequirementID());
		if(gradProgramRulesOptional.isPresent()) {
			ProgramRequirementEntity gradRuleEnity = gradProgramRulesOptional.get();			
			if(checkIfRuleCodeChanged(gradRuleEnity,sourceObject)) {				
				UUID existingObjectCheck = programRequirementRepository.findIdByRuleCode(sourceObject.getProgramRequirementCode().getProReqCode(), sourceObject.getGraduationProgramCode());
				if(existingObjectCheck != null) {
					validation.addErrorAndStop(String.format(errorStringRuleCodeProgramCodeAssociated,gradProgramRule.getProgramRequirementCode().getProReqCode(),gradProgramRule.getGraduationProgramCode()));
					return gradProgramRule;			
				}
			}
			BeanUtils.copyProperties(sourceObject,gradRuleEnity,CREATE_USER,CREATE_DATE);
			Optional<ProgramRequirementCodeEntity> opt = programRequirementCodeRepository.findById(gradProgramRule.getProgramRequirementCode().getProReqCode());
			opt.ifPresent(gradRuleEnity::setProgramRequirementCode);
			return programRequirementTransformer.transformToDTO(programRequirementRepository.save(gradRuleEnity));
		}else {
			validation.addErrorAndStop("Unique Identifier not found. Update Failed");
			return gradProgramRule;
		}
	}

	private boolean checkIfRuleCodeChanged(ProgramRequirementEntity gradRuleEnity, ProgramRequirementEntity sourceObject) {
		return !sourceObject.getProgramRequirementCode().getProReqCode().equals(gradRuleEnity.getProgramRequirementCode().getProReqCode());		
	}

	private boolean checkIfOptionalRuleCodeChanged(OptionalProgramRequirementEntity gradRuleEnity, OptionalProgramRequirementEntity sourceObject) {
		return !sourceObject.getOptionalProgramRequirementCode().getOptProReqCode().equals(gradRuleEnity.getOptionalProgramRequirementCode().getOptProReqCode());
	}

	@Transactional
	public int deleteGradProgramRules(UUID programRuleID) {
		Optional<ProgramRequirementEntity> gradProgramRuleOptional = programRequirementRepository.findById(programRuleID);
		if(gradProgramRuleOptional.isPresent()) {
			programRequirementRepository.deleteById(programRuleID);
			return 1;
		}else {
			validation.addErrorAndStop("This Program Rule does not exists.");
			return 0;			
		}
	}

	@Transactional(readOnly = true)
	public Boolean getRequirementByRequirementType(String typeCode) {
		List<ProgramRequirementEntity> gradList = programRequirementRepository.existsByRequirementTypeCode(typeCode);
		return !gradList.isEmpty();
	}

	@Transactional(readOnly = true)
	public GraduationProgramCode getSpecificProgram(String programCode) {
		Optional<GraduationProgramCodeEntity> gradResponse = graduationProgramCodeRepository.findById(programCode);
		return gradResponse.map(graduationProgramCodeEntity -> graduationProgramCodeTransformer.transformToDTO(graduationProgramCodeEntity)).orElse(null);
	}

	@Transactional
	public OptionalProgram createGradOptionalProgram(@Valid OptionalProgram optionalProgram) {
		OptionalProgramEntity toBeSavedObject = optionalProgramTransformer.transformToEntity(optionalProgram);
		Optional<OptionalProgramEntity> existingObjectCheck = optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(optionalProgram.getGraduationProgramCode(),optionalProgram.getOptProgramCode());
		if(existingObjectCheck.isPresent()) {
			validation.addErrorAndStop(String.format("Optioanl Program Code [%s] and Program Code [%s] already exists",optionalProgram.getOptProgramCode(),optionalProgram.getGraduationProgramCode()));
			return optionalProgram;			
		}else {
			return optionalProgramTransformer.transformToDTO(optionalProgramRepository.save(toBeSavedObject));
		}
	}
	
	@Transactional
	public OptionalProgram updateGradOptionalPrograms(OptionalProgram optionalProgram) {
		Optional<OptionalProgramEntity> gradOptionalProgramOptional = optionalProgramRepository.findById(optionalProgram.getOptionalProgramID());
		OptionalProgramEntity sourceObject = optionalProgramTransformer.transformToEntity(optionalProgram);
		if(gradOptionalProgramOptional.isPresent()) {			
			OptionalProgramEntity gradEnity = gradOptionalProgramOptional.get();
			if(checkIfProgramCodeandOptionalPogramCodeChanged(gradEnity,sourceObject)) {
				Optional<OptionalProgramEntity> existingObjectCheck = optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(optionalProgram.getGraduationProgramCode(),optionalProgram.getOptProgramCode());
				if(existingObjectCheck.isPresent()) {
					validation.addErrorAndStop(String.format("Optional Program Code [%s] and Program Code [%s] already exists",optionalProgram.getOptProgramCode(),optionalProgram.getGraduationProgramCode()));
					return optionalProgram;			
				}
			}
			BeanUtils.copyProperties(sourceObject,gradEnity,CREATE_USER,CREATE_DATE);
			return optionalProgramTransformer.transformToDTO(optionalProgramRepository.save(gradEnity));
		}else {
			validation.addErrorAndStop(String.format("Optional Program ID [%s] does not exists",optionalProgram.getOptionalProgramID()));
			return optionalProgram;
		}
	}
	
	private boolean checkIfProgramCodeandOptionalPogramCodeChanged(OptionalProgramEntity gradOptionalProgramEntity, OptionalProgramEntity sourceObject) {
		return (!sourceObject.getGraduationProgramCode().equals(gradOptionalProgramEntity.getGraduationProgramCode()) || !sourceObject.getOptProgramCode().equals(gradOptionalProgramEntity.getOptProgramCode()));
				
	}

	@Transactional
	public int deleteGradOptionalPrograms(UUID optionalProgramID) {
		Optional<OptionalProgramEntity> gradOptionalProgramOptional = optionalProgramRepository.findById(optionalProgramID);
		if(gradOptionalProgramOptional.isPresent()) {
			optionalProgramRepository.deleteById(optionalProgramID);
			return 1;
		}else {
			validation.addErrorAndStop("This Optional Program ID does not exists.");
			return 0;			
		}
	}

	@Transactional(readOnly = true)
	public List<OptionalProgramRequirement>  getAllOptionalProgramRuleList(UUID optionalProgramID) {
        return optionalProgramRequirementTransformer.transformToDTO(optionalProgramRequirementRepository.findByOptionalProgramID(optionalProgramID));
	}

	@Transactional
	public OptionalProgramRequirement createGradOptionalProgramRules(@Valid OptionalProgramRequirement optionalProgramRequirement) {
		OptionalProgramRequirementEntity toBeSavedObject = optionalProgramRequirementTransformer.transformToEntity(optionalProgramRequirement);
		UUID existingObjectCheck = optionalProgramRequirementRepository.findIdByRuleCode(optionalProgramRequirement.getOptionalProgramRequirementCode().getOptProReqCode(),optionalProgramRequirement.getOptionalProgramID().getOptionalProgramID());
		if(existingObjectCheck != null) {
			Optional<OptionalProgramEntity> optional = optionalProgramRepository.findById(optionalProgramRequirement.getOptionalProgramID().getOptionalProgramID());
			optional.ifPresent(optionalProgramEntity -> validation.addErrorAndStop(String.format("This Rule Code [%s] is already associated to a Optional Program Code [%s] and Program Code [%s] combination.", optionalProgramRequirement.getOptionalProgramRequirementCode().getOptProReqCode(), optionalProgramEntity.getOptProgramCode(), optionalProgramEntity.getGraduationProgramCode())));
			return optionalProgramRequirement;			
		}else {
			Optional<OptionalProgramRequirementCodeEntity> opt = optionalProgramRequirementCodeRepository.findById(optionalProgramRequirement.getOptionalProgramRequirementCode().getOptProReqCode());
			opt.ifPresent(toBeSavedObject::setOptionalProgramRequirementCode);
			return optionalProgramRequirementTransformer.transformToDTO(optionalProgramRequirementRepository.save(toBeSavedObject));
		}
	}

	@Transactional
	public OptionalProgramRequirement updateGradOptionalProgramRules(@Valid OptionalProgramRequirement optionalProgramRequirement) {
		OptionalProgramRequirementEntity sourceObject = optionalProgramRequirementTransformer.transformToEntity(optionalProgramRequirement);
		Optional<OptionalProgramRequirementEntity> gradOptionalProgramRulesOptional = optionalProgramRequirementRepository.findById(optionalProgramRequirement.getOptionalProgramID().getOptionalProgramID());
		if(gradOptionalProgramRulesOptional.isPresent()) {
			OptionalProgramRequirementEntity gradRuleEnity = gradOptionalProgramRulesOptional.get();	
			if(checkIfOptionalRuleCodeChanged(gradRuleEnity,sourceObject)) {				
				UUID existingObjectCheck = optionalProgramRequirementRepository.findIdByRuleCode(sourceObject.getOptionalProgramRequirementCode().getOptProReqCode(), sourceObject.getOptionalProgramID().getOptionalProgramID());
				if(existingObjectCheck != null) {
					Optional<OptionalProgramEntity> optional = optionalProgramRepository.findById(optionalProgramRequirement.getOptionalProgramID().getOptionalProgramID());
					optional.ifPresent(optionalProgramEntity -> validation.addErrorAndStop(String.format("This Rule Code [%s] is already associated to a Optional Program Code [%s] and Program Code [%s] combination.", optionalProgramRequirement.getOptionalProgramRequirementCode().getOptProReqCode(), optionalProgramEntity.getOptProgramCode(), optionalProgramEntity.getGraduationProgramCode())));
					return optionalProgramRequirement;			
				}
			}
			BeanUtils.copyProperties(sourceObject,gradRuleEnity,CREATE_USER,CREATE_DATE);
			Optional<OptionalProgramRequirementCodeEntity> opt = optionalProgramRequirementCodeRepository.findById(optionalProgramRequirement.getOptionalProgramRequirementCode().getOptProReqCode());
			opt.ifPresent(gradRuleEnity::setOptionalProgramRequirementCode);
			return optionalProgramRequirementTransformer.transformToDTO(optionalProgramRequirementRepository.save(gradRuleEnity));			
		}else {
			validation.addErrorAndStop("Unique Identifier not found. Update Failed");
			return optionalProgramRequirement;
		}
	}

	@Transactional
	public int deleteGradOptionalProgramRules(UUID programRuleID) {
		Optional<OptionalProgramRequirementEntity> gradOptionalProgramRuleOptional = optionalProgramRequirementRepository.findById(programRuleID);
		if(gradOptionalProgramRuleOptional.isPresent()) {
			programRequirementRepository.deleteById(programRuleID);
			return 1;
		}else {
			validation.addErrorAndStop("This Program Rule does not exists.");
			return 0;			
		}
	}

	@Transactional(readOnly = true)
	public List<OptionalProgram> getAllOptionalProgramList() {
		List<OptionalProgram> opList = optionalProgramTransformer.transformToDTO(optionalProgramRepository.findAll());
		opList.forEach(op-> {
			if(op.getOptProgramCode() != null) {
				Optional<OptionalProgramCodeEntity> ent = optionalProgramCodeRepository.findById(op.getOptProgramCode());
				ent.ifPresent(optionalProgramCodeEntity -> op.setAssociatedCredential(optionalProgramCodeEntity.getAssociatedCredential()));
			}
		});
		logger.debug("Returning optional program list: {}", opList);
		return opList;
	}

	@Transactional(readOnly = true)
	public OptionalProgram getOptionalProgramByID(UUID optionalProgramID) {
		return optionalProgramTransformer.transformToDTO(optionalProgramRepository.findById(optionalProgramID));
	}

	@Transactional(readOnly = true)
	public List<OptionalProgramRequirement> getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(String programCode,
			String optionalProgramCode) {
		Optional<OptionalProgramEntity> existingObjectCheck = optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(programCode, optionalProgramCode);
		if(existingObjectCheck.isPresent()) {
			return getAllOptionalProgramRuleList(existingObjectCheck.get().getOptionalProgramID());
		}else {
			validation.addErrorAndStop(String.format("Optional Program Code [%s] and Program Code [%s] combination does not exist",optionalProgramCode,programCode));
			return new ArrayList<>();
		}
	}

	@Transactional(readOnly = true)
	public List<ProgramRequirement> getAllProgramRulesList() {
		List<ProgramRequirement> programRuleList  = programRequirementTransformer.transformToDTO(programRequirementRepository.findAll());   
    	if(!programRuleList.isEmpty()) {
	    	programRuleList.sort(Comparator.comparing(ProgramRequirement::getGraduationProgramCode));
    	}
        return programRuleList;
	}

	@Transactional(readOnly = true)
	public List<ProgramRequirement> getAllProgramRuleList(String programCode) {
      	return programRequirementTransformer.transformToDTO(programRequirementRepository.findByGraduationProgramCode(programCode));
	}

	@Transactional(readOnly = true)
	public List<OptionalProgramRequirement>  getAllOptionalProgramRulesList() {
        return optionalProgramRequirementTransformer.transformToDTO(optionalProgramRequirementRepository.findAll());
	}

	@Transactional(readOnly = true)
	public OptionalProgram getOptionalProgram(String programCode, String optionalProgramCode) {
		Optional<OptionalProgramEntity> optionalRec = optionalProgramRepository.findByGraduationProgramCodeAndOptProgramCode(programCode, optionalProgramCode);
		if(optionalRec.isPresent()) {
			return optionalProgramTransformer.transformToDTO(optionalRec.get());
		}else {
			validation.addErrorAndStop(String.format("Optional Program Code [%s] and Program Code [%s] combination does not exist",optionalProgramCode,programCode));
			return null;
		}
	}

	@Transactional(readOnly = true)
	public List<ProgramRequirementCode> getAllProgramRequirementCodeList() {
		return programRequirementCodeTransformer.transformToDTO(programRequirementCodeRepository.findAll()); 
	}

	@Transactional(readOnly = true)
	public List<OptionalProgramRequirementCode> getAllOptionalProgramRequirementCodeList() {
		return optionalProgramRequirementCodeTransformer.transformToDTO(optionalProgramRequirementCodeRepository.findAll()); 
	}

	@Transactional(readOnly = true)
	public GradProgramAlgorithmData getAllAlgorithmData(String programCode, String optionalProgramCode,UUID optionalProgramID) {
		GradProgramAlgorithmData data = new GradProgramAlgorithmData();
		GraduationProgramCode code = getSpecificProgram(programCode);
		data.setGradProgram(code);
		data.setOptionalProgramID(optionalProgramID);
		data.setProgramKey(programCode+" "+optionalProgramCode);
		List<ProgramRequirement> programRules = getAllProgramRuleList(programCode);
		data.setProgramRules(programRules);
		if(StringUtils.isNotBlank(optionalProgramCode)) {
			List<OptionalProgramRequirement> optionalProgramRules = getOptionalProgramRulesByProgramCodeAndOptionalProgramCode(programCode, optionalProgramCode);
			data.setOptionalProgramRules(optionalProgramRules);
		}
		return data;		
	}

	@Transactional(readOnly = true)
	public List<GradProgramAlgorithmData> getAllAlgorithmDataList() {
		List<GradProgramAlgorithmData> pList = new ArrayList<>();
		List<GraduationProgramCode> pgmList = getAllProgramList();
		pgmList.forEach(p-> {
			pList.add(getAllAlgorithmData(p.getProgramCode(),"",null));
			List<OptionalProgram> opList = optionalProgramTransformer.transformToDTO(optionalProgramRepository.findByGraduationProgramCode(p.getProgramCode()));
			opList.forEach(op-> pList.add(getAllAlgorithmData(p.getProgramCode(),op.getOptProgramCode(),op.getOptionalProgramID())));
		});

		return pList;
	}


	@Transactional(readOnly = true)
	public List<CareerProgram> getAllCareerProgramCodeList() {
		List<CareerProgram> gradCareerProgramList = gradCareerProgramTransformer.transformToDTO(gradCareerProgramRepository.findAll());
		gradCareerProgramList.sort(Comparator.comparing(CareerProgram::getCode));
		return gradCareerProgramList;
	}

	@Transactional(readOnly = true)
	public CareerProgram getSpecificCareerProgramCode(String cpc) {
		Optional<CareerProgramEntity> entity = gradCareerProgramRepository.findById(StringUtils.toRootUpperCase(cpc));
		if (entity.isPresent()) {
			return gradCareerProgramTransformer.transformToDTO(entity);
		} else {
			return null;
		}
	}


	@Transactional(readOnly = true)
	public List<RequirementTypeCode> getAllRequirementTypeCodeList() {
		return requirementTypeCodeTransformer.transformToDTO(requirementTypeCodeRepository.findAll());
	}

	@Transactional(readOnly = true)
	public RequirementTypeCode getSpecificRequirementTypeCode(String typeCode) {
		Optional<RequirementTypeCodeEntity> entity = requirementTypeCodeRepository
				.findById(StringUtils.toRootUpperCase(typeCode));
		return entity.map(requirementTypeCodeEntity -> requirementTypeCodeTransformer.transformToDTO(requirementTypeCodeEntity)).orElse(null);
	}

	@Transactional
	public RequirementTypeCode createRequirementTypeCode(@Valid RequirementTypeCode gradRequirementTypes) {
		RequirementTypeCodeEntity toBeSavedObject = requirementTypeCodeTransformer.transformToEntity(gradRequirementTypes);
		Optional<RequirementTypeCodeEntity> existingObjectCheck = requirementTypeCodeRepository.findById(gradRequirementTypes.getReqTypeCode());
		if(existingObjectCheck.isPresent()) {
			validation.addErrorAndStop(String.format("Requirement Type [%s] already exists",gradRequirementTypes.getReqTypeCode()));
			return gradRequirementTypes;			
		}else {
			return requirementTypeCodeTransformer.transformToDTO(requirementTypeCodeRepository.save(toBeSavedObject));
		}
	}

	@Transactional
	public RequirementTypeCode updateRequirementTypeCode(@Valid RequirementTypeCode gradRequirementTypes) {
		Optional<RequirementTypeCodeEntity> gradRequirementTypesOptional = requirementTypeCodeRepository.findById(gradRequirementTypes.getReqTypeCode());
		RequirementTypeCodeEntity sourceObject = requirementTypeCodeTransformer.transformToEntity(gradRequirementTypes);
		if(gradRequirementTypesOptional.isPresent()) {
			RequirementTypeCodeEntity gradEnity = gradRequirementTypesOptional.get();			
			BeanUtils.copyProperties(sourceObject,gradEnity,CREATE_USER,CREATE_DATE);
    		return requirementTypeCodeTransformer.transformToDTO(requirementTypeCodeRepository.save(gradEnity));
		}else {
			validation.addErrorAndStop(String.format("Requirement Type [%s] does not exists",gradRequirementTypes.getReqTypeCode()));
			return gradRequirementTypes;
		}
	}

	@Transactional
	public int deleteRequirementTypeCode(@Valid String programType) {
		boolean isPresent = getRequirementByRequirementType(programType);
		if(isPresent) {
			validation.addErrorAndStop(
					String.format("This Requirement Type [%s] cannot be deleted as some rules are of this type.",programType));
			return 0;
		}else {
			requirementTypeCodeRepository.deleteById(programType);
			return 1;
		}
	}
	
}
