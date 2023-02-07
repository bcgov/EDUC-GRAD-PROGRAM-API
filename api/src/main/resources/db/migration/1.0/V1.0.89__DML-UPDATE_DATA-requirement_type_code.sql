UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm identifies eligible, elective courses at the grade 12 level for the 1950 program' WHERE REQUIREMENT_TYPE_CODE='MCE12';
UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm identifies eligible, elective courses at the grade 12 level or a Grade 11 Socials Studies course for the 1950 program' WHERE REQUIREMENT_TYPE_CODE='MCEOTHER';
UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm matches a specific course to those listed under a graduation requirement subject area.' WHERE REQUIREMENT_TYPE_CODE='M';
UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm counts eligible course credits at the Grade 12 level for an applicable program' WHERE REQUIREMENT_TYPE_CODE='MC';
UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm counts eligible, elective course credits in a range of grade levels for an applicable program' WHERE REQUIREMENT_TYPE_CODE='MCE';
UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm identifies student courses taken after starting the program. Currently applies to the Adult Graduation Diploma Program only.' WHERE REQUIREMENT_TYPE_CODE='MAC';
UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm matches a specific course to those listed under a rule that restricts the number of work experience courses that can be used to meet a graduation requirement.' WHERE REQUIREMENT_TYPE_CODE='MWEX';
UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm identifies if a program completion date exists for a student on the SCCP program.' WHERE REQUIREMENT_TYPE_CODE='PCD';
UPDATE REQUIREMENT_TYPE_CODE SET DESCRIPTION = 'Algorithm checks if student on the 1950 program has been reported by the school as grade ''AD'' (student on the Adult Graduation Program who is expected to graduate).' WHERE REQUIREMENT_TYPE_CODE='SG';

DELETE FROM REQUIREMENT_TYPE_CODE WHERE REQUIREMENT_TYPE_CODE = 'SR';