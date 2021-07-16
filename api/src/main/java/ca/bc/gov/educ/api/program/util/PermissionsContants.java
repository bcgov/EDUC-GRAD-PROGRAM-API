package ca.bc.gov.educ.api.program.util;

public interface PermissionsContants {
	String _PREFIX = "#oauth2.hasAnyScope('";
	String _SUFFIX = "')";

	String READ_GRAD_PROGRAM = _PREFIX + "READ_GRAD_PROGRAM_CODE_DATA" + _SUFFIX;
	String CREATE_GRAD_PROGRAM = _PREFIX + "CREATE_GRAD_PROGRAM_CODE_DATA" + _SUFFIX;
	String UPDATE_GRAD_PROGRAM = _PREFIX + "UPDATE_GRAD_PROGRAM_CODE_DATA" + _SUFFIX;
	String READ_GRAD_PROGRAM_SET = _PREFIX + "READ_GRAD_PROGRAM_SETS_DATA" + _SUFFIX;
	String DELETE_GRAD_PROGRAM = _PREFIX + "DELETE_GRAD_PROGRAM_CODE_DATA" + _SUFFIX;
	String READ_GRAD_PROGRAM_RULES = _PREFIX + "READ_GRAD_PROGRAM_RULES_DATA" + _SUFFIX;
	String READ_GRAD_SPECIAL_CASE = _PREFIX + "READ_GRAD_SPECIAL_CASE_DATA" + _SUFFIX;
	String READ_GRAD_LETTER_GRADE = _PREFIX + "READ_GRAD_LETTER_GRADE_DATA" + _SUFFIX;
	String CREATE_GRAD_PROGRAM_SET = _PREFIX + "CREATE_GRAD_PROGRAM_SETS_DATA" + _SUFFIX;
	String UPDATE_GRAD_PROGRAM_SET = _PREFIX + "UPDATE_GRAD_PROGRAM_SETS_DATA" + _SUFFIX;
	String DELETE_GRAD_PROGRAM_SET = _PREFIX + "DELETE_GRAD_PROGRAM_SETS_DATA" + _SUFFIX;
	String CREATE_GRAD_PROGRAM_RULES = _PREFIX + "CREATE_GRAD_PROGRAM_RULES_DATA" + _SUFFIX;
	String UPDATE_GRAD_PROGRAM_RULES = _PREFIX + "UPDATE_GRAD_PROGRAM_RULES_DATA" + _SUFFIX;
	String DELETE_GRAD_PROGRAM_RULES = _PREFIX + "DELETE_GRAD_PROGRAM_RULES_DATA" + _SUFFIX;
	String READ_GRAD_SPECIAL_PROGRAM = _PREFIX + "READ_GRAD_SPECIAL_PROGRAM_CODE_DATA" + _SUFFIX;
	String CREATE_GRAD_SPECIAL_PROGRAM = _PREFIX + "CREATE_GRAD_SPECIAL_PROGRAM_CODE_DATA" + _SUFFIX;
	String UPDATE_GRAD_SPECIAL_PROGRAM = _PREFIX + "UPDATE_GRAD_SPECIAL_PROGRAM_CODE_DATA" + _SUFFIX;
	String DELETE_GRAD_SPECIAL_PROGRAM = _PREFIX + "DELETE_GRAD_SPECIAL_PROGRAM_CODE_DATA" + _SUFFIX;
	String READ_GRAD_SPECIAL_PROGRAM_RULES = _PREFIX + "READ_GRAD_SPECIAL_PROGRAM_RULES_DATA" + _SUFFIX;
	String CREATE_GRAD_SPECIAL_PROGRAM_RULES = _PREFIX + "CREATE_GRAD_SPECIAL_PROGRAM_RULES_DATA" + _SUFFIX;
	String UPDATE_GRAD_SPECIAL_PROGRAM_RULES = _PREFIX + "UPDATE_GRAD_SPECIAL_PROGRAM_RULES_DATA" + _SUFFIX;
	String DELETE_GRAD_SPECIAL_PROGRAM_RULES = _PREFIX + "DELETE_GRAD_SPECIAL_PROGRAM_RULES_DATA" + _SUFFIX;
}
