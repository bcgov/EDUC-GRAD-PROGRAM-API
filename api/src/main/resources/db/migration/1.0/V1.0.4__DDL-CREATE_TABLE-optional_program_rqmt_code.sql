-- OPTIONAL_PROGRAM_RQMT_CODE definition

CREATE TABLE "OPTIONAL_PROGRAM_RQMT_CODE" 
   (	"OPTIONAL_PROGRAM_RQMT_CODE" VARCHAR2(4), 
	"LABEL" VARCHAR2(60) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(255) NOT NULL ENABLE,
	"DISPLAY_ORDER" NUMBER NOT NULL ENABLE,
	"ACTIVE_REQUIREMENT" VARCHAR2(1) NOT NULL ENABLE,
	"LANGUAGE_OF_INSTRUCTION" VARCHAR2(20),
	"NOT_MET_DESC" VARCHAR2(150),
	"REQUIRED_CREDITS" VARCHAR2(3),
	"REQUIRED_LEVEL" VARCHAR2(10),
	"REQUIREMENT_CATEGORY" VARCHAR2(1),
	"REQUIREMENT_TYPE_CODE" VARCHAR2(20) NOT NULL ENABLE,
	"CREATE_USER" VARCHAR2(32) DEFAULT USER NOT NULL ENABLE,
	"CREATE_DATE" DATE DEFAULT SYSTIMESTAMP NOT NULL ENABLE,
	"UPDATE_USER" VARCHAR2(32) DEFAULT USER NOT NULL ENABLE,
	"UPDATE_DATE" DATE DEFAULT SYSTIMESTAMP NOT NULL ENABLE,
	 CONSTRAINT "OPTIONAL_PROGRAM_RQMT_CODE_PK" PRIMARY KEY ("OPTIONAL_PROGRAM_RQMT_CODE")
  USING INDEX TABLESPACE "API_GRAD_IDX"  ENABLE, 
	 CONSTRAINT "RQMT_TYCD_OPT_PGM_RQMTCD_FK" FOREIGN KEY ("REQUIREMENT_TYPE_CODE")
	  REFERENCES "REQUIREMENT_TYPE_CODE" ("REQUIREMENT_TYPE_CODE") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
 NOCOMPRESS LOGGING
  TABLESPACE "API_GRAD_DATA"   NO INMEMORY ;