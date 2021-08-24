package ca.bc.gov.educ.api.program.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class GradProgramAlgorithmData {

	private List<ProgramRequirement> programRules;
	private List<OptionalProgramRequirement> optionalProgramRules;
	private GraduationProgramCode gradProgram;
}
