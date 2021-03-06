--------------------------------------------------------
--  SCHEDULER DDL
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Table AweSchCal
--  Calendar list
--------------------------------------------------------
CREATE TABLE "AWESCHCAL"
(
    "IDE" NUMBER(*, 0),
    "DES" VARCHAR2(250 BYTE),
    "ACT" NUMBER(*, 0) DEFAULT 1,
    "NOM" VARCHAR2(100 BYTE)
);

--------------------------------------------------------
--  DDL for Table AweSchCalDat
--  Calendar dates
--------------------------------------------------------
CREATE TABLE "AWESCHTSKFILMOD"
(
    "IDETSK" NUMBER(*, 0),
    "FILPTH" VARCHAR2(256 BYTE),
    "MODDAT" DATE
);

--------------------------------------------------------
--  DDL for Table AweSchTskFilMod
--  Task file
--------------------------------------------------------
CREATE TABLE "AWESCHCALDAT"
(
    "IDE"    NUMBER(*, 0),
    "IDECAL" NUMBER(*, 0),
    "NOM"    VARCHAR2(40 BYTE),
    "DAT"    DATE
);

--------------------------------------------------------
--  DDL for Table AweSchExe
--  Task executions
--------------------------------------------------------
CREATE TABLE "AWESCHEXE"
(
    "IDETSK" NUMBER(*, 0),
    "GRPTSK" VARCHAR2(40 BYTE),
    "EXETSK" NUMBER(*, 0),
    "INIDAT" timestamp,
    "ENDDAT" timestamp,
    "EXETIM" NUMBER(*, 0),
    "STA"    NUMBER(*, 0),
    "LCHBY" VARCHAR2(200 BYTE),
    "DES"   VARCHAR2(2000 BYTE)
);

--------------------------------------------------------
--  DDL for Table AweSchSrv
--  Scheduler servers
--------------------------------------------------------
CREATE TABLE "AWESCHSRV"
(
    "IDE" NUMBER(*, 0),
    "NOM" VARCHAR2(40 BYTE),
    "PRO" VARCHAR2(10 BYTE),
    "HST" VARCHAR2(40 BYTE),
    "PRT" VARCHAR2(10 BYTE),
    "ACT" NUMBER(*, 0) DEFAULT 1
);

--------------------------------------------------------
--  DDL for Table AweSchTsk
--  Scheduler tasks
--------------------------------------------------------
CREATE TABLE "AWESCHTSK"
(
    "IDE"       NUMBER(*, 0),
    "NAM"       VARCHAR2(40 BYTE),
    "DES"       VARCHAR2(250 BYTE),
    "NUMSTOEXE" NUMBER(*, 0),
    "TIMOUTEXE" NUMBER(*, 0),
    "TYPEXE"    NUMBER(*, 0),
    "IDESRVEXE" NUMBER(*, 0),
    "CMDEXE"    VARCHAR2(250 BYTE),
    "TYPLCH"    NUMBER(*, 0),
    "LCHDEPERR" NUMBER(*, 0) DEFAULT 0,
    "LCHDEPWRN" NUMBER(*, 0) DEFAULT 0,
    "LCHSETWRN" NUMBER(*, 0) DEFAULT 0,
    "REPTYP"    NUMBER(*, 0) DEFAULT 0,
    "REPEMASRV" NUMBER(*, 0),
    "REPSNDSTA" VARCHAR2(20 BYTE),
    "REPEMADST" VARCHAR2(250 BYTE),
    "REPTIT"    VARCHAR2(100 BYTE),
    "REPMSG"    VARCHAR2(250 BYTE),
    "ACT"       NUMBER(*, 0) DEFAULT 1,
    "REPUSRDST" VARCHAR2(250 BYTE),
    "REPMNTID"  VARCHAR2(200 BYTE),
    "CMDEXEPTH" VARCHAR2(200 BYTE)
);

--------------------------------------------------------
--  DDL for Table AweSchTskDpn
--  Task dependencies
--------------------------------------------------------
CREATE TABLE "AWESCHTSKDPN"
(
    "IDETSK" NUMBER(*, 0),
    "IDEPRN" NUMBER(*, 0),
    "ISBLK"  NUMBER(*, 0),
    "DPNORD" NUMBER(*, 0)
);

--------------------------------------------------------
--  DDL for Table AweSchTskLch
--  Task launchers
--------------------------------------------------------
CREATE TABLE "AWESCHTSKLCH"
(
    "IDE"      NUMBER(*, 0),
    "IDETSK"   NUMBER(*, 0),
    "RPTNUM"   NUMBER(*, 0),
    "RPTTYP"   NUMBER(*, 0),
    "INIDAT"   DATE,
    "ENDDAT"   DATE,
    "INITIM"   VARCHAR2(8 BYTE),
    "ENDTIM"   VARCHAR2(8 BYTE),
    "IDECAL"   NUMBER(*, 0),
    "IDSRV"    NUMBER(*, 0),
    "PTH"      VARCHAR2(250 BYTE),
    "PAT"      VARCHAR2(250 BYTE),
    "EXEHRS"   VARCHAR2(200 BYTE),
    "EXEMTH"   VARCHAR2(200 BYTE),
    "EXEWEK"   VARCHAR2(200 BYTE),
    "EXEDAY"   VARCHAR2(200 BYTE),
    "EXEDTE"   DATE,
    "EXETIM"   VARCHAR2(8 BYTE),
    "WEEKDAYS" VARCHAR2(200 BYTE),
    "EXEYRS"   VARCHAR2(200 BYTE),
    "EXEMIN"   VARCHAR2(200 BYTE),
    "EXESEC"   VARCHAR2(200 BYTE),
    "SRVUSR"   VARCHAR2(200 BYTE),
    "SRVPWD"   VARCHAR2(200 BYTE)
);

--------------------------------------------------------
--  DDL for Table AweSchTskPar
--  Task parameters
--------------------------------------------------------
CREATE TABLE "AWESCHTSKPAR"
(
    "IDE"    NUMBER(*, 0),
    "IDETSK" NUMBER(*, 0),
    "NAM"    VARCHAR2(40 BYTE),
    "VAL"    VARCHAR2(400 BYTE),
    "SRC"    NUMBER(*, 0),
    "TYP"    VARCHAR2(100 BYTE)
);

--------------------------------------------------------
--  DDL for HISTORIC TABLES
--------------------------------------------------------

CREATE TABLE "HISAWESCHCAL"
(
    "HISOPE" VARCHAR2(20 BYTE),
    "HISDAT" DATE,
    "HISACT" VARCHAR2(1 BYTE),
    "IDE"    NUMBER(*, 0),
    "NOM"    VARCHAR2(40 BYTE),
    "DES"    VARCHAR2(250 BYTE),
    "ACT"    NUMBER(*, 0)
);

CREATE TABLE "HISAWESCHCALDAT"
(
    "HISOPE" VARCHAR2(20 BYTE),
    "HISDAT" DATE,
    "HISACT" VARCHAR2(1 BYTE),
    "IDE"    NUMBER(*, 0),
    "IDECAL" NUMBER(*, 0),
    "NOM"    VARCHAR2(40 BYTE),
    "DAT"    DATE
);

CREATE TABLE "HISAWESCHSRV"
(
    "HISOPE" VARCHAR2(20 BYTE),
    "HISDAT" DATE,
    "HISACT" VARCHAR2(1 BYTE),
    "IDE"    NUMBER(*, 0),
    "NOM"    VARCHAR2(40 BYTE),
    "PRO"    VARCHAR2(10 BYTE),
    "HST"    VARCHAR2(40 BYTE),
    "PRT"    VARCHAR2(10 BYTE),
    "ACT"    NUMBER(*, 0)
);

CREATE TABLE "HISAWESCHTSK"
(
    "HISOPE"    VARCHAR2(20 BYTE),
    "HISDAT"    DATE,
    "HISACT"    VARCHAR2(1 BYTE),
    "IDE"       NUMBER(*, 0),
    "IDEPAR"    NUMBER(*, 0),
    "NAM"       VARCHAR2(40 BYTE),
    "DES"       VARCHAR2(250 BYTE),
    "NUMSTOEXE" NUMBER(*, 0),
    "TIMOUTEXE" NUMBER(*, 0),
    "TYPEXE"    NUMBER(*, 0),
    "IDESRVEXE" NUMBER(*, 0),
    "CMDEXE"    VARCHAR2(250 BYTE),
    "TYPLCH"    NUMBER(*, 0),
    "LCHDEPERR" NUMBER(*, 0),
    "LCHDEPWRN" NUMBER(*, 0),
    "LCHSETWRN" NUMBER(*, 0),
    "BLKPAR"    NUMBER(*, 0),
    "REPTYP"    NUMBER(*, 0),
    "REPEMASRV" NUMBER(*, 0),
    "REPSNDSTA" VARCHAR2(20 BYTE),
    "REPEMADST" VARCHAR2(250 BYTE),
    "REPTIT"    VARCHAR2(100 BYTE),
    "REPMSG"    VARCHAR2(250 BYTE),
    "ACT"       NUMBER(*, 0),
    "REPUSRDST" VARCHAR2(250 BYTE),
    "REPMNTID"  VARCHAR2(200 BYTE),
    "CMDEXEPTH" VARCHAR(200 BYTE)
);

CREATE TABLE "HISAWESCHTSKLCH"
(
    "HISOPE"   VARCHAR2(20 BYTE),
    "HISDAT"   DATE,
    "HISACT"   VARCHAR2(1 BYTE),
    "IDE"      NUMBER(*, 0),
    "IDETSK"   NUMBER(*, 0),
    "RPTNUM"   NUMBER(*, 0),
    "RPTTYP"   NUMBER(*, 0),
    "INIDAT"   DATE,
    "ENDDAT"   DATE,
    "INITIM"   VARCHAR2(8 BYTE),
    "ENDTIM"   VARCHAR2(8 BYTE),
    "IDECAL"   NUMBER(*, 0),
    "IDSRV"    NUMBER(*, 0),
    "PTH"      VARCHAR2(250 BYTE),
    "PAT"      VARCHAR2(250 BYTE),
    "EXEMTH"   VARCHAR2(200 BYTE),
    "EXEWEK"   VARCHAR2(200 BYTE),
    "EXEDAY"   VARCHAR2(200 BYTE),
    "EXEHRS"   VARCHAR2(200 BYTE),
    "EXEDTE"   DATE,
    "EXETIM"   VARCHAR2(8 BYTE),
    "WEEKDAYS" VARCHAR2(200 BYTE),
    "EXEYRS"   VARCHAR2(200 BYTE),
    "EXEMIN"   VARCHAR2(200 BYTE),
    "EXESEC"   VARCHAR2(200 BYTE),
    "SRVUSR"   VARCHAR2(200 BYTE),
    "SRVPWD"   VARCHAR2(200 BYTE)
);

CREATE TABLE "HISAWESCHTSKPAR"
(
    "HISOPE" VARCHAR2(20 BYTE),
    "HISDAT" DATE,
    "HISACT" VARCHAR2(1 BYTE),
    "IDE"    NUMBER(*, 0),
    "IDETSK" NUMBER(*, 0),
    "NAM"    VARCHAR2(40 BYTE),
    "VAL"    VARCHAR2(400 BYTE),
    "SRC"    NUMBER(*, 0),
    "TYP"    VARCHAR2(100 BYTE)
);

--------------------------------------------------------
--  DDL for CONSTRAINTS
--------------------------------------------------------

CREATE UNIQUE INDEX "NOM_UQ" ON "AWESCHCAL" ("NOM");
CREATE UNIQUE INDEX "PK_AWESCHCAL" ON "AWESCHCAL" ("IDE");
CREATE UNIQUE INDEX "PK_AWESCHCALDAT" ON "AWESCHCALDAT" ("IDE");
CREATE INDEX "AWESCHEXEI1" ON "AWESCHEXE" ("IDETSK", "GRPTSK", "EXETSK", "INIDAT");
CREATE UNIQUE INDEX "PK_AWESCHSRV" ON "AWESCHSRV" ("IDE");
CREATE UNIQUE INDEX "PK_AWESCHTSK" ON "AWESCHTSK" ("IDE");
CREATE UNIQUE INDEX "SYS_C00164575" ON "AWESCHTSKDPN" ("IDETSK", "IDEPRN");
CREATE UNIQUE INDEX "PK_AWESCHTSKLCH" ON "AWESCHTSKLCH" ("IDE");
CREATE UNIQUE INDEX "PK_AWESCHTSKPAR" ON "AWESCHTSKPAR" ("IDE");
CREATE INDEX "HISAWESCHCALI1" ON "HISAWESCHCAL" ("HISOPE", "HISDAT", "HISACT");
CREATE INDEX "HISAWESCHCALDATI1" ON "HISAWESCHCALDAT" ("HISOPE", "HISDAT", "HISACT");
CREATE INDEX "HISAWESCHSRVI1" ON "HISAWESCHSRV" ("HISOPE", "HISDAT", "HISACT");
CREATE INDEX "HISAWESCHTSKI1" ON "HISAWESCHTSK" ("HISOPE", "HISDAT", "HISACT");
CREATE INDEX "HISAWESCHTSKLCHI1" ON "HISAWESCHTSKLCH" ("HISOPE", "HISDAT", "HISACT");
CREATE INDEX "HISAWESCHTSKPARI1" ON "HISAWESCHTSKPAR" ("HISOPE", "HISDAT", "HISACT");
ALTER TABLE "AWESCHCAL"
    ADD CONSTRAINT "NOM_UQ" UNIQUE ("NOM");
ALTER TABLE "AWESCHCAL"
    ADD CONSTRAINT "PK_AWESCHCAL" PRIMARY KEY ("IDE");
ALTER TABLE "AWESCHCAL"
    MODIFY ("IDE" NOT NULL ENABLE);
ALTER TABLE "AWESCHCAL"
    MODIFY ("DES" NOT NULL ENABLE);
ALTER TABLE "AWESCHCAL"
    MODIFY ("ACT" NOT NULL ENABLE);
ALTER TABLE "AWESCHCAL"
    MODIFY ("NOM" NOT NULL ENABLE);
ALTER TABLE "AWESCHCALDAT"
    ADD CONSTRAINT "PK_AWESCHCALDAT" PRIMARY KEY ("IDE");
ALTER TABLE "AWESCHTSKFILMOD"
    ADD PRIMARY KEY ("IDETSK", "FILPTH")
ALTER TABLE "AWESCHTSKFILMOD"
    MODIFY ("IDETSK" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSKFILMOD"
    MODIFY ("FILPTH" NOT NULL ENABLE);
ALTER TABLE "AWESCHCALDAT"
    MODIFY ("IDE" NOT NULL ENABLE);
ALTER TABLE "AWESCHCALDAT"
    MODIFY ("IDECAL" NOT NULL ENABLE);
ALTER TABLE "AWESCHCALDAT"
    MODIFY ("NOM" NOT NULL ENABLE);
ALTER TABLE "AWESCHCALDAT"
    MODIFY ("DAT" NOT NULL ENABLE);
ALTER TABLE "AWESCHEXE"
    MODIFY ("IDETSK" NOT NULL ENABLE);
ALTER TABLE "AWESCHEXE"
    MODIFY ("GRPTSK" NOT NULL ENABLE);
ALTER TABLE "AWESCHEXE"
    MODIFY ("EXETSK" NOT NULL ENABLE);
ALTER TABLE "AWESCHEXE"
    MODIFY ("INIDAT" NOT NULL ENABLE);
ALTER TABLE "AWESCHEXE"
    MODIFY ("STA" NOT NULL ENABLE);
ALTER TABLE "AWESCHSRV"
    ADD CONSTRAINT "PK_AWESCHSRV" PRIMARY KEY ("IDE");
ALTER TABLE "AWESCHSRV"
    MODIFY ("IDE" NOT NULL ENABLE);
ALTER TABLE "AWESCHSRV"
    MODIFY ("NOM" NOT NULL ENABLE);
ALTER TABLE "AWESCHSRV"
    MODIFY ("PRO" NOT NULL ENABLE);
ALTER TABLE "AWESCHSRV"
    MODIFY ("HST" NOT NULL ENABLE);
ALTER TABLE "AWESCHSRV"
    MODIFY ("PRT" NOT NULL ENABLE);
ALTER TABLE "AWESCHSRV"
    MODIFY ("ACT" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    ADD CONSTRAINT "PK_AWESCHTSK" PRIMARY KEY ("IDE");
ALTER TABLE "AWESCHTSK"
    MODIFY ("IDE" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("NAM" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("TYPEXE" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("CMDEXE" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("TYPLCH" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("LCHDEPERR" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("LCHDEPWRN" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("LCHSETWRN" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("REPTYP" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSK"
    MODIFY ("ACT" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSKDPN"
    MODIFY ("IDETSK" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSKDPN"
    MODIFY ("IDEPRN" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSKDPN"
    ADD PRIMARY KEY ("IDETSK", "IDEPRN");
ALTER TABLE "AWESCHTSKLCH"
    ADD CONSTRAINT "PK_AWESCHTSKLCH" PRIMARY KEY ("IDE");
ALTER TABLE "AWESCHTSKLCH"
    MODIFY ("IDE" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSKPAR"
    ADD CONSTRAINT "PK_AWESCHTSKPAR" PRIMARY KEY ("IDE");
ALTER TABLE "AWESCHTSKPAR"
    MODIFY ("IDE" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSKPAR"
    MODIFY ("NAM" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSKPAR"
    MODIFY ("SRC" NOT NULL ENABLE);
ALTER TABLE "AWESCHTSKPAR"
    MODIFY ("TYP" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHCAL"
    MODIFY ("HISOPE" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHCAL"
    MODIFY ("HISDAT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHCAL"
    MODIFY ("HISACT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHCALDAT"
    MODIFY ("HISOPE" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHCALDAT"
    MODIFY ("HISDAT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHCALDAT"
    MODIFY ("HISACT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHSRV"
    MODIFY ("HISOPE" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHSRV"
    MODIFY ("HISDAT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHSRV"
    MODIFY ("HISACT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSK"
    MODIFY ("HISOPE" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSK"
    MODIFY ("HISDAT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSK"
    MODIFY ("HISACT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSKLCH"
    MODIFY ("HISOPE" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSKLCH"
    MODIFY ("HISDAT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSKLCH"
    MODIFY ("HISACT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSKPAR"
    MODIFY ("HISOPE" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSKPAR"
    MODIFY ("HISDAT" NOT NULL ENABLE);
ALTER TABLE "HISAWESCHTSKPAR"
    MODIFY ("HISACT" NOT NULL ENABLE);

-- Scheduler sequences
Insert into AweKey (KeyNam, KeyVal) values ('SchTskSrv', (select coalesce(max(Ide),0) + 1 from AweSchSrv));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskCal', (select coalesce(max(Ide),0) + 1 from AweSchCal));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskCalDat', (select coalesce(max(Ide),0) + 1 from AweSchCalDat));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskKey', (select coalesce(max(Ide),0) + 1 from AweSchTsk));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskLch', (select coalesce(max(Ide),0) + 1 from AweSchTskLch));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskPar', (select coalesce(max(Ide),0) + 1 from AweSchTskPar));