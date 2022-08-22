package ca.bc.gov.educ.api.program.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class GradProgramAlgorithmData {

	private String programKey;
	private List<ProgramRequirement> programRules;
	private List<OptionalProgramRequirement> optionalProgramRules;
	private GraduationProgramCode gradProgram;
}
