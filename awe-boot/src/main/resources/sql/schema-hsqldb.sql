--------------------------------------------------------
--  DDL for Schema AWE
--------------------------------------------------------
CREATE SCHEMA AWE;

--------------------------------------------------------
--  DDL for Table AweAppPar
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweAppPar (IdeAweAppPar int CONSTRAINT pk_AweAppPar PRIMARY KEY NOT NULL, ParNam varchar(40) NOT NULL, ParVal varchar(60) NULL, Cat int NOT NULL, Des varchar(250) NULL, Act int DEFAULT 1 NOT NULL);
CREATE UNIQUE INDEX AweAppParI1 ON AweAppPar (ParNam)

--------------------------------------------------------
--  DDL for Table AweThm
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweThm (IdeThm int CONSTRAINT pk_AweThm PRIMARY KEY NOT NULL, Nam varchar(100) not NULL, Act int default 1 not NULL);
CREATE UNIQUE INDEX AweThmI1 ON AweThm (Nam);

--------------------------------------------------------
--  DDL for Table AwePro
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AwePro (IdePro int CONSTRAINT pk_AwePro PRIMARY KEY NOT NULL, Acr varchar(3) not NULL, Nam varchar(120) not NULL, IdeThm int NULL, ScrIni varchar(40) NULL, Res varchar(40) NULL, Act int default 1 not NULL);
CREATE UNIQUE INDEX AweProI1 ON AwePro (Nam);

--------------------------------------------------------
--  DDL for Table ope
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS ope (IdeOpe int CONSTRAINT pk_ope PRIMARY KEY NOT NULL, l1_nom char(20), l1_pas char(40), OpePas char(200), l1_con int DEFAULT 0,	l1_dev char(3),	l1_act int DEFAULT 1, l1_trt char(1), l1_uti int, l1_opr char(6), l1_dat DATE, imp_nom char(32) DEFAULT 'none', dat_mod TIMESTAMP, l1_psd TIMESTAMP, l1_lan char(3), l1_sgn int, PcPrn varchar(255), EmlSrv varchar(10), EmlAdr varchar(50), OpeNam varchar(50), IdePro int, IdeThm int, ScrIni varchar(40), Res varchar(40), ShwPrn int, WebPrn varchar(255), PwdLck int DEFAULT 0, NumLog int DEFAULT 0);
CREATE UNIQUE INDEX opeI1 ON ope (l1_nom);

--------------------------------------------------------
--  DDL for Table AweDbs
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweDbs (IdeDbs int CONSTRAINT pk_AweDbs PRIMARY KEY NOT NULL, Als varchar(16) not NULL, Des varchar(40) NULL, Dct varchar(1) not NULL, Dbt varchar(10) not NULL, Drv varchar(256), DbsUsr varchar(50), DbsPwd varchar(50), Typ varchar(3) not NULL, Dbc varchar(256) not NULL, Act int default 1 not NULL);
CREATE UNIQUE INDEX AweDbsI1 ON AweDbs (Als);

--------------------------------------------------------
--  DDL for Table AweSit
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSit (IdeSit int CONSTRAINT pk_AweSit PRIMARY KEY NOT NULL, Nam varchar(100) NOT NULL, Ord int NULL, Act int default 1 not NULL);
CREATE UNIQUE INDEX AweSitI1 ON AweSit (Nam);

--------------------------------------------------------
--  DDL for Table AweMod
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweMod (IdeMod int CONSTRAINT pk_AweMod PRIMARY KEY NOT NULL, Nam varchar(100) not NULL, ScrIni varchar(40) NULL, IdeThm int NULL, Act int default 1 not NULL);
CREATE UNIQUE INDEX AweModI1 ON AweMod (Nam);

--------------------------------------------------------
--  DDL for Table AweSitModDbs
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweSitModDbs (IdeSitModDbs int CONSTRAINT pk_AweSitModDbs PRIMARY KEY NOT NULL, IdeSit int NOT NULL, IdeMod int NOT NULL, IdeDbs int NOT NULL, Ord int NULL);
CREATE UNIQUE INDEX AweSitModDbsI1 ON AweSitModDbs (IdeSit,IdeMod,IdeDbs);

--------------------------------------------------------
--  DDL for Table AweModOpe
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweModOpe (IdeModOpe int CONSTRAINT pk_AweModOpe PRIMARY KEY NOT NULL, IdeMod int NOT NULL, IdeOpe int NOT NULL, IdeThm int NULL, Ord int NULL);
CREATE UNIQUE INDEX AweModopeI1 ON AweModOpe (IdeMod, IdeOpe);

--------------------------------------------------------
--  DDL for Table AweModPro
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweModPro (IdeModPro int CONSTRAINT pk_AweMdlPrf PRIMARY KEY NOT NULL, IdeMod int NOT NULL, IdePro int NOT NULL, Ord int NULL);
CREATE UNIQUE INDEX AweModProI1 ON AweModPro (IdeMod,IdePro);

--------------------------------------------------------
--  DDL for Table AweScrPro
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweScrPro (IdeScrPro int CONSTRAINT pk_AweScrPro PRIMARY KEY NOT NULL, IdePro int NOT NULL, IdeMod int NULL, Scr varchar(40) NOT NULL, AccMod varchar(1) NOT NULL, Act int default 1 NOT NULL);
CREATE INDEX AweScrProI1 ON AweScrPro (IdePro,Scr,IdeMod);

--------------------------------------------------------
--  DDL for Table AweScrOpe
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweScrOpe (IdeScrOpe int CONSTRAINT pk_AweScrOpe PRIMARY KEY NOT NULL, IdeOpe int NOT NULL, IdeMod int NULL, Scr varchar(40) NOT NULL, AccMod varchar(1) NOT NULL, Act int DEFAULT 1 NOT NULL);
CREATE INDEX AweScrOpeI1 ON AweScrOpe (IdeOpe,Scr,IdeMod);

--------------------------------------------------------
--  DDL for Table AweScrMod
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweScrMod (IdeScrMod int CONSTRAINT pk_AweScrMod PRIMARY KEY NOT NULL, Scr varchar(20) NOT NULL, IdeMod int NULL, Act int NULL);
CREATE INDEX AweScrModI1 ON AweScrMod (Scr,IdeMod);

--------------------------------------------------------
--  DDL for Table AweEmlSrv
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweEmlSrv (IdeAweEmlSrv int CONSTRAINT pk_AweEmlSrv PRIMARY KEY NOT NULL, SrvNam varchar(40) NOT NULL, Hst varchar(60) NOT NULL, Prt int NULL, Ath int DEFAULT 0 NOT NULL, EmlUsr varchar(40) NULL, EmlPwd varchar(40) NULL, Act int DEFAULT 1 NOT NULL);
CREATE UNIQUE INDEX AweEmlSrvI1 ON AweEmlSrv (SrvNam);

--------------------------------------------------------
--  DDL for Table AweScrCnf
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweScrCnf (IdeAweScrCnf int CONSTRAINT pk_AweScrCnf PRIMARY KEY NOT NULL, IdeOpe int NULL, IdePro int NULL, Scr varchar(40) NOT NULL, Nam varchar(40) NOT NULL, Atr varchar(40) NOT NULL, Val varchar(60) NULL, Act int DEFAULT 1 NOT NULL);
CREATE INDEX AweScrCnfI1 ON AweScrCnf (Nam, Atr, Val);

--------------------------------------------------------
--  DDL for Table AweScrRes
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweScrRes (IdeAweScrRes int CONSTRAINT pk_AweScrRes PRIMARY KEY NOT NULL, IdeOpe int NULL, IdePro int NULL, IdeMod int NULL, Opt varchar(40) NOT NULL, AccMod varchar(1) NOT NULL, Act int DEFAULT 1 NOT NULL);

--------------------------------------------------------
--  DDL for Table AWEQUE
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweQue (IdeAweQue int CONSTRAINT pk_AweQue PRIMARY KEY NOT NULL, Als varchar(40) NOT NULL, Des varchar(60), QueTyp varchar(5) NOT NULL, ConTyp varchar(1) NOT NULL, JmsBrk varchar(60), JmsUsr varchar(40), JmsPwd varchar(60), DstNam varchar(40) NOT NULL, Act int DEFAULT 1);
CREATE UNIQUE INDEX AweQueI1 ON AweQue (Als);

--------------------------------------------------------
--  DDL for Table AweKey
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS AweKey (KeyNam varchar(20) CONSTRAINT pk_AweKey PRIMARY KEY NOT NULL, KeyVal int DEFAULT 0 NOT NULL, Act int default 1 not NULL);


--------------------------------------------------------
--  DDL for Historic Tables
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS HISAweAppPar (HISope varchar(20) not NULL, HISdat datetime not NULL, HISact varchar(1) not NULL, IdeAweAppPar int NULL, ParNam varchar(40) NULL, ParVal varchar(60) NULL, Cat int NULL, Des varchar(250) NULL, Act int DEFAULT 1 NULL);
CREATE INDEX HISAweAppParI1 ON HISAweAppPar (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweThm (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeThm int NULL, Nam varchar(100) NULL, Act int NULL);
CREATE INDEX HISAweThmI1 ON HISAweThm (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAwePro (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdePro int NULL, Acr varchar(3) NULL, Nam varchar(120) NULL, IdeThm int NULL, ScrIni varchar(40) NULL, Res varchar(40) NULL, Act int NULL);
CREATE INDEX HISAweProI1 ON HISAwePro (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISope (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeOpe int NULL, l1_nom char(20) NULL, l1_pas char(40) NULL, OpePas char(200) NULL, l1_con int NULL, l1_dev char(3) NULL, l1_act int NULL, l1_trt char(1) NULL, l1_uti int NULL, l1_opr char(6) NULL, l1_dat date NULL, imp_nom char(32) NULL, dat_mod date NULL, l1_psd date NULL, l1_lan char(3) NULL, l1_sgn int NULL, PcPrn varchar(255) NULL, EmlSrv varchar(10) NULL, EmlAdr varchar(50) NULL, OpeNam varchar(50) NULL, IdePro int not NULL, IdeThm int NULL, ScrIni varchar(40) NULL, Res varchar(40) NULL, ShwPrn int NULL, PwdLck int NULL, NumLog int NULL);
CREATE INDEX HISopeI1 ON HISope (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweDbs (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeDbs int NULL, Als char(16) NULL, Des char(40) NULL, Dct varchar (1) NULL, Dbt varchar (3) NULL, Drv varchar (256), DbsUsr varchar(50), DbsPwd varchar(50), Typ char(3) NULL, Dbc varchar(256) NULL, Act int NULL);
CREATE INDEX HISAweDbsI1 ON HISAweDbs (HISope, HISdat, HISact);
CREATE TABLE IF NOT EXISTS HISAweSit (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeSit int NULL, Nam varchar(100) NULL, Ord int NULL, Act int NULL );
CREATE INDEX HISAweSitI1 ON HISAweSit (HISope, HISdat, HISact);
CREATE TABLE HISAweMod (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeMod int NULL, Nam varchar(100) NULL, ScrIni varchar(40) NULL, IdeThm int NULL, Act int NULL);
CREATE INDEX HISAweModI1 ON HISAweMod (HISope, HISdat, HISact);
CREATE TABLE HISAweSitModDbs(HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeSitModDbs int NULL, IdeSit int NULL, IdeMod int NULL, IdeDbs int NULL, Ord int NULL);
CREATE INDEX HISAweSitModDbsI1 ON HISAweMod (HISope, HISdat, HISact);
CREATE TABLE HISAweModOpe (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeModOpe int NULL, IdeMod int NULL, IdeOpe int NULL, IdeThm int NULL);
CREATE INDEX HISAweModOpeI1 ON HISAweModOpe (HISope, HISdat, HISact);
CREATE TABLE HISAweModPro (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeModPro int NULL, IdeMod int NULL, IdePro int NULL);
CREATE INDEX HISAweModProI1 ON HISAweModPro (HISope, HISdat, HISact);
CREATE TABLE HISAweScrPro (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeScrPro int NULL, IdePro int NULL, IdeMod int NULL, Scr varchar(40) NULL, AccMod varchar(1) NULL, Act int NULL);
CREATE INDEX HISAweScrProI1 ON HISAweScrPro (HISope, HISdat, HISact);
CREATE TABLE HISAweScrOpe (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeScrOpe int NULL, IdeOpe varchar(20) NULL, IdeMod int NULL, Scr varchar(40) NULL, AccMod varchar(1) NULL, Act int NULL);
CREATE INDEX HISAweScrOpeI1 ON HISAweScrOpe (HISope, HISdat, HISact);
CREATE TABLE HISAweScrMod (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeScrMod int NULL, Scr varchar(20) NULL, IdeMod int NULL, Act int NULL);
CREATE INDEX HISAweScrModI1 ON HISAweScrMod (HISope, HISdat, HISact);
CREATE TABLE HISAweEmlSrv (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeAweEmlSrv int NULL, SrvNam varchar(40) NULL, Hst varchar(60) NULL, Prt int NULL, Ath int DEFAULT 0 NULL, EmlUsr varchar(40) NULL, EmlPwd varchar(240) NULL, Act int NULL);
CREATE INDEX HISAweEmlSrvI1 ON HISAweEmlSrv (HISope, HISdat, HISact);
CREATE TABLE HISAweScrCnf (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeAweScrCnf int NULL, IdeOpe int NULL, IdePro int NULL, Scr varchar(40) NULL, Nam varchar(40) NULL, Atr varchar(40) NULL, Val varchar(60) NULL, Act int NULL);
CREATE INDEX HISAweScrCnfI1 ON HISAweScrCnf (HISope, HISdat, HISact);
CREATE TABLE HISAweScrRes (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeAweScrRes int NULL, IdeOpe int NULL, IdePro int NULL, IdeMod int NULL, Opt varchar(40) NULL, AccMod varchar(1) NULL, Act int NULL);
CREATE INDEX HISAweScrResI1 ON HISAweScrRes (HISope, HISdat, HISact);
CREATE TABLE HISAweQue (HISope varchar(20) not NULL, HISdat date not NULL, HISact varchar(1) not NULL, IdeAweQue int NULL, Als varchar(40) NULL, Des varchar(60), QueTyp varchar(5) NULL, ConTyp varchar(1) NULL, JmsBrk varchar(60), JmsUsr varchar(40), JmsPwd varchar(60), DstNam varchar(40) NULL, Act int DEFAULT 1);
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
ALTER TABLE AweScrPro ADD CONSTRAINT fk_AweScrPro1 FOREIGN KEY (IdePro) REFERENCES AwePro (IdePro);
ALTER TABLE AweScrPro ADD CONSTRAINT fk_AweScrPro2 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweScrOpe ADD CONSTRAINT fk_AweScrOpe1 FOREIGN KEY (IdeOpe) REFERENCES ope (IdeOpe);
ALTER TABLE AweScrOpe ADD CONSTRAINT fk_AweScrOpe2 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweScrMod ADD CONSTRAINT fk_AweScrMod1 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweScrCnf ADD CONSTRAINT fk_AweScrCnf1 FOREIGN KEY (IdeOpe) REFERENCES ope (IdeOpe);

ALTER TABLE AweScrCnf
ADD CONSTRAINT fk_AweScrCnf2 FOREIGN KEY
(
 IdePro
)
REFERENCES AwePro
(
 IdePro
);