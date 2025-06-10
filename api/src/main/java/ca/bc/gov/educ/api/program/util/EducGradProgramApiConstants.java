package ca.bc.gov.educ.api.program.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class EducGradProgramApiConstants {

    private EducGradProgramApiConstants() {}

    public static final String CORRELATION_ID = "correlationID";
    public static final String USER_NAME = "User-Name";
    public static final String REQUEST_SOURCE = "Request-Source";
    public static final String API_NAME = "EDUC-GRAD-PROGRAM-API";

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
    public static final String GET_ALL_OPTIONAL_PROGRAM_MAPPING_BY_ID = "/optionalprograms/id/{optionalProgramID}";
    
    public static final String GET_ALL_PROGRAM_REQUIREMENT_CODES_MAPPING = "/programrequirementcode";
    public static final String GET_ALL_OPTIONAL_PROGRAM_REQUIREMENT_CODES_MAPPING = "/optionapProgramrequirementcode";

    public static final String GET_ALL_GRAD_REQUIREMENT_TYPE_CODE_MAPPING = "/gradrequirementtype";
    public static final String GET_ALL_GRAD_REQUIREMENT_TYPE_CODE_BY_CODE_MAPPING = "/gradrequirementtype/{typeCode}";
    //Attribute Constants
    public static final String GET_ALL_PROGRAM_RULES = "/programrules";
    public static final String GET_PROGRAM_RULES = "/allprogramrules";
    public static final String GET_ALL_OPTIONAL_PROGRAM_RULES = "/optionalprogramrules";
    public static final String GET_OPTIONAL_PROGRAM_RULES = "/alloptionalprogramrules";
    
    public static final String GET_ALL_OPTIONAL_PROGRAM_BY_PROGRAM_CODE_AND_OPTIONAL_PROGRAM_MAPPING = "/optionalprograms/{programCode}/{optionalProgramCode}";
    public static final String GET_OPTIONAL_PROGRAM_RULES_BY_PROGRAM_CODE_AND_OPTIONAL_PROGRAM_CODE_ONLY = "/optionalprogramrules/{programCode}/{optionalProgramCode}";
    public static final String GET_ALL_SPECIFIC_PROGRAM_RULES_BY_RULE = "/programrules/{ruleCode}";
    public static final String GET_ALL_SPECIFIC_PROGRAM_RULES_BY_TRAX_SEQ_NUMBER = "/programrules/by-trax/{traxReqNumber}";
    
    public static final String GET_DATA_FOR_ALGORITHM_MAPPING = "/algorithmdata";
    public static final String GET_DATA_FOR_ALGORITHM_LIST_MAPPING = "/algorithmdatalist";
    
    public static final String GET_ALL_GRAD_CAREER_PROGRAM_MAPPING = "/careerprogram";
    public static final String GET_ALL_GRAD_CAREER_PROGRAM_BY_CODE_MAPPING = "/careerprogram/{cpCode}";
    
    //Default Attribute value constants
    public static final String DEFAULT_CREATED_BY = "API_GRAD_PROGRAM";
    public static final String DEFAULT_UPDATED_BY = "API_GRAD_PROGRAM";

    //Default Date format constants
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    public static final String TRAX_DATE_FORMAT = "yyyyMM";

    // Splunk LogHelper Enabled
    @Value("${splunk.log-helper.enabled}")
    private boolean splunkLogHelperEnabled;

    @Value("${endpoint.keycloak.token-uri}")
    private String tokenUrl;
}
