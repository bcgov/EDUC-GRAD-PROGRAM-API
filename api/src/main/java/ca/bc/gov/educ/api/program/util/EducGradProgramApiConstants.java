package ca.bc.gov.educ.api.program.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class EducGradProgramApiConstants {

    //API end-point Mapping constants
    public static final String API_ROOT_MAPPING = "";
    public static final String API_VERSION = "v1";
    public static final String GRAD_PROGRAM_API_ROOT_MAPPING = "/api/" + API_VERSION + "/program";
    public static final String GET_ALL_PROGRAM_MAPPING = "/programs";
    public static final String GET_ALL_OPTIONAL_PROGRAM_MAPPING = "/optionalprograms";
    public static final String DELETE_PROGRAM_MAPPING = "/programs/{programCode}";
    public static final String DELETE_OPTIONAL_PROGRAM_MAPPING = "/optionalprograms/{optionalProgramID}";
    public static final String GET_PROGRAM_MAPPING = "/programs/{programCode}";
    public static final String DELETE_PROGRAM_RULES_MAPPING = "/programrules/{programRuleID}";
    public static final String DELETE_OPTIONAL_PROGRAM_RULES_MAPPING = "/optionalprogramrules/{programRuleID}";
    public static final String GET_ALL_OPTIONAL_PROGRAM_MAPPING_BY_ID = "/optioanalprograms/id/{optionalProgramID}";

    //Attribute Constants
    public static final String GET_ALL_PROGRAM_RULES = "/programrules";
    public static final String GET_PROGRAM_RULES = "/allprogramrules";
    public static final String GET_ALL_OPTIONAL_PROGRAM_RULES = "/optionalprogramrules";
    public static final String GET_OPTIONAL_PROGRAM_RULES = "/alloptionalprogramrules";
    
    public static final String GET_ALL_OPTIONAL_PROGRAM_BY_PROGRAM_CODE_AND_OPTIONAL_PROGRAM_MAPPING = "/optionalprograms/{programCode}/{optionalProgramCode}";
    public static final String GET_OPTIONAL_PROGRAM_RULES_BY_PROGRAM_CODE_AND_OPTIONAL_PROGRAM_CODE_ONLY = "/optionalprogramrules/{programCode}/{optionalProgramCode}";
    public static final String GET_ALL_SPECIFIC_PROGRAM_RULES_BY_RULE = "/programrules/{ruleCode}";
    
    //Default Attribute value constants
    public static final String DEFAULT_CREATED_BY = "API_GRAD_PROGRAM";
    public static final Date DEFAULT_CREATED_TIMESTAMP = new Date();
    public static final String DEFAULT_UPDATED_BY = "API_GRAD_PROGRAM";
    public static final Date DEFAULT_UPDATED_TIMESTAMP = new Date();

    //Default Date format constants
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    public static final String TRAX_DATE_FORMAT = "yyyyMM";
    
    @Value("${endpoint.code-api.program-type_by_code.url}")
    private String gradProgramTypeByCode;
    
    @Value("${endpoint.code-api.requirement-type_by_code.url}")
    private String gradRequirementTypeByCode;
}
