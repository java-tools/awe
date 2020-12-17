--------------------------------------------------------
--  DDL for Schema AWE
--------------------------------------------------------
CREATE SCHEMA AWE;

--------------------------------------------------------
--  DDL for Table AweAppPar
--  Application parameters table: Allows to configure specific parameters in the application
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweAppPar (
IdeAweAppPar int NOT NULL PRIMARY KEY, --- Table identifier
ParNam varchar(40) NOT NULL, --- Parameter name
ParVal varchar(60) NULL, ---  Parameter value
Cat int NOT NULL, ---  Parameter category: General (1), Reporting (2), Security (3)
Des varchar(250) NULL, --- Parameter description
Act int DEFAULT 1 NOT NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweAppParI1 ON AweAppPar (ParNam);

--------------------------------------------------------
--  DDL for Table AweThm
--  Themes table: List of available themes
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweThm (
IdeThm int NOT NULL PRIMARY KEY, --- Theme key
Nam varchar(100) not NULL, --- Theme name
Act int default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweThmI1 ON AweThm (Nam);

--------------------------------------------------------
--  DDL for Table AwePro
--  Profiles table: List of application profiles
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AwePro (
IdePro int NOT NULL PRIMARY KEY , --- Profile key
Acr varchar(3) not NULL, --- Profile acronym (3 chars)
Nam varchar(120) not NULL, --- Profile name
IdeThm int NULL, --- Default theme identifier for profile
ScrIni varchar(40) NULL, --- Initial screen for profile
Res varchar(40) NULL, --- Profile restriction file (listed on profile folder)
Act int default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweProI1 ON AwePro (Nam);

--------------------------------------------------------
--  DDL for Table ope
--  Operators table: List of application users
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS ope (
IdeOpe int NOT NULL PRIMARY KEY, --- Operator key
l1_nom char(20), --- User name
l1_pas char(40), --- User password hash
OpePas char(200), --- User password hash (old)
l1_con int DEFAULT 0, --- Connected (1) or not (0)
l1_dev char(3), --- unused
l1_act int DEFAULT 1, --- Active (1) or not (0)
l1_trt char(1), --- unused
l1_uti int, --- unused
l1_opr char(6), --- unused
l1_dat DATE, --- Last connection date
imp_nom char(32) DEFAULT 'none',
dat_mod datetime, --- User update date
l1_psd datetime, --- Date of password expiration
l1_lan char(3), --- User language
l1_sgn int, --- User signed
PcPrn varchar(255), --- User printer
EmlSrv varchar(10), --- Email server
EmlAdr varchar(50), --- Email address
OpeNam varchar(50), --- User full name
IdePro int, --- User profile
IdeThm int, --- User theme
ScrIni varchar(40), --- User initial screen
Res varchar(40), --- User specific restriction profile
ShwPrn int, --- Allow user to print (1) or not (0)
WebPrn varchar(255), --- User web printer
PwdLck int DEFAULT 0, --- Password locked (1) or not (0)
NumLog int DEFAULT 0 --- Number of times logged in concurrently
);
CREATE UNIQUE INDEX opeI1 ON ope (l1_nom);

--------------------------------------------------------
--  DDL for Table AweDbs
--  Database table: List of application database connections
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweDbs (
IdeDbs int NOT NULL PRIMARY KEY, --- Database key
Als varchar(16) not NULL, --- Database alias
Des varchar(40) NULL, --- Database description
Dct varchar(1) not NULL, --- Database connection type: (J) JDBC, (D) Datasource
Dbt varchar(10) not NULL, --- Database type (ora) Oracle, (sqs) SQL Server, (hsql) HSQLDB, (h2) H2 Database, (mysql) MySQL/MariaDB
Drv varchar(256), --- Database driver
DbsUsr varchar(50), --- Database username
DbsPwd varchar(50), --- Database password (encrypted)
Typ varchar(3) not NULL, --- Database environment: (Des) Development, (Pre) Pre-production, (Pro) Production
Dbc varchar(256) not NULL, --- Database connection: JDBC database connection URL
Act int default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweDbsI1 ON AweDbs (Als);

--------------------------------------------------------
--  DDL for Table AweSit
--  Sites table: List of available application sites
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSit (
IdeSit int NOT NULL PRIMARY KEY, --- Site key
Nam varchar(100) NOT NULL, --- Site name
Ord int NULL, --- Site order (in selector)
Act int default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweSitI1 ON AweSit (Nam);

--------------------------------------------------------
--  DDL for Table AweMod
--  Module table: List of awe modules
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweMod (
IdeMod int NOT NULL PRIMARY KEY, --- Module key
Nam varchar(100) not NULL, --- Module name
ScrIni varchar(40) NULL, --- Module initial screen (deprecated)
IdeThm int NULL, --- Module theme (deprecated)
Act int default 1 not NULL, --- Active (1) or not (0)
Ord int NULL  --- value to recover modules sorted as convenience
);
CREATE UNIQUE INDEX AweModI1 ON AweMod (Nam);

--------------------------------------------------------
--  DDL for Table AweSitModDbs
--  Sites-Modules-Databases relationship table
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSitModDbs (
IdeSitModDbs int NOT NULL PRIMARY KEY, --- Relationship key
IdeSit int NOT NULL, --- Site key
IdeMod int NOT NULL, --- Module key
IdeDbs int NOT NULL, --- Database key
Ord int NULL --- Relationship order
);
CREATE UNIQUE INDEX AweSitModDbsI1 ON AweSitModDbs (IdeSit,IdeMod,IdeDbs);

--------------------------------------------------------
--  DDL for Table AweModOpe
--  Operator modules table: Relationship between modules and users
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweModOpe (
IdeModOpe int NOT NULL PRIMARY KEY, --- Relationship key
IdeMod int NOT NULL, --- Module key
IdeOpe int NOT NULL, --- Operator key
IdeThm int NULL, --- Theme key (not used)
Ord int NULL --- Relationship order
);
CREATE UNIQUE INDEX AweModopeI1 ON AweModOpe (IdeMod, IdeOpe);

--------------------------------------------------------
--  DDL for Table AweModPro
--  Profile modules table: Relationship between modules and profiles
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweModPro (
IdeModPro int NOT NULL PRIMARY KEY, --- Relationship key
IdeMod int NOT NULL, --- Module key
IdePro int NOT NULL, --- Profile key
Ord int NULL --- Relationship order
);
CREATE UNIQUE INDEX AweModProI1 ON AweModPro (IdeMod,IdePro);

--------------------------------------------------------
--  DDL for Table AweEmlSrv
--  Email servers table: List of available email servers on application
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweEmlSrv (
IdeAweEmlSrv int NOT NULL PRIMARY KEY, --- Email server key
SrvNam varchar(40) NOT NULL, --- Server name
Hst varchar(60) NOT NULL, --- Server host
Prt int NULL, --- Server port
Ath int DEFAULT 0 NOT NULL, --- Needs authentication (1) or not (0)
EmlUsr varchar(40) NULL, --- Server username
EmlPwd varchar(40) NULL, --- Server password (encrypted)
Act int DEFAULT 1 NOT NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweEmlSrvI1 ON AweEmlSrv (SrvNam);

--------------------------------------------------------
--  DDL for Table AweScrCnf
--  Screen configuration table: Screen component overload
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweScrCnf (
IdeAweScrCnf int NOT NULL PRIMARY KEY, --- Screen configuration key
IdeOpe int NULL, --- Operator key
IdePro int NULL, --- Profile key
Scr varchar(40) NOT NULL, --- Option name
Nam varchar(40) NOT NULL, --- Component identifier
Atr varchar(40) NOT NULL, --- Attribute to overload
Val varchar(60) NULL, --- Attribute value
Act int DEFAULT 1 NOT NULL --- Active (1) or not (0)
);
CREATE INDEX AweScrCnfI1 ON AweScrCnf (Nam, Atr, Val);

--------------------------------------------------------
--  DDL for Table AweScrRes
--  Screen restriction table: Restricts the access to screens to users or profiles
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweScrRes (
IdeAweScrRes int NOT NULL PRIMARY KEY, --- Screen restriction key
IdeOpe int NULL, --- Operator key
IdePro int NULL, --- Profile key
IdeMod int NULL, --- Module key (deprecated)
Opt varchar(40) NOT NULL, --- Option name
AccMod varchar(1) NOT NULL, --- Access type: (R) Restricted (A) Allowed
Act int DEFAULT 1 NOT NULL --- Active (1) or not (0)
);

--------------------------------------------------------
--  DDL for Table AweQue
--  Queue definition table: List of available JMS queues on application
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweQue (
IdeAweQue int NOT NULL PRIMARY KEY, --- Queue key
Als varchar(40) NOT NULL, --- Queue alias
Des varchar(60), --- Queue description
QueTyp varchar(5) NOT NULL, --- Queue type
ConTyp varchar(1) NOT NULL, --- Connection type
JmsBrk varchar(60), --- JMS Broker
JmsUsr varchar(40), --- JMS Username
JmsPwd varchar(60), --- JMS Password (encrypted)
DstNam varchar(40) NOT NULL, --- Destination name
Act int DEFAULT 1 --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweQueI1 ON AweQue (Als);

--------------------------------------------------------
--  DDL for Table AweKey
--  Awe Sequences table: List of available sequences
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweKey (
KeyNam varchar(20) NOT NULL PRIMARY KEY, --- Sequence key
KeyVal int DEFAULT 0 NOT NULL, --- Sequence value
Act int default 1 not NULL --- Active (1) or not (0)
);

--------------------------------------------------------
--  DDL for Historic Tables
--  Same fields as plain tables but with 3 key audit fields:
--  - HISope Username who has made the change
--  - HISdat Date of audit
--  - HISact Action made: (I) Insert, (U) Update, (D) Delete
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS HISAweAppPar (HISope varchar(20) not NULL, HISdat datetime not NULL, HISact varchar(1) not NULL, IdeAweAppPar int NULL, ParNam varchar(40) NULL, ParVal varchar(60) NULL, Cat int NULL, Des varchar(250) NULL, Act int DEFAULT 1 NULL);
CREATE INDEX HISAweAppParI1 ON HISAweAppPar (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweThm (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeThm int NULL, Nam varchar(100) NULL, Act int NULL);
CREATE INDEX HISAweThmI1 ON HISAweThm (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAwePro (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdePro int NULL, Acr varchar(3) NULL, Nam varchar(120) NULL, IdeThm int NULL, ScrIni varchar(40) NULL, Res varchar(40) NULL, Act int NULL);
CREATE INDEX HISAweProI1 ON HISAwePro (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISope (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeOpe int NULL, l1_nom char(20) NULL, l1_pas char(40) NULL, OpePas char(200) NULL, l1_con int NULL, l1_dev char(3) NULL, l1_act int NULL, l1_trt char(1) NULL, l1_uti int NULL, l1_opr char(6) NULL, l1_dat date NULL, imp_nom char(32) NULL, dat_mod date NULL, l1_psd date NULL, l1_lan char(3) NULL, l1_sgn int NULL, PcPrn varchar(255) NULL, EmlSrv varchar(10) NULL, EmlAdr varchar(50) NULL, OpeNam varchar(50) NULL, IdePro int not NULL, IdeThm int NULL, ScrIni varchar(40) NULL, Res varchar(40) NULL, ShwPrn int NULL, PwdLck int NULL, NumLog int NULL);
CREATE INDEX HISopeI1 ON HISope (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweDbs (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeDbs int NULL, Als char(16) NULL, Des char(40) NULL, Dct varchar (1) NULL, Dbt varchar (10) NULL, Drv varchar (256), DbsUsr varchar(50), DbsPwd varchar(50), Typ char(3) NULL, Dbc varchar(256) NULL, Act int NULL);
CREATE INDEX HISAweDbsI1 ON HISAweDbs (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweSit (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeSit int NULL, Nam varchar(100) NULL, Ord int NULL, Act int NULL );
CREATE INDEX HISAweSitI1 ON HISAweSit (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweMod (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeMod int NULL, Nam varchar(100) NULL, ScrIni varchar(40) NULL, IdeThm int NULL, Act int NULL, Ord int NULL);
CREATE INDEX HISAweModI1 ON HISAweMod (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweSitModDbs(HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeSitModDbs int NULL, IdeSit int NULL, IdeMod int NULL, IdeDbs int NULL, Ord int NULL);
CREATE INDEX HISAweSitModDbsI1 ON HISAweSitModDbs (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweModOpe (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeModOpe int NULL, IdeMod int NULL, IdeOpe int NULL, IdeThm int NULL);
CREATE INDEX HISAweModOpeI1 ON HISAweModOpe (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweModPro (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeModPro int NULL, IdeMod int NULL, IdePro int NULL);
CREATE INDEX HISAweModProI1 ON HISAweModPro (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweEmlSrv (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeAweEmlSrv int NULL, SrvNam varchar(40) NULL, Hst varchar(60) NULL, Prt int NULL, Ath int DEFAULT 0 NULL, EmlUsr varchar(40) NULL, EmlPwd varchar(240) NULL, Act int NULL);
CREATE INDEX HISAweEmlSrvI1 ON HISAweEmlSrv (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweScrCnf (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeAweScrCnf int NULL, IdeOpe int NULL, IdePro int NULL, Scr varchar(40) NULL, Nam varchar(40) NULL, Atr varchar(40) NULL, Val varchar(60) NULL, Act int NULL);
CREATE INDEX HISAweScrCnfI1 ON HISAweScrCnf (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweScrRes (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeAweScrRes int NULL, IdeOpe int NULL, IdePro int NULL, IdeMod int NULL, Opt varchar(40) NULL, AccMod varchar(1) NULL, Act int NULL);
CREATE INDEX HISAweScrResI1 ON HISAweScrRes (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweQue (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeAweQue int NULL, Als varchar(40) NULL, Des varchar(60), QueTyp varchar(5) NULL, ConTyp varchar(1) NULL, JmsBrk varchar(60), JmsUsr varchar(40), JmsPwd varchar(60), DstNam varchar(40) NULL, Act int DEFAULT 1);
CREATE INDEX HISAweQueI1 ON HISAweQue (HISope, HISdat, HISact);

--------------------------------------------------------
--  DDL for CONSTRAINTS
--------------------------------------------------------
ALTER TABLE AwePro ADD CONSTRAINT fk_AwePro1 FOREIGN KEY (IdeThm) REFERENCES AweThm (IdeThm);
ALTER TABLE AwePro ADD CONSTRAINT uq_AwePro UNIQUE (Acr);
ALTER TABLE ope ADD CONSTRAINT fk_ope1 FOREIGN KEY (IdePro) REFERENCES AwePro (IdePro);
ALTER TABLE ope ADD CONSTRAINT fk_ope2 FOREIGN KEY (IdeThm) REFERENCES AweThm (IdeThm);
ALTER TABLE AweMod ADD CONSTRAINT fk_AweMod1 FOREIGN KEY (IdeThm) REFERENCES AweThm (IdeThm);
ALTER TABLE AweSitModDbs ADD CONSTRAINT fk_AweSitModDbs1 FOREIGN KEY (IdeSit) REFERENCES AweSit (IdeSit);
ALTER TABLE AweSitModDbs ADD CONSTRAINT fk_AweSitModDbs2 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweSitModDbs ADD CONSTRAINT fk_AweSitModDbs3 FOREIGN KEY (IdeDbs) REFERENCES AweDbs (IdeDbs);
ALTER TABLE AweModOpe ADD CONSTRAINT fk_AweModOpe1 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweModOpe ADD CONSTRAINT fk_AweModOpe2 FOREIGN KEY (IdeOpe) REFERENCES ope (IdeOpe);
ALTER TABLE AweModOpe ADD CONSTRAINT fk_AweModOpe3 FOREIGN KEY (IdeThm) REFERENCES AweThm (IdeThm);
ALTER TABLE AweModPro ADD CONSTRAINT fk_AweModPro1 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweModPro ADD CONSTRAINT fk_AweModPro2 FOREIGN KEY (IdePro) REFERENCES AwePro (IdePro);
ALTER TABLE AweScrCnf ADD CONSTRAINT fk_AweScrCnf1 FOREIGN KEY (IdeOpe) REFERENCES ope (IdeOpe);
ALTER TABLE AweScrCnf ADD CONSTRAINT fk_AweScrCnf2 FOREIGN KEY (IdePro) REFERENCES AwePro (IdePro);

--------------------------------------------------------
--  DDL for TEST AUTO INCREMENT TABLE
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS TestAutoIncrement (
id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
name varchar(30),
email varchar(50)
);
ALTER TABLE TestAutoIncrement AUTO_INCREMENT = 100;


--------------------------------------------------------
--  SCHEDULER DDL
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Table AweSchCal
--  Calendar list
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchCal
(
    Ide INTEGER not NULL,
    Des VARCHAR(250) not NULL,
    Act INTEGER DEFAULT 1 not NULL,
    Nom VARCHAR(100) not NULL
);

--------------------------------------------------------
--  DDL for Table AweSchTskFilMod
--  Calendar dates
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchTskFilMod
(
    IdeTsk INTEGER not NULL,
    FilPth VARCHAR(256) not NULL,
    ModDat DATE
);

--------------------------------------------------------
--  DDL for Table AweSchCalDat
--  Task file
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchCalDat
(
    Ide    INTEGER not NULL,
    IdeCal INTEGER not NULL,
    Nom    VARCHAR(40) not NULL,
    Dat    DATE not NULL
);

--------------------------------------------------------
--  DDL for Table AweSchExe
--  Task executions
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchExe
(
    IdeTsk INTEGER not NULL,
    GrpTsk VARCHAR(40) not NULL,
    ExeTsk INTEGER not NULL,
    IniDat DATETIME not NULL,
    EndDat DATETIME,
    ExeTim INTEGER,
    Sta    INTEGER not NULL,
    LchBy  VARCHAR(200),
    Des    VARCHAR(2000)
);

--------------------------------------------------------
--  DDL for Table AweSchSrv
--  Scheduler servers
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchSrv
(
    Ide INTEGER not NULL,
    Nom VARCHAR(40) not NULL,
    Pro VARCHAR(10) not NULL,
    Hst VARCHAR(40) not NULL,
    Prt VARCHAR(10) not NULL,
    Act INTEGER DEFAULT 1 not NULL
);

--------------------------------------------------------
--  DDL for Table AweSchTsk
--  Scheduler tasks
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchTsk
(
    Ide       INTEGER not NULL,
    Nam       VARCHAR(40) not NULL,
    Des       VARCHAR(250),
    NumStoExe INTEGER,
    TimOutExe INTEGER,
    TypExe    INTEGER not NULL,
    IdeSrvExe INTEGER,
    CmdExe    VARCHAR(250) not NULL,
    TypLch    INTEGER not NULL,
    LchDepErr INTEGER DEFAULT 0 not NULL,
    LchDepWrn INTEGER DEFAULT 0 not NULL,
    LchSetWrn INTEGER DEFAULT 0 not NULL,
    RepTyp    INTEGER DEFAULT 0 not NULL,
    RepEmaSrv INTEGER,
    RepSndSta VARCHAR(20),
    RepEmaDst VARCHAR(250),
    RepTit    VARCHAR(100),
    RepMsg    VARCHAR(250),
    Act       INTEGER DEFAULT 1 not NULL,
    RepUsrDst VARCHAR(250),
    RepMntId  VARCHAR(200),
    CmdExePth VARCHAR(200),
    db        VARCHAR(200),
    site      VARCHAR(200)
);

--------------------------------------------------------
--  DDL for Table AweSchTskDpn
--  Task dependencies
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchTskDpn
(
    IdeTsk INTEGER NOT NULL,
    IdePrn INTEGER NOT NULL,
    IsBlk  INTEGER,
    DpnOrd INTEGER
);

--------------------------------------------------------
--  DDL for Table AweSchTskLch
--  Task launchers
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchTskLch
(
    Ide      INTEGER NOT NULL,
    IdeTsk   INTEGER,
    RptNum   INTEGER,
    RptTyp   INTEGER,
    IniDat   DATE,
    EndDat   DATE,
    IniTim   VARCHAR(8),
    EndTim   VARCHAR(8),
    IdeCal   INTEGER,
    IdSrv    INTEGER,
    Pth      VARCHAR(250),
    Pat      VARCHAR(250),
    ExeHrs   VARCHAR(200),
    ExeMth   VARCHAR(200),
    ExeWek   VARCHAR(200),
    ExeDay   VARCHAR(200),
    ExeDte   DATE,
    ExeTim   VARCHAR(8),
    WeekDays VARCHAR(200),
    ExeYrs   VARCHAR(200),
    ExeMin   VARCHAR(200),
    ExeSec   VARCHAR(200),
    SrvUsr   VARCHAR(200),
    SrvPwd   VARCHAR(200)
);

--------------------------------------------------------
--  DDL for Table AweSchTskPar
--  Task parameters
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSchTskPar
(
    Ide    INTEGER NOT NULL,
    IdeTsk INTEGER,
    Nam    VARCHAR(40) NOT NULL,
    Val    VARCHAR(400),
    Src    INTEGER NOT NULL,
    Typ    VARCHAR(100) NOT NULL
);

--------------------------------------------------------
--  DDL for HISTORIC TABLES
--------------------------------------------------------

CREATE TABLE IF NOT EXISTS HisAweSchCal
(
    HISope VARCHAR(20),
    HISdat DATE,
    HISact VARCHAR(1),
    Ide    INTEGER,
    Nom    VARCHAR(40),
    Des    VARCHAR(250),
    Act    INTEGER
);

CREATE TABLE IF NOT EXISTS HisAweSchCalDat
(
    HISope VARCHAR(20),
    HISdat DATE,
    HISact VARCHAR(1),
    Ide    INTEGER,
    IdeCal INTEGER,
    Nom    VARCHAR(40),
    Dat    DATE
);

CREATE TABLE IF NOT EXISTS HisAweSchSrv
(
    HISope VARCHAR(20) not NULL,
    HISdat DATE not NULL,
    HISact VARCHAR(1) not NULL,
    Ide    INTEGER,
    Nom    VARCHAR(40),
    Pro    VARCHAR(10),
    Hst    VARCHAR(40),
    Prt    VARCHAR(10),
    Act    INTEGER
);

CREATE TABLE IF NOT EXISTS HisAweSchTsk
(
    HISope    VARCHAR(20) not NULL,
    HISdat    DATE not NULL,
    HISact    VARCHAR(1) not NULL,
    Ide       INTEGER,
    IdePar    INTEGER,
    Nam       VARCHAR(40),
    Des       VARCHAR(250),
    NumStoExe INTEGER,
    TimOutExe INTEGER,
    TypExe    INTEGER,
    IdeSrvExe INTEGER,
    CmdExe    VARCHAR(250),
    TypLch    INTEGER,
    LchDepErr INTEGER,
    LchDepWrn INTEGER,
    LchSetWrn INTEGER,
    BlkPar    INTEGER,
    RepTyp    INTEGER,
    RepEmaSrv INTEGER,
    RepSndSta VARCHAR(20),
    RepEmaDst VARCHAR(250),
    RepTit    VARCHAR(100),
    RepMsg    VARCHAR(250),
    Act       INTEGER,
    RepUsrDst VARCHAR(250),
    RepMntId  VARCHAR(200),
    CmdExePth VARCHAR(200),
    db        VARCHAR(200),
    site      VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS HisAweSchTskLch
(
    HISope   VARCHAR(20) not NULL,
    HISdat   DATE not NULL,
    HISact   VARCHAR(1) not NULL,
    Ide      INTEGER,
    IdeTsk   INTEGER,
    RptNum   INTEGER,
    RptTyp   INTEGER,
    IniDat   DATE,
    EndDat   DATE,
    IniTim   VARCHAR(8),
    EndTim   VARCHAR(8),
    IdeCal   INTEGER,
    IdSrv    INTEGER,
    Pth      VARCHAR(250),
    Pat      VARCHAR(250),
    ExeMth   VARCHAR(200),
    ExeWek   VARCHAR(200),
    ExeDay   VARCHAR(200),
    ExeHrs   VARCHAR(200),
    ExeDte   DATE,
    ExeTim   VARCHAR(8),
    WeekDays VARCHAR(200),
    ExeYrs   VARCHAR(200),
    ExeMin   VARCHAR(200),
    ExeSec   VARCHAR(200),
    SrvUsr   VARCHAR(200),
    SrvPwd   VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS HisAweSchTskPar
(
    HISope VARCHAR(20) not NULL,
    HISdat DATE not NULL,
    HISact VARCHAR(1) not NULL,
    Ide    INTEGER,
    IdeTsk INTEGER,
    Nam    VARCHAR(40),
    Val    VARCHAR(400),
    Src    INTEGER,
    Typ    VARCHAR(100)
);

--------------------------------------------------------
--  TESTING TABLES
--------------------------------------------------------

CREATE TABLE IF NOT EXISTS DummyClobTestTable
(
    textFile TEXT
);

--------------------------------------------------------
--  DDL for CONSTRAINTS
--------------------------------------------------------

CREATE UNIQUE INDEX NOM_UQ ON AWESCHCAL (Nom);
CREATE UNIQUE INDEX PK_AWESCHCAL ON AWESCHCAL (Ide);
CREATE UNIQUE INDEX PK_AWESCHCALDAT ON AWESCHCALDAT (Ide);
CREATE INDEX AWESCHEXEI1 ON AWESCHEXE (IdeTsk, GrpTsk, ExeTsk, IniDat);
CREATE UNIQUE INDEX PK_AWESCHSRV ON AWESCHSRV (Ide);
CREATE UNIQUE INDEX PK_AWESCHTSK ON AWESCHTSK (Ide);
CREATE UNIQUE INDEX SYS_C00164575 ON AWESCHTSKDPN (IdeTsk, IDEPRN);
CREATE UNIQUE INDEX PK_AWESCHTSKLCH ON AWESCHTSKLCH (Ide);
CREATE UNIQUE INDEX PK_AWESCHTSKPAR ON AWESCHTSKPAR (Ide);
CREATE INDEX HisAweSchCalI1 ON HisAweSchCal (HISope, HISdat, HISact);
CREATE INDEX HisAweSchCalDatI1 ON HisAweSchCalDat (HISope, HISdat, HISact);
CREATE INDEX HisAweSchSrvI1 ON HisAweSchSrv (HISope, HISdat, HISact);
CREATE INDEX HisAweSchTskI1 ON HisAweSchTsk (HISope, HISdat, HISact);
CREATE INDEX HisAweSchTskLchI1 ON HisAweSchTskLch (HISope, HISdat, HISact);
CREATE INDEX HisAweSchTskParI1 ON HisAweSchTskPar (HISope, HISdat, HISact);
ALTER TABLE AWESCHCAL ADD CONSTRAINT NOM_UQ UNIQUE (Nom);
ALTER TABLE AWESCHCAL ADD CONSTRAINT PK_AWESCHCAL PRIMARY KEY (Ide);
ALTER TABLE AWESCHCALDAT ADD CONSTRAINT PK_AWESCHCALDAT PRIMARY KEY (Ide);
ALTER TABLE AWESCHTSKFILMOD ADD PRIMARY KEY (IdeTsk, FILPTH);
ALTER TABLE AWESCHSRV ADD CONSTRAINT PK_AWESCHSRV PRIMARY KEY (Ide);
ALTER TABLE AWESCHTSK ADD CONSTRAINT PK_AWESCHTSK PRIMARY KEY (Ide);
ALTER TABLE AWESCHTSKDPN ADD PRIMARY KEY (IdeTsk, IDEPRN);
ALTER TABLE AWESCHTSKLCH ADD CONSTRAINT PK_AWESCHTSKLCH PRIMARY KEY (Ide);
ALTER TABLE AWESCHTSKPAR ADD CONSTRAINT PK_AWESCHTSKPAR PRIMARY KEY (Ide);