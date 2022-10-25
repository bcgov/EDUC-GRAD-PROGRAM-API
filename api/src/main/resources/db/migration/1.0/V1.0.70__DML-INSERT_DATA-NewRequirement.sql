INSERT INTO REQUIREMENT_TYPE_CODE (REQUIREMENT_TYPE_CODE, LABEL, DESCRIPTION, DISPLAY_ORDER, EFFECTIVE_DATE) VALUES('SG', 'Student Grade', 'Adult student must be in grade AD', 120, '2021-06-14');

INSERT INTO PROGRAM_REQUIREMENT_CODE (PROGRAM_REQUIREMENT_CODE, LABEL, DESCRIPTION, DISPLAY_ORDER, ACTIVE_REQUIREMENT, LANGUAGE_OF_INSTRUCTION, NOT_MET_DESC, REQUIRED_CREDITS, REQUIREMENT_TYPE_CODE) VALUES('508', 'Student Grade AD', 'Adult students must be in grade AD', 508, 'Y', NULL, 'The school has not reported the student as being expected to graduate this year \(grade must = AD\)', '0', 'SG');

INSERT INTO PROGRAM_REQUIREMENT (PROGRAM_REQUIREMENT_ID, GRADUATION_PROGRAM_CODE, PROGRAM_REQUIREMENT_CODE) VALUES('EBDB344B2A7CF91FE05398E9228E9042', '1950', '508');