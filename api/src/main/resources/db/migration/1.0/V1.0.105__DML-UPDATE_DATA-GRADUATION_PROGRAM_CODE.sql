UPDATE API_GRAD_PROGRAM.GRADUATION_PROGRAM_CODE
SET EXPIRY_DATE=TO_TIMESTAMP('2024-06-30 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS.FF3'), UPDATE_USER='API_GRAD_PROGRAM', UPDATE_DATE=SYSTIMESTAMP
WHERE GRADUATION_PROGRAM_CODE='2018-EN';

UPDATE API_GRAD_PROGRAM.GRADUATION_PROGRAM_CODE
SET EXPIRY_DATE=TO_TIMESTAMP('2024-06-30 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS.FF3'), UPDATE_USER='API_GRAD_PROGRAM', UPDATE_DATE=SYSTIMESTAMP
WHERE GRADUATION_PROGRAM_CODE='2018-PF';