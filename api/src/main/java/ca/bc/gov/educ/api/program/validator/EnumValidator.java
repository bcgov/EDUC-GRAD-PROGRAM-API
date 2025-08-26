package ca.bc.gov.educ.api.program.validator;


import ca.bc.gov.educ.api.program.constants.v1.AssessmentTypeCodes;
import ca.bc.gov.educ.api.program.validator.constraint.IsAllowedValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class EnumValidator implements ConstraintValidator<IsAllowedValue, String> {

    private String enumName;

    private EnumValidator() {
    }

    @Override
    public void initialize(IsAllowedValue annotation) {
        this.enumName = annotation.enumName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        if(enumName.equals("AssessmentTypeCodes")){
            return AssessmentTypeCodes.findByValue(value).isPresent();
        } else {
            return false;
        }
    }
}
