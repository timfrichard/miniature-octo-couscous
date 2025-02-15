SET SCHEMA 'PUBLIC';
-- Creating Entity tables
DROP TABLE TAS_BETC IF EXISTS;
-- Sequences
CREATE SEQUENCE TAS_BETC_SEQ START WITH 20 INCREMENT BY 10;
CREATE SEQUENCE FILE_UPLOAD_JOB_HEADER_SEQ START WITH 20 INCREMENT BY 10;
CREATE SEQUENCE PROCESSING_ERROR_SEQ START WITH 20 INCREMENT BY 10;
CREATE SEQUENCE FILE_STAGING_SEQ START WITH 20 INCREMENT BY 10;

CREATE TABLE TAS_BETC (
    ID_NUMBER BIGINT DEFAULT NEXT VALUE FOR TAS_BETC_SEQ,
    VERSION_ID BIGINT DEFAULT 0 NOT NULL,
    CMPNT_TAS_SP VARCHAR(25),
    CMPNT_TAS_ATA VARCHAR(25),
    CMPNT_TAS_AID VARCHAR(25),
    CMPNT_TAS_BPOA VARCHAR(25),
    CMPNT_TAS_EPOA VARCHAR(25),
    CMPNT_TAS_A VARCHAR(25),
    CMPNT_TAS_MAIN VARCHAR(25),
    CMPNT_TAS_SUB VARCHAR(25),
    ADMIN_BUR VARCHAR(250),
    GWA_TAS VARCHAR(35),
    GWA_TAS_NAME VARCHAR(250),
    AGENCY_NAME VARCHAR(250),
    BETC VARCHAR(35),
    BETC_NAME VARCHAR(250),
    EFF_DATE VARCHAR(35),
    SUSPEND_DATE VARCHAR(35),
    CREDIT TINYINT,
    ADJ_BETC VARCHAR(35),
    STAR_TAS VARCHAR(35),
    STAR_DEPT_REG VARCHAR(35),
    STAR_DEPT_XFR VARCHAR(35),
    STAR_MAIN_ACCT VARCHAR(35),
    TXN_TYPE VARCHAR(35),
    ACCT_TYPE VARCHAR(35),
    ACCT_TYPE_DESCRIPTION VARCHAR(250),
    FUND_TYPE VARCHAR(35),
    FUND_TYPE_DESCRIPTION VARCHAR(250),
    PROCESS_DATETIME TIMESTAMP,
    FK_FILE_UPLOAD_JOB_HEADER_ID BIGINT NOT NULL,
    PRIMARY KEY (ID_NUMBER)
);
CREATE TABLE FILE_UPLOAD_JOB_HEADER (
    FILE_UPLOAD_JOB_HEADER_ID BIGINT DEFAULT NEXT VALUE FOR FILE_UPLOAD_JOB_HEADER_SEQ,
    VERSION_ID BIGINT DEFAULT 0 NOT NULL,
    FK_JOB_EXECUTION_ID BIGINT,
    READ_COUNT BIGINT,
    EXIT_CODE VARCHAR(2500),
    STATUS VARCHAR(35),
    FILE_NAME VARCHAR(250),
    FILE_UPLOAD_DATETIME TIMESTAMP,
    PRIMARY KEY (FILE_UPLOAD_JOB_HEADER_ID)
);
CREATE TABLE PROCESSING_ERROR (
    PROCESSING_ERROR_ID BIGINT DEFAULT NEXT VALUE FOR PROCESSING_ERROR_SEQ,
    VERSION_ID BIGINT DEFAULT 0 NOT NULL,
    FK_FILE_UPLOAD_JOB_HEADER_ID BIGINT NOT NULL,
    ERROR_DESCRIPTION VARCHAR(2500),
    STEP_TYPE_ERROR VARCHAR(30),
    PRIMARY KEY (PROCESSING_ERROR_ID)
);
CREATE TABLE FILE_STAGING(
    FILE_STAGING_ID BIGINT DEFAULT NEXT VALUE FOR FILE_STAGING_SEQ,
    FILE_LINE_DATA VARCHAR(2500),
    FILE_LINE_NUM INTEGER,
    FK_FILE_UPLOAD_JOB_HEADER_ID BIGINT NOT NULL,
    PRIMARY KEY (FILE_STAGING_ID)
);

ALTER TABLE TAS_BETC
    ADD CONSTRAINT FK_JOB_HEADER_TAS_BETC_CONSTRAINT
    FOREIGN KEY (FK_FILE_UPLOAD_JOB_HEADER_ID)
    REFERENCES FILE_UPLOAD_JOB_HEADER;
ALTER TABLE PROCESSING_ERROR
    ADD CONSTRAINT FK_JOB_HEADER_PROCESSING_ERROR_CONSTRAINT
    FOREIGN KEY (FK_FILE_UPLOAD_JOB_HEADER_ID)
    REFERENCES FILE_UPLOAD_JOB_HEADER;
ALTER TABLE FILE_STAGING
    ADD CONSTRAINT FK_FILE_STAGING_CONSTRAINT
    FOREIGN KEY (FK_FILE_UPLOAD_JOB_HEADER_ID)
    REFERENCES FILE_UPLOAD_JOB_HEADER;

-- ***BEGIN Drop SpringBatch Tables
DROP TABLE  BATCH_STEP_EXECUTION_CONTEXT IF EXISTS;
DROP TABLE  BATCH_JOB_EXECUTION_CONTEXT IF EXISTS;
DROP TABLE  BATCH_STEP_EXECUTION IF EXISTS;
DROP TABLE  BATCH_JOB_EXECUTION_PARAMS IF EXISTS;
DROP TABLE  BATCH_JOB_EXECUTION IF EXISTS;
DROP TABLE  BATCH_JOB_INSTANCE IF EXISTS;

DROP SEQUENCE  BATCH_STEP_EXECUTION_SEQ IF EXISTS;
DROP SEQUENCE  BATCH_JOB_EXECUTION_SEQ IF EXISTS;
DROP SEQUENCE  BATCH_JOB_SEQ IF EXISTS;
-- **END Drop SpringBatch Tables


-- **BEGIN Create SpringBatch Tables
CREATE TABLE BATCH_JOB_INSTANCE  (
	JOB_INSTANCE_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
	VERSION BIGINT ,
	JOB_NAME VARCHAR(100) NOT NULL,
	JOB_KEY VARCHAR(32) NOT NULL,
	constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ;

CREATE TABLE BATCH_JOB_EXECUTION  (
	JOB_EXECUTION_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
	VERSION BIGINT  ,
	JOB_INSTANCE_ID BIGINT NOT NULL,
	CREATE_TIME TIMESTAMP(9) NOT NULL,
	START_TIME TIMESTAMP(9) DEFAULT NULL ,
	END_TIME TIMESTAMP(9) DEFAULT NULL ,
	STATUS VARCHAR(10) ,
	EXIT_CODE VARCHAR(2500) ,
	EXIT_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED TIMESTAMP(9),
	constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
	references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID BIGINT NOT NULL ,
	PARAMETER_NAME VARCHAR(100) NOT NULL ,
	PARAMETER_TYPE VARCHAR(100) NOT NULL ,
	PARAMETER_VALUE VARCHAR(2500) ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION  (
	STEP_EXECUTION_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
	VERSION BIGINT NOT NULL,
	STEP_NAME VARCHAR(100) NOT NULL,
	JOB_EXECUTION_ID BIGINT NOT NULL,
	CREATE_TIME TIMESTAMP(9) NOT NULL,
	START_TIME TIMESTAMP(9) DEFAULT NULL ,
	END_TIME TIMESTAMP(9) DEFAULT NULL ,
	STATUS VARCHAR(10) ,
	COMMIT_COUNT BIGINT ,
	READ_COUNT BIGINT ,
	FILTER_COUNT BIGINT ,
	WRITE_COUNT BIGINT ,
	READ_SKIP_COUNT BIGINT ,
	WRITE_SKIP_COUNT BIGINT ,
	PROCESS_SKIP_COUNT BIGINT ,
	ROLLBACK_COUNT BIGINT ,
	EXIT_CODE VARCHAR(2500) ,
	EXIT_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED TIMESTAMP(9),
	constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
	STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT LONGVARCHAR ,
	constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
	references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
	JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT LONGVARCHAR ,
	constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_SEQ;
--  ***END Create SpringBatch Tables
