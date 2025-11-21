UPDATE PROGRAM_REQUIREMENT_CODE
SET LABEL='Student Grade AD', DESCRIPTION='Student must be in grade AD or GA (on the Adult Graduation program and expected to graduate)', NOT_MET_DESC='The school has not reported the student as being expected to graduate this year (grade must = AD)'
WHERE PROGRAM_REQUIREMENT_CODE='508';