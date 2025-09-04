package ca.bc.gov.educ.api.program.model.dto.external.algorithm;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Data
@Component
public class AssessmentRequirementCode {

	private String assmtRequirementCode;
	private String label;
	private String description;
	private Date effectiveDate;
	private Date expiryDate;
}
