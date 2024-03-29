-- OPTIONAL_PROGRAM definition

CREATE TABLE "OPTIONAL_PROGRAM"
   (	"OPTIONAL_PROGRAM_ID" RAW(16) DEFAULT SYS_GUID(),
    "OPTIONAL_PROGRAM_CODE" VARCHAR2(8) NOT NULL ENABLE,
	"GRADUATION_PROGRAM_CODE" VARCHAR2(20) NOT NULL ENABLE,
	"LABEL" VARCHAR2(100) NOT NULL ENABLE,
	"DESCRIPTION" VARCHAR2(255) NOT NULL ENABLE,
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE,
	"EXPIRY_DATE" DATE,
	"DISPLAY_ORDER" NUMBER NOT NULL ENABLE,
	"CREATE_USER" VARCHAR2(30) DEFAULT USER NOT NULL ENABLE,
	"CREATE_DATE" DATE DEFAULT SYSTIMESTAMP NOT NULL ENABLE,
	"UPDATE_USER" VARCHAR2(30) DEFAULT USER NOT NULL ENABLE,
	"UPDATE_DATE" DATE DEFAULT SYSTIMESTAMP NOT NULL ENABLE,
	 CONSTRAINT "OPTIONAL_PROGRAM_PK" PRIMARY KEY ("OPTIONAL_PROGRAM_ID")
  USING INDEX TABLESPACE "API_GRAD_IDX"  ENABLE,
	 CONSTRAINT "OPT_PGM_OPT_PGMCD_FK" FOREIGN KEY ("OPTIONAL_PROGRAM_CODE")
	  REFERENCES "OPTIONAL_PROGRAM_CODE" ("OPTIONAL_PROGRAM_CODE") ENABLE,
	 CONSTRAINT "OPT_PGM_GRAD_PGMCD_FK" FOREIGN KEY ("GRADUATION_PROGRAM_CODE")
	  REFERENCES "GRADUATION_PROGRAM_CODE" ("GRADUATION_PROGRAM_CODE") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
 NOCOMPRESS LOGGING
  TABLESPACE "API_GRAD_DATA"   NO INMEMORY ;
