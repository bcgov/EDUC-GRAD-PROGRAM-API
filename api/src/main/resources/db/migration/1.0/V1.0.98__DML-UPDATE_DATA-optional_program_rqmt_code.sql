UPDATE OPTIONAL_PROGRAM_RQMT_CODE SET LABEL = REPLACE(LABEL, 'Certificate', 'Course'), DESCRIPTION = REPLACE(DESCRIPTION, 'Certificate', 'Course'), UPDATE_USER = 'GRAD2-2385', UPDATE_DATE = SYSDATE where OPTIONAL_PROGRAM_RQMT_CODE = '952';
COMMIT;


