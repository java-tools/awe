--------------------------------------------------------
--  SCHEDULER DDL
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Table AweSchCal
--  Calendar list
--------------------------------------------------------
CREATE TABLE AweSchCal
(
    Ide  INT           not NULL,
    Des  VARCHAR(250)  not NULL,
    Act  INT DEFAULT 1 not NULL,
    Nom  VARCHAR(100)  not NULL,
    db   VARCHAR(200)  not NULL,
    site VARCHAR(200)  not NULL
);

--------------------------------------------------------
--  DDL for Table AweSchCalDat
--  Calendar dates
--------------------------------------------------------
CREATE TABLE AweSchCalDat
(
    Ide    INT not NULL,
    IdeCal INT not NULL,
    Nom    VARCHAR(40),
    Dat    DATETIME
);

--------------------------------------------------------
--  DDL for Table AweSchTskFilMod
--  Task file
--------------------------------------------------------
CREATE TABLE AweSchTskFilMod
(
    IdeTsk INT          not NULL,
    FilPth VARCHAR(256) not NULL,
    ModDat DATETIME
);

--------------------------------------------------------
--  DDL for Table AweSchExe
--  Task executions
--------------------------------------------------------
CREATE TABLE AweSchExe
(
    IdeTsk INT      not NULL,
    GrpTsk VARCHAR(40) not NULL,
    IniDat DATETIME not NULL,
    EndDat DATETIME,
    ExeTim INT,
    Sta    INT      not NULL,
    LchBy  VARCHAR(200),
    Des    VARCHAR(2000)
);

--------------------------------------------------------
--  DDL for Table AweSchSrv
--  Scheduler servers
--------------------------------------------------------
CREATE TABLE AweSchSrv
(
    Ide INT           not NULL,
    Nom VARCHAR(40)   not NULL,
    Pro VARCHAR(10)   not NULL,
    Hst VARCHAR(40)   not NULL,
    Prt VARCHAR(10)   not NULL,
    Act INT DEFAULT 1 not NULL
);

--------------------------------------------------------
--  DDL for Table AweSchTsk
--  Scheduler tasks
--------------------------------------------------------
CREATE TABLE AweSchTsk
(
    Ide       INT           not NULL,
    Nam       VARCHAR(40)   not NULL,
    Des       VARCHAR(250),
    NumStoExe INT,
    TimOutExe INT,
    TypExe    INT           not NULL,
    IdeSrvExe INT,
    CmdExe    VARCHAR(250)  not NULL,
    TypLch    INT           not NULL,
    LchDepErr INT DEFAULT 0 not NULL,
    LchDepWrn INT DEFAULT 0 not NULL,
    LchSetWrn INT DEFAULT 0 not NULL,
    RepTyp    INT DEFAULT 0 not NULL,
    RepEmaSrv INT,
    RepSndSta VARCHAR(20),
    RepEmaDst VARCHAR(250),
    RepTit    VARCHAR(100),
    RepMsg    VARCHAR(250),
    Act       INT DEFAULT 1 not NULL,
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
CREATE TABLE AweSchTskDpn
(
    IdeTsk INT not NULL,
    IdePrn INT not NULL,
    IsBlk  INT,
    DpnOrd INT
);

--------------------------------------------------------
--  DDL for Table AweSchTskLch
--  Task launchers
--------------------------------------------------------
CREATE TABLE AweSchTskLch
(
    Ide      INT not NULL,
    IdeTsk   INT,
    RptNum   INT,
    RptTyp   INT,
    IniDat   DATETIME,
    EndDat   DATETIME,
    IniTim   VARCHAR(8),
    EndTim   VARCHAR(8),
    IdeCal   INT,
    IdSrv    INT,
    Pth      VARCHAR(250),
    Pat      VARCHAR(250),
    ExeHrs   VARCHAR(200),
    ExeMth   VARCHAR(200),
    ExeWek   VARCHAR(200),
    ExeDay   VARCHAR(200),
    ExeDte   DATETIME,
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
CREATE TABLE AweSchTskPar
(
    Ide    INT          not NULL,
    IdeTsk INT,
    Nam    VARCHAR(40)  not NULL,
    Val    VARCHAR(400),
    Src    INT          not NULL,
    Typ    VARCHAR(100) not NULL
);

--------------------------------------------------------
--  DDL for HISTORIC TABLES
--------------------------------------------------------

CREATE TABLE HISAweSchCal
(
    HISope VARCHAR(20) not NULL,
    HISdat DATETIME    not NULL,
    HISact VARCHAR(1)  not NULL,
    Ide    INT,
    Nom    VARCHAR(40),
    Des    VARCHAR(250),
    Act    INT,
    db     VARCHAR(200),
    site   VARCHAR(200)
);

CREATE TABLE HISAweSchCalDat
(
    HISope VARCHAR(20) not NULL,
    HISdat DATETIME    not NULL,
    HISact VARCHAR(1)  not NULL,
    Ide    INT,
    IdeCal INT,
    Nom    VARCHAR(40),
    Dat    DATETIME
);

CREATE TABLE HISAweSchSrv
(
    HISope VARCHAR(20) not NULL,
    HISdat DATETIME    not NULL,
    HISact VARCHAR(1)  not NULL,
    Ide    INT,
    Nom    VARCHAR(40),
    Pro    VARCHAR(10),
    Hst    VARCHAR(40),
    Prt    VARCHAR(10),
    Act    INT
);

CREATE TABLE HISAweSchTsk
(
    HISope    VARCHAR(20)  not NULL,
    HISdat    DATETIME     not NULL,
    HISact    VARCHAR(1)   not NULL,
    Ide       INT,
    IdePAR    INT,
    Nam       VARCHAR(40),
    Des       VARCHAR(250),
    NumStoExe INT,
    TimOutExe INT,
    TypExe    INT,
    IdeSrvExe INT,
    CmdExe    VARCHAR(250),
    TypLch    INT,
    LchDepErr INT,
    LchDepWrn INT,
    LchSetWrn INT,
    BLKPAR    INT,
    RepTyp    INT,
    RepEmaSrv INT,
    RepSndSta VARCHAR(20),
    RepEmaDst VARCHAR(250),
    RepTit    VARCHAR(100),
    RepMsg    VARCHAR(250),
    Act       INT,
    RepUsrDst VARCHAR(250),
    RepMntId  VARCHAR(200),
    CmdExePth VARCHAR(200),
    db        VARCHAR(200) not NULL,
    site      VARCHAR(200) not NULL
);

CREATE TABLE HISAweSchTskLch
(
    HISope   VARCHAR(20) not NULL,
    HISdat   DATETIME    not NULL,
    HISact   VARCHAR(1)  not NULL,
    Ide      INT,
    IdeTsk   INT,
    RptNum   INT,
    RptTyp   INT,
    IniDat   DATETIME,
    EndDat   DATETIME,
    IniTim   VARCHAR(8),
    EndTim   VARCHAR(8),
    IdeCal   INT,
    IdSrv    INT,
    Pth      VARCHAR(250),
    Pat      VARCHAR(250),
    ExeMth   VARCHAR(200),
    ExeWek   VARCHAR(200),
    ExeDay   VARCHAR(200),
    ExeHrs   VARCHAR(200),
    ExeDte   DATETIME,
    ExeTim   VARCHAR(8),
    WeekDays VARCHAR(200),
    ExeYrs   VARCHAR(200),
    ExeMin   VARCHAR(200),
    ExeSec   VARCHAR(200),
    SrvUsr   VARCHAR(200),
    SrvPwd   VARCHAR(200)
);

CREATE TABLE HISAweSchTskPar
(
    HISope VARCHAR(20) not NULL,
    HISdat DATETIME    not NULL,
    HISact VARCHAR(1)  not NULL,
    Ide    INT,
    IdeTsk INT,
    Nam    VARCHAR(40),
    Val    VARCHAR(400),
    Src    INT,
    Typ    VARCHAR(100)
);

--------------------------------------------------------
--  DDL for CONSTRAINTS
--------------------------------------------------------

ALTER TABLE AweSchCal
    ADD CONSTRAINT Nom_UQ UNIQUE (Nom);
ALTER TABLE AweSchCal
    ADD CONSTRAINT UN_AweSchCal UNIQUE (Ide);
ALTER TABLE AweSchCalDat
    ADD CONSTRAINT UN_AweSchCalDat UNIQUE (Ide);
CREATE INDEX AweSchExeI1 ON AweSchExe (IdeTsk, GrpTsk, IniDat);
ALTER TABLE AweSchSrv
    ADD CONSTRAINT UN_AweSchSrv UNIQUE (Ide);
ALTER TABLE AweSchTsk
    ADD CONSTRAINT UN_AweSchTsk UNIQUE (Ide);
ALTER TABLE AweSchTskDpn
    ADD CONSTRAINT UN_AweSchTskDpn UNIQUE (IdeTsk, IdePrn);
ALTER TABLE AweSchTskLch
    ADD CONSTRAINT UN_AweSchTskLch UNIQUE (Ide);
ALTER TABLE AweSchTskPar
    ADD CONSTRAINT UN_AweSchTskPar UNIQUE (Ide);

CREATE INDEX HISAweSchCalI1 ON HISAweSchCal (HISope, HISdat, HISact);
CREATE INDEX HISAweSchCalDatI1 ON HISAweSchCalDat (HISope, HISdat, HISact);
CREATE INDEX HISAweSchSrvI1 ON HISAweSchSrv (HISope, HISdat, HISact);
CREATE INDEX HISAweSchTskI1 ON HISAweSchTsk (HISope, HISdat, HISact);
CREATE INDEX HISAweSchTskLchI1 ON HISAweSchTskLch (HISope, HISdat, HISact);
CREATE INDEX HISAweSchTskParI1 ON HISAweSchTskPar (HISope, HISdat, HISact);

ALTER TABLE AweSchCal
    ADD CONSTRAINT PK_AweSchCal PRIMARY KEY (Ide);
ALTER TABLE AweSchCalDat
    ADD CONSTRAINT PK_AweSchCalDat PRIMARY KEY (Ide);
ALTER TABLE AweSchTskFilMod
    ADD CONSTRAINT PK_AweSchTskFilMod PRIMARY KEY (IdeTsk, FilPth);
ALTER TABLE AweSchSrv
    ADD CONSTRAINT PK_AweSchSrv PRIMARY KEY (Ide);
ALTER TABLE AweSchTsk
    ADD CONSTRAINT PK_AweSchTsk PRIMARY KEY (Ide);
ALTER TABLE AweSchTskDpn
    ADD PRIMARY KEY (IdeTsk, IdePrn);
ALTER TABLE AweSchTskLch
    ADD CONSTRAINT PK_AweSchTskLch PRIMARY KEY (Ide);
ALTER TABLE AweSchTskPar
    ADD CONSTRAINT PK_AweSchTskPar PRIMARY KEY (Ide);