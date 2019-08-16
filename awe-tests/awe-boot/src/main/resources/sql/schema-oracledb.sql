--------------------------------------------------------
--  DDL for Table AweAppPar
--  Application parameters table: Allows to configure specific parameters in the application
--------------------------------------------------------
CREATE TABLE AweAppPar (
IdeAweAppPar number(5) CONSTRAINT pk_AweAppPar PRIMARY KEY NOT NULL, --- Table identifier
ParNam varchar2(40) NOT NULL, --- Parameter name
ParVal varchar2(60) NULL, ---  Parameter value
Cat number(5) NOT NULL, ---  Parameter category: General (1), Reporting (2), Security (3)
Des varchar2(250) NULL, --- Parameter description
Act number(5) DEFAULT 1 NOT NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweAppParI1 ON AweAppPar (ParNam);

--------------------------------------------------------
--  DDL for Table AweThm
--  Themes table: List of available themes
--------------------------------------------------------
CREATE TABLE AweThm (
IdeThm number(5) CONSTRAINT pk_AweThm PRIMARY KEY NOT NULL, --- Theme key
Nam varchar2(100) not NULL, --- Theme name
Act number(5) default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweThmI1 ON AweThm (Nam);

--------------------------------------------------------
--  DDL for Table AwePro
--  Profiles table: List of application profiles
--------------------------------------------------------
CREATE TABLE AwePro (
IdePro number(5) CONSTRAINT pk_AwePro PRIMARY KEY NOT NULL, --- Profile key
Acr varchar2(3) not NULL, --- Profile acronym (3 chars)
Nam varchar2(120) not NULL, --- Profile name
IdeThm number(5) NULL, --- Default theme identifier for profile
ScrIni varchar2(40) NULL, --- Initial screen for profile
Res varchar2(40) NULL, --- Profile restriction file (listed on profile folder)
Act number(5) default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweProI1 ON AwePro (Nam);

--------------------------------------------------------
--  DDL for Table ope
--  Operators table: List of application users
--------------------------------------------------------
CREATE TABLE ope (
IdeOpe number(5) CONSTRAINT pk_ope PRIMARY KEY NOT NULL, --- Operator key
l1_nom char(20), --- User name
l1_pas char(40), --- User password hash
OpePas char(200), --- User password hash (old)
l1_con number(5) DEFAULT 0, --- Connected (1) or not (0)
l1_dev char(3), --- unused
l1_act number(5) DEFAULT 1, --- Active (1) or not (0)
l1_trt char(1), --- unused
l1_uti number(5), --- unused
l1_opr char(6), --- unused
l1_dat DATE, --- Last connection date
imp_nom char(32) DEFAULT 'none',
dat_mod timestamp, --- User update date
l1_psd date, --- Date of password expiration
l1_lan char(3), --- User language
l1_sgn number(5), --- User signed
PcPrn varchar2(255), --- User printer
EmlSrv varchar2(10), --- Email server
EmlAdr varchar2(50), --- Email address
OpeNam varchar2(50), --- User full name
IdePro number(5), --- User profile
IdeThm number(5), --- User theme
ScrIni varchar2(40), --- User initial screen
Res varchar2(40), --- User specific restriction profile
ShwPrn number(5), --- Allow user to print (1) or not (0)
WebPrn varchar2(255), --- User web printer
PwdLck number(5) DEFAULT 0, --- Password locked (1) or not (0)
NumLog number(5) DEFAULT 0 --- Number of times logged in concurrently
);
CREATE UNIQUE INDEX opeI1 ON ope (l1_nom);

--------------------------------------------------------
--  DDL for Table AweDbs
--  Database table: List of application database connections
--------------------------------------------------------
CREATE TABLE AweDbs (
IdeDbs number(5) CONSTRAINT pk_AweDbs PRIMARY KEY NOT NULL, --- Database key
Als varchar2(16) not NULL, --- Database alias
Des varchar2(40) NULL, --- Database description
Dct varchar2(1) not NULL, --- Database connection type: (J) JDBC, (D) Datasource
Dbt varchar2(10) not NULL, --- Database type (ora) Oracle, (sqs) SQL Server, (hsql) HSQLDB, (h2) H2 Database, (mysql) MySQL/MariaDB
Drv varchar2(256), --- Database driver
DbsUsr varchar2(50), --- Database username
DbsPwd varchar2(50), --- Database password (encrypted)
Typ varchar2(3) not NULL, --- Database environment: (Des) Development, (Pre) Pre-production, (Pro) Production
Dbc varchar2(256) not NULL, --- Database connection: JDBC database connection URL
Act number(5) default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweDbsI1 ON AweDbs (Als);

--------------------------------------------------------
--  DDL for Table AweSit
--  Sites table: List of available application sites
--------------------------------------------------------
CREATE TABLE AweSit (
IdeSit number(5) CONSTRAINT pk_AweSit PRIMARY KEY NOT NULL, --- Site key
Nam varchar2(100) NOT NULL, --- Site name
Ord number(5) NULL, --- Site order (in selector)
Act number(5) default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweSitI1 ON AweSit (Nam);

--------------------------------------------------------
--  DDL for Table AweMod
--  Module table: List of awe modules
--------------------------------------------------------
CREATE TABLE AweMod (
IdeMod number(5) CONSTRAINT pk_AweMod PRIMARY KEY NOT NULL, --- Module key
Nam varchar2(100) not NULL, --- Module name
ScrIni varchar2(40) NULL, --- Module initial screen (deprecated)
IdeThm number(5) NULL, --- Module theme (deprecated)
Act number(5) default 1 not NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweModI1 ON AweMod (Nam);

--------------------------------------------------------
--  DDL for Table AweSitModDbs
--  Sites-Modules-Databases relationship table
--------------------------------------------------------
CREATE TABLE AweSitModDbs (
IdeSitModDbs number(5) CONSTRAINT pk_AweSitModDbs PRIMARY KEY NOT NULL, --- Relationship key
IdeSit number(5) NOT NULL, --- Site key
IdeMod number(5) NOT NULL, --- Module key
IdeDbs number(5) NOT NULL, --- Database key
Ord number(5) NULL --- Relationship order
);
CREATE UNIQUE INDEX AweSitModDbsI1 ON AweSitModDbs (IdeSit,IdeMod,IdeDbs);

--------------------------------------------------------
--  DDL for Table AweModOpe
--  Operator modules table: Relationship between modules and users
--------------------------------------------------------
CREATE TABLE AweModOpe (
IdeModOpe number(5) CONSTRAINT pk_AweModOpe PRIMARY KEY NOT NULL, --- Relationship key
IdeMod number(5) NOT NULL, --- Module key
IdeOpe number(5) NOT NULL, --- Operator key
IdeThm number(5) NULL, --- Theme key (not used)
Ord number(5) NULL --- Relationship order
);
CREATE UNIQUE INDEX AweModopeI1 ON AweModOpe (IdeMod, IdeOpe);

--------------------------------------------------------
--  DDL for Table AweModPro
--  Profile modules table: Relationship between modules and profiles
--------------------------------------------------------
CREATE TABLE AweModPro (
IdeModPro number(5) CONSTRAINT pk_AweMdlPrf PRIMARY KEY NOT NULL, --- Relationship key
IdeMod number(5) NOT NULL, --- Module key
IdePro number(5) NOT NULL, --- Profile key
Ord number(5) NULL --- Relationship order
);
CREATE UNIQUE INDEX AweModProI1 ON AweModPro (IdeMod,IdePro);

--------------------------------------------------------
--  DDL for Table AweEmlSrv
--  Email servers table: List of available email servers on application
--------------------------------------------------------
CREATE TABLE AweEmlSrv (
IdeAweEmlSrv number(5) CONSTRAINT pk_AweEmlSrv PRIMARY KEY NOT NULL, --- Email server key
SrvNam varchar2(40) NOT NULL, --- Server name
Hst varchar2(60) NOT NULL, --- Server host
Prt number(5) NULL, --- Server port
Ath number(5) DEFAULT 0 NOT NULL, --- Needs authentication (1) or not (0)
EmlUsr varchar2(40) NULL, --- Server username
EmlPwd varchar2(40) NULL, --- Server password (encrypted)
Act number(5) DEFAULT 1 NOT NULL --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweEmlSrvI1 ON AweEmlSrv (SrvNam);

--------------------------------------------------------
--  DDL for Table AweScrCnf
--  Screen configuration table: Screen component overload
--------------------------------------------------------
CREATE TABLE AweScrCnf (
IdeAweScrCnf number(5) CONSTRAINT pk_AweScrCnf PRIMARY KEY NOT NULL, --- Screen configuration key
IdeOpe number(5) NULL, --- Operator key
IdePro number(5) NULL, --- Profile key
Scr varchar2(40) NOT NULL, --- Option name
Nam varchar2(40) NOT NULL, --- Component identifier
Atr varchar2(40) NOT NULL, --- Attribute to overload
Val varchar2(60) NULL, --- Attribute value
Act number(5) DEFAULT 1 NOT NULL --- Active (1) or not (0)
);
CREATE INDEX AweScrCnfI1 ON AweScrCnf (Nam, Atr, Val);

--------------------------------------------------------
--  DDL for Table AweScrRes
--  Screen restriction table: Restricts the access to screens to users or profiles
--------------------------------------------------------
CREATE TABLE AweScrRes (
IdeAweScrRes number(5) CONSTRAINT pk_AweScrRes PRIMARY KEY NOT NULL, --- Screen restriction key
IdeOpe number(5) NULL, --- Operator key
IdePro number(5) NULL, --- Profile key
IdeMod number(5) NULL, --- Module key (deprecated)
Opt varchar2(40) NOT NULL, --- Option name
AccMod varchar2(1) NOT NULL, --- Access type: (R) Restricted (A) Allowed
Act number(5) DEFAULT 1 NOT NULL --- Active (1) or not (0)
);

--------------------------------------------------------
--  DDL for Table AweQue
--  Queue definition table: List of available JMS queues on application
--------------------------------------------------------
CREATE TABLE AweQue (
IdeAweQue number(5) CONSTRAINT pk_AweQue PRIMARY KEY NOT NULL, --- Queue key
Als varchar2(40) NOT NULL, --- Queue alias
Des varchar2(60), --- Queue description
QueTyp varchar2(5) NOT NULL, --- Queue type
ConTyp varchar2(1) NOT NULL, --- Connection type
JmsBrk varchar2(60), --- JMS Broker
JmsUsr varchar2(40), --- JMS Username
JmsPwd varchar2(60), --- JMS Password (encrypted)
DstNam varchar2(40) NOT NULL, --- Destination name
Act number(5) DEFAULT 1 --- Active (1) or not (0)
);
CREATE UNIQUE INDEX AweQueI1 ON AweQue (Als);

--------------------------------------------------------
--  DDL for Table AweKey
--  Awe Sequences table: List of available sequences
--------------------------------------------------------
CREATE TABLE AweKey (
KeyNam varchar2(20) CONSTRAINT pk_AweKey PRIMARY KEY NOT NULL, --- Sequence key
KeyVal number(5) DEFAULT 0 NOT NULL, --- Sequence value
Act number(5) default 1 not NULL --- Active (1) or not (0)
);

--------------------------------------------------------
--  DDL for Historic Tables
--  Same fields as plain tables but with 3 key audit fields:
--  - HISope Username who has made the change
--  - HISdat Date of audit
--  - HISact Action made: (I) Insert, (U) Update, (D) Delete
--------------------------------------------------------
CREATE TABLE HISAweAppPar (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeAweAppPar number(5) NULL, ParNam varchar2(40) NULL, ParVal varchar2(60) NULL, Cat number(5) NULL, Des varchar2(250) NULL, Act number(5) DEFAULT 1 NULL);
CREATE INDEX HISAweAppParI1 ON HISAweAppPar (HISope, HISdat, HISact);
CREATE TABLE HISAweThm (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeThm number(5) NULL, Nam varchar2(100) NULL, Act number(5) NULL);
CREATE INDEX HISAweThmI1 ON HISAweThm (HISope, HISdat, HISact);
CREATE TABLE HISAwePro (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdePro number(5) NULL, Acr varchar2(3) NULL, Nam varchar2(120) NULL, IdeThm number(5) NULL, ScrIni varchar2(40) NULL, Res varchar2(40) NULL, Act number(5) NULL);
CREATE INDEX HISAweProI1 ON HISAwePro (HISope, HISdat, HISact);
CREATE TABLE HISope (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeOpe number(5) NULL, l1_nom char(20) NULL, l1_pas char(40) NULL, OpePas char(200) NULL, l1_con number(5) NULL, l1_dev char(3) NULL, l1_act number(5) NULL, l1_trt char(1) NULL, l1_uti number(5) NULL, l1_opr char(6) NULL, l1_dat date NULL, imp_nom char(32) NULL, dat_mod date NULL, l1_psd date NULL, l1_lan char(3) NULL, l1_sgn number(5) NULL, PcPrn varchar2(255) NULL, EmlSrv varchar2(10) NULL, EmlAdr varchar2(50) NULL, OpeNam varchar2(50) NULL, IdePro number(5) not NULL, IdeThm number(5) NULL, ScrIni varchar2(40) NULL, Res varchar2(40) NULL, ShwPrn number(5) NULL, PwdLck number(5) NULL, NumLog number(5) NULL);
CREATE INDEX HISopeI1 ON HISope (HISope, HISdat, HISact);
CREATE TABLE HISAweDbs (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeDbs number(5) NULL, Als char(16) NULL, Des char(40) NULL, Dct varchar (1) NULL, Dbt varchar (10) NULL, Drv varchar (256), DbsUsr varchar2(50), DbsPwd varchar2(50), Typ char(3) NULL, Dbc varchar2(256) NULL, Act number(5) NULL);
CREATE INDEX HISAweDbsI1 ON HISAweDbs (HISope, HISdat, HISact);
CREATE TABLE HISAweSit (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeSit number(5) NULL, Nam varchar2(100) NULL, Ord number(5) NULL, Act number(5) NULL );
CREATE INDEX HISAweSitI1 ON HISAweSit (HISope, HISdat, HISact);
CREATE TABLE HISAweMod (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeMod number(5) NULL, Nam varchar2(100) NULL, ScrIni varchar2(40) NULL, IdeThm number(5) NULL, Act number(5) NULL);
CREATE INDEX HISAweModI1 ON HISAweMod (HISope, HISdat, HISact);
CREATE TABLE HISAweSitModDbs(HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeSitModDbs number(5) NULL, IdeSit number(5) NULL, IdeMod number(5) NULL, IdeDbs number(5) NULL, Ord number(5) NULL);
CREATE INDEX HISAweSitModDbsI1 ON HISAweSitModDbs (HISope, HISdat, HISact);
CREATE TABLE HISAweModOpe (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeModOpe number(5) NULL, IdeMod number(5) NULL, IdeOpe number(5) NULL, IdeThm number(5) NULL);
CREATE INDEX HISAweModOpeI1 ON HISAweModOpe (HISope, HISdat, HISact);
CREATE TABLE HISAweModPro (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeModPro number(5) NULL, IdeMod number(5) NULL, IdePro number(5) NULL);
CREATE INDEX HISAweModProI1 ON HISAweModPro (HISope, HISdat, HISact);
CREATE TABLE HISAweEmlSrv (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeAweEmlSrv number(5) NULL, SrvNam varchar2(40) NULL, Hst varchar2(60) NULL, Prt number(5) NULL, Ath number(5) DEFAULT 0 NULL, EmlUsr varchar2(40) NULL, EmlPwd varchar2(240) NULL, Act number(5) NULL);
CREATE INDEX HISAweEmlSrvI1 ON HISAweEmlSrv (HISope, HISdat, HISact);
CREATE TABLE HISAweScrCnf (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeAweScrCnf number(5) NULL, IdeOpe number(5) NULL, IdePro number(5) NULL, Scr varchar2(40) NULL, Nam varchar2(40) NULL, Atr varchar2(40) NULL, Val varchar2(60) NULL, Act number(5) NULL);
CREATE INDEX HISAweScrCnfI1 ON HISAweScrCnf (HISope, HISdat, HISact);
CREATE TABLE HISAweScrRes (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeAweScrRes number(5) NULL, IdeOpe number(5) NULL, IdePro number(5) NULL, IdeMod number(5) NULL, Opt varchar2(40) NULL, AccMod varchar2(1) NULL, Act number(5) NULL);
CREATE INDEX HISAweScrResI1 ON HISAweScrRes (HISope, HISdat, HISact);
CREATE TABLE HISAweQue (HISope varchar2(20) not NULL, HISdat date not NULL, HISact varchar2(1) not NULL, IdeAweQue number(5) NULL, Als varchar2(40) NULL, Des varchar2(60), QueTyp varchar2(5) NULL, ConTyp varchar2(1) NULL, JmsBrk varchar2(60), JmsUsr varchar2(40), JmsPwd varchar2(60), DstNam varchar2(40) NULL, Act number(5) DEFAULT 1);
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
