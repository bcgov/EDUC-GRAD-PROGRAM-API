-- PROGRAM_REQUIREMENT definition

CREATE TABLE "PROGRAM_REQUIREMENT" 
   (  "PROGRAM_REQUIREMENT_ID" RAW(16) DEFAULT SYS_GUID(), 
	"GRADUATION_PROGRAM_CODE" VARCHAR2(20) NOT NULL ENABLE,
	"PROGRAM_REQUIREMENT_CODE" VARCHAR2(4) NOT NULL ENABLE,
	"CREATE_USER" VARCHAR2(32) DEFAULT USER NOT NULL ENABLE,
	"CREATE_DATE" DATE DEFAULT SYSTIMESTAMP NOT NULL ENABLE,
	"UPDATE_USER" VARCHAR2(32) DEFAULT USER NOT NULL ENABLE,
	"UPDATE_DATE" DATE DEFAULT SYSTIMESTAMP NOT NULL ENABLE,
   CONSTRAINT "PROGRAM_REQUIREMENT_PK" PRIMARY KEY ("PROGRAM_REQUIREMENT_ID")
  USING INDEX TABLESPACE "API_GRAD_IDX"  ENABLE, 
   CONSTRAINT "GRAD_PGMCD_PGM_RQMT_FK" FOREIGN KEY ("GRADUATION_PROGRAM_CODE")
    REFERENCES "GRADUATION_PROGRAM_CODE" ("GRADUATION_PROGRAM_CODE") ENABLE, 
   CONSTRAINT "PGM_RQMTCD_PGM_RQMT_FK" FOREIGN KEY ("PROGRAM_REQUIREMENT_CODE")
    REFERENCES "PROGRAM_REQUIREMENT_CODE" ("PROGRAM_REQUIREMENT_CODE") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
 NOCOMPRESS LOGGING
  TABLESPACE "API_GRAD_DATA"   NO INMEMORY ;
