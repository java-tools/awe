--------------------------------------------------------
--  DDL for Schema AWE
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.schemas
              WHERE name = N'AWE')
    EXEC ('CREATE SCHEMA AWE');

--------------------------------------------------------
--  DDL for Table AweAppPar
--  Application parameters table: Allows to configure specific parameters in the application
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweAppPar'
                AND type = 'U')
CREATE TABLE AweAppPar
(
    IdeAweAppPar int           NOT NULL PRIMARY KEY, --- Table identifier
    ParNam       varchar(40)   NOT NULL,             --- Parameter name
    ParVal       varchar(60)   NULL,                 ---  Parameter value
    Cat          int           NOT NULL,             ---  Parameter category: General (1), Reporting (2), Security (3)
    Des          varchar(250)  NULL,                 --- Parameter description
    Act          int DEFAULT 1 NOT NULL              --- Active (1) or not (0)
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweAppParI1')
CREATE UNIQUE INDEX AweAppParI1 ON AweAppPar (ParNam);

--------------------------------------------------------
--  DDL for Table AweThm
--  Themes table: List of available themes
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweThm'
                AND type = 'U')
CREATE TABLE AweThm
(
    IdeThm int           NOT NULL PRIMARY KEY, --- Theme key
    Nam    varchar(100)  not NULL,             --- Theme name
    Act    int default 1 not NULL              --- Active (1) or not (0)
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweThmI1')
CREATE UNIQUE INDEX AweThmI1 ON AweThm (Nam);

--------------------------------------------------------
--  DDL for Table AwePro
--  Profiles table: List of application profiles
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AwePro'
                AND type = 'U')
CREATE TABLE AwePro
(
    IdePro int           NOT NULL PRIMARY KEY, --- Profile key
    Acr    varchar(3)    not NULL,             --- Profile acronym (3 chars)
    Nam    varchar(120)  not NULL,             --- Profile name
    IdeThm int           NULL,                 --- Default theme identifier for profile
    ScrIni varchar(40)   NULL,                 --- Initial screen for profile
    Res    varchar(40)   NULL,                 --- Profile restriction file (listed on profile folder)
    Act    int default 1 not NULL              --- Active (1) or not (0)
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweProI1')
CREATE UNIQUE INDEX AweProI1 ON AwePro (Nam);

--------------------------------------------------------
--  DDL for Table ope
--  Operators table: List of application users
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'ope'
                AND type = 'U')
CREATE TABLE ope
(
    IdeOpe  int NOT NULL PRIMARY KEY, --- Operator key
    l1_nom  char(20),                 --- User name
    l1_pas  char(40),                 --- User password hash
    OpePas  char(200),                --- User password hash (old)
    l1_con  int      DEFAULT 0,       --- Connected (1) or not (0)
    l1_dev  char(3),                  --- unused
    l1_act  int      DEFAULT 1,       --- Active (1) or not (0)
    l1_trt  char(1),                  --- unused
    l1_uti  int,                      --- unused
    l1_opr  char(6),                  --- unused
    l1_dat  DATE,                     --- Last connection date
    imp_nom char(32) DEFAULT 'none',
    dat_mod datetime,                 --- User update date
    l1_psd  datetime,                 --- Date of password expiration
    l1_lan  char(3),                  --- User language
    l1_sgn  int,                      --- User signed
    PcPrn   varchar(255),             --- User printer
    EmlSrv  varchar(10),              --- Email server
    EmlAdr  varchar(50),              --- Email address
    OpeNam  varchar(50),              --- User full name
    IdePro  int,                      --- User profile
    IdeThm  int,                      --- User theme
    ScrIni  varchar(40),              --- User initial screen
    Res     varchar(40),              --- User specific restriction profile
    ShwPrn  int,                      --- Allow user to print (1) or not (0)
    WebPrn  varchar(255),             --- User web printer
    PwdLck  int      DEFAULT 0,       --- Password locked (1) or not (0)
    NumLog  int      DEFAULT 0        --- Number of times logged in concurrently
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'opeI1')
CREATE UNIQUE INDEX opeI1 ON ope (l1_nom);

--------------------------------------------------------
--  DDL for Table AweDbs
--  Database table: List of application database connections
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweDbs'
                AND type = 'U')
CREATE TABLE AweDbs
(
    IdeDbs int           NOT NULL PRIMARY KEY, --- Database key
    Als    varchar(16)   not NULL,             --- Database alias
    Des    varchar(40)   NULL,                 --- Database description
    Dct    varchar(1)    not NULL,             --- Database connection type: (J) JDBC, (D) Datasource
    Dbt    varchar(10)   not NULL,             --- Database type (ora) Oracle, (sqs) SQL Server, (hsql) HSQLDB, (h2) H2 Database, (mysql) MySQL/MariaDB
    Drv    varchar(256),                       --- Database driver
    DbsUsr varchar(50),                        --- Database username
    DbsPwd varchar(50),                        --- Database password (encrypted)
    Typ    varchar(3)    not NULL,             --- Database environment: (Des) Development, (Pre) Pre-production, (Pro) Production
    Dbc    varchar(256)  not NULL,             --- Database connection: JDBC database connection URL
    Act    int default 1 not NULL              --- Active (1) or not (0)
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweDbsI1')
CREATE UNIQUE INDEX AweDbsI1 ON AweDbs (Als);

--------------------------------------------------------
--  DDL for Table AweSit
--  Sites table: List of available application sites
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweSit'
                AND type = 'U')
CREATE TABLE AweSit
(
    IdeSit int           NOT NULL PRIMARY KEY, --- Site key
    Nam    varchar(100)  NOT NULL,             --- Site name
    Ord    int           NULL,                 --- Site order (in selector)
    Act    int default 1 not NULL              --- Active (1) or not (0)
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweSitI1')
CREATE UNIQUE INDEX AweSitI1 ON AweSit (Nam);

--------------------------------------------------------
--  DDL for Table AweMod
--  Module table: List of awe modules
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweMod'
                AND type = 'U')
CREATE TABLE AweMod
(
    IdeMod int           NOT NULL PRIMARY KEY, --- Module key
    Nam    varchar(100)  not NULL,             --- Module name
    ScrIni varchar(40)   NULL,                 --- Module initial screen (deprecated)
    IdeThm int           NULL,                 --- Module theme (deprecated)
    Act    int default 1 not NULL,             --- Active (1) or not (0)
    Ord    int           NULL                  --- Value to recover modules sorted as convenience

);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweModI1')
CREATE UNIQUE INDEX AweModI1 ON AweMod (Nam);

--------------------------------------------------------
--  DDL for Table AweSitModDbs
--  Sites-Modules-Databases relationship table
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweSitModDbs'
                AND type = 'U')
CREATE TABLE AweSitModDbs
(
    IdeSitModDbs int NOT NULL PRIMARY KEY, --- Relationship key
    IdeSit       int NOT NULL,             --- Site key
    IdeMod       int NOT NULL,             --- Module key
    IdeDbs       int NOT NULL,             --- Database key
    Ord          int NULL                  --- Relationship order
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweSitModDbsI1')
CREATE UNIQUE INDEX AweSitModDbsI1 ON AweSitModDbs (IdeSit, IdeMod, IdeDbs);

--------------------------------------------------------
--  DDL for Table AweModOpe
--  Operator modules table: Relationship between modules and users
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweModOpe'
                AND type = 'U')
CREATE TABLE AweModOpe
(
    IdeModOpe int NOT NULL PRIMARY KEY, --- Relationship key
    IdeMod    int NOT NULL,             --- Module key
    IdeOpe    int NOT NULL,             --- Operator key
    IdeThm    int NULL,                 --- Theme key (not used)
    Ord       int NULL                  --- Relationship order
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweModOpeI1')
CREATE UNIQUE INDEX AweModopeI1 ON AweModOpe (IdeMod, IdeOpe);

--------------------------------------------------------
--  DDL for Table AweModPro
--  Profile modules table: Relationship between modules and profiles
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweModPro'
                AND type = 'U')
CREATE TABLE AweModPro
(
    IdeModPro int NOT NULL PRIMARY KEY, --- Relationship key
    IdeMod    int NOT NULL,             --- Module key
    IdePro    int NOT NULL,             --- Profile key
    Ord       int NULL                  --- Relationship order
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweModProI1')
CREATE UNIQUE INDEX AweModProI1 ON AweModPro (IdeMod, IdePro);

--------------------------------------------------------
--  DDL for Table AweEmlSrv
--  Email servers table: List of available email servers on application
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweEmlSrv'
                AND type = 'U')
CREATE TABLE AweEmlSrv
(
    IdeAweEmlSrv int           NOT NULL PRIMARY KEY, --- Email server key
    SrvNam       varchar(40)   NOT NULL,             --- Server name
    Hst          varchar(60)   NOT NULL,             --- Server host
    Prt          int           NULL,                 --- Server port
    Ath          int DEFAULT 0 NOT NULL,             --- Needs authentication (1) or not (0)
    EmlUsr       varchar(40)   NULL,                 --- Server username
    EmlPwd       varchar(40)   NULL,                 --- Server password (encrypted)
    Act          int DEFAULT 1 NOT NULL              --- Active (1) or not (0)
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweEmlSrvI1')
CREATE UNIQUE INDEX AweEmlSrvI1 ON AweEmlSrv (SrvNam);

--------------------------------------------------------
--  DDL for Table AweScrCnf
--  Screen configuration table: Screen component overload
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweScrCnf'
                AND type = 'U')
CREATE TABLE AweScrCnf
(
    IdeAweScrCnf int           NOT NULL PRIMARY KEY, --- Screen configuration key
    IdeOpe       int           NULL,                 --- Operator key
    IdePro       int           NULL,                 --- Profile key
    Scr          varchar(40)   NOT NULL,             --- Option name
    Nam          varchar(40)   NOT NULL,             --- Component identifier
    Atr          varchar(40)   NOT NULL,             --- Attribute to overload
    Val          varchar(60)   NULL,                 --- Attribute value
    Act          int DEFAULT 1 NOT NULL              --- Active (1) or not (0)
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweScrCnfI1')
CREATE INDEX AweScrCnfI1 ON AweScrCnf (Nam, Atr, Val);

--------------------------------------------------------
--  DDL for Table AweScrRes
--  Screen restriction table: Restricts the access to screens to users or profiles
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweScrRes'
                AND type = 'U')
CREATE TABLE AweScrRes
(
    IdeAweScrRes int           NOT NULL PRIMARY KEY, --- Screen restriction key
    IdeOpe       int           NULL,                 --- Operator key
    IdePro       int           NULL,                 --- Profile key
    IdeMod       int           NULL,                 --- Module key (deprecated)
    Opt          varchar(40)   NOT NULL,             --- Option name
    AccMod       varchar(1)    NOT NULL,             --- Access type: (R) Restricted (A) Allowed
    Act          int DEFAULT 1 NOT NULL              --- Active (1) or not (0)
);

--------------------------------------------------------
--  DDL for Table AweQue
--  Queue definition table: List of available JMS queues on application
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweQue'
                AND type = 'U')
CREATE TABLE AweQue
(
    IdeAweQue int         NOT NULL PRIMARY KEY, --- Queue key
    Als       varchar(40) NOT NULL,             --- Queue alias
    Des       varchar(60),                      --- Queue description
    QueTyp    varchar(5)  NOT NULL,             --- Queue type
    ConTyp    varchar(1)  NOT NULL,             --- Connection type
    JmsBrk    varchar(60),                      --- JMS Broker
    JmsUsr    varchar(40),                      --- JMS Username
    JmsPwd    varchar(60),                      --- JMS Password (encrypted)
    DstNam    varchar(40) NOT NULL,             --- Destination name
    Act       int DEFAULT 1                     --- Active (1) or not (0)
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'AweQueI1')
CREATE UNIQUE INDEX AweQueI1 ON AweQue (Als);

--------------------------------------------------------
--  DDL for Table AweKey
--  Awe Sequences table: List of available sequences
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'AweKey'
                AND type = 'U')
CREATE TABLE AweKey
(
    KeyNam varchar(20)   NOT NULL PRIMARY KEY, --- Sequence key
    KeyVal int DEFAULT 0 NOT NULL,             --- Sequence value
    Act    int default 1 not NULL              --- Active (1) or not (0)
);

--------------------------------------------------------
--  DDL for Historic Tables
--  Same fields as plain tables but with 3 key audit fields:
--  - HISope Username who has made the change
--  - HISdat Date of audit
--  - HISact Action made: (I) Insert, (U) Update, (D) Delete
--------------------------------------------------------
IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweAppPar'
                AND type = 'U')
CREATE TABLE HISAweAppPar
(
    HISope       varchar(20)   not NULL,
    HISdat       datetime      not NULL,
    HISact       varchar(1)    not NULL,
    IdeAweAppPar int           NULL,
    ParNam       varchar(40)   NULL,
    ParVal       varchar(60)   NULL,
    Cat          int           NULL,
    Des          varchar(250)  NULL,
    Act          int DEFAULT 1 NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweAppParI1')
CREATE INDEX HISAweAppParI1 ON HISAweAppPar (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweThm'
                AND type = 'U')
CREATE TABLE HISAweThm
(
    HISope varchar(20)  not NULL,
    HISdat date         not NULL,
    HISact varchar(1)   not NULL,
    IdeThm int          NULL,
    Nam    varchar(100) NULL,
    Act    int          NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweThmI1')
CREATE INDEX HISAweThmI1 ON HISAweThm (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAwePro'
                AND type = 'U')
CREATE TABLE HISAwePro
(
    HISope varchar(20)  not NULL,
    HISdat date         not NULL,
    HISact varchar(1)   not NULL,
    IdePro int          NULL,
    Acr    varchar(3)   NULL,
    Nam    varchar(120) NULL,
    IdeThm int          NULL,
    ScrIni varchar(40)  NULL,
    Res    varchar(40)  NULL,
    Act    int          NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweProI1')
CREATE INDEX HISAweProI1 ON HISAwePro (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISope'
                AND type = 'U')
CREATE TABLE HISope
(
    HISope  varchar(20)  not NULL,
    HISdat  date         not NULL,
    HISact  varchar(1)   not NULL,
    IdeOpe  int          NULL,
    l1_nom  char(20)     NULL,
    l1_pas  char(40)     NULL,
    OpePas  char(200)    NULL,
    l1_con  int          NULL,
    l1_dev  char(3)      NULL,
    l1_act  int          NULL,
    l1_trt  char(1)      NULL,
    l1_uti  int          NULL,
    l1_opr  char(6)      NULL,
    l1_dat  date         NULL,
    imp_nom char(32)     NULL,
    dat_mod date         NULL,
    l1_psd  date         NULL,
    l1_lan  char(3)      NULL,
    l1_sgn  int          NULL,
    PcPrn   varchar(255) NULL,
    EmlSrv  varchar(10)  NULL,
    EmlAdr  varchar(50)  NULL,
    OpeNam  varchar(50)  NULL,
    IdePro  int          not NULL,
    IdeThm  int          NULL,
    ScrIni  varchar(40)  NULL,
    Res     varchar(40)  NULL,
    ShwPrn  int          NULL,
    PwdLck  int          NULL,
    NumLog  int          NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISopeI1')
CREATE INDEX HISopeI1 ON HISope (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweDbs'
                AND type = 'U')
CREATE TABLE HISAweDbs
(
    HISope varchar(20)  not NULL,
    HISdat date         not NULL,
    HISact varchar(1)   not NULL,
    IdeDbs int          NULL,
    Als    char(16)     NULL,
    Des    char(40)     NULL,
    Dct    varchar(1)   NULL,
    Dbt    varchar(10)  NULL,
    Drv    varchar(256),
    DbsUsr varchar(50),
    DbsPwd varchar(50),
    Typ    char(3)      NULL,
    Dbc    varchar(256) NULL,
    Act    int          NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweDbsI1')
CREATE INDEX HISAweDbsI1 ON HISAweDbs (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweSit'
                AND type = 'U')
CREATE TABLE HISAweSit
(
    HISope varchar(20)  not NULL,
    HISdat date         not NULL,
    HISact varchar(1)   not NULL,
    IdeSit int          NULL,
    Nam    varchar(100) NULL,
    Ord    int          NULL,
    Act    int          NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweSitI1')
CREATE INDEX HISAweSitI1 ON HISAweSit (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweMod'
                AND type = 'U')
CREATE TABLE HISAweMod
(
    HISope varchar(20)  not NULL,
    HISdat date         not NULL,
    HISact varchar(1)   not NULL,
    IdeMod int          NULL,
    Nam    varchar(100) NULL,
    ScrIni varchar(40)  NULL,
    IdeThm int          NULL,
    Act    int          NULL,
    Ord    int          NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweModI1')
CREATE INDEX HISAweModI1 ON HISAweMod (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweSitModDbs'
                AND type = 'U')
CREATE TABLE HISAweSitModDbs
(
    HISope       varchar(20) not NULL,
    HISdat       date        not NULL,
    HISact       varchar(1)  not NULL,
    IdeSitModDbs int         NULL,
    IdeSit       int         NULL,
    IdeMod       int         NULL,
    IdeDbs       int         NULL,
    Ord          int         NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweSitModDbsI1')
CREATE INDEX HISAweSitModDbsI1 ON HISAweSitModDbs (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweModOpe'
                AND type = 'U')
CREATE TABLE HISAweModOpe
(
    HISope    varchar(20) not NULL,
    HISdat    date        not NULL,
    HISact    varchar(1)  not NULL,
    IdeModOpe int         NULL,
    IdeMod    int         NULL,
    IdeOpe    int         NULL,
    IdeThm    int         NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweModOpeI1')
CREATE INDEX HISAweModOpeI1 ON HISAweModOpe (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweModPro'
                AND type = 'U')
CREATE TABLE HISAweModPro
(
    HISope    varchar(20) not NULL,
    HISdat    date        not NULL,
    HISact    varchar(1)  not NULL,
    IdeModPro int         NULL,
    IdeMod    int         NULL,
    IdePro    int         NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweModProI1')
CREATE INDEX HISAweModProI1 ON HISAweModPro (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweEmlSrv'
                AND type = 'U')
CREATE TABLE HISAweEmlSrv
(
    HISope       varchar(20)   not NULL,
    HISdat       date          not NULL,
    HISact       varchar(1)    not NULL,
    IdeAweEmlSrv int           NULL,
    SrvNam       varchar(40)   NULL,
    Hst          varchar(60)   NULL,
    Prt          int           NULL,
    Ath          int DEFAULT 0 NULL,
    EmlUsr       varchar(40)   NULL,
    EmlPwd       varchar(240)  NULL,
    Act          int           NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweEmlSrvI1')
CREATE INDEX HISAweEmlSrvI1 ON HISAweEmlSrv (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweScrCnf'
                AND type = 'U')
CREATE TABLE HISAweScrCnf
(
    HISope       varchar(20) not NULL,
    HISdat       date        not NULL,
    HISact       varchar(1)  not NULL,
    IdeAweScrCnf int         NULL,
    IdeOpe       int         NULL,
    IdePro       int         NULL,
    Scr          varchar(40) NULL,
    Nam          varchar(40) NULL,
    Atr          varchar(40) NULL,
    Val          varchar(60) NULL,
    Act          int         NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweScrCnfI1')
CREATE INDEX HISAweScrCnfI1 ON HISAweScrCnf (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweScrRes'
                AND type = 'U')
CREATE TABLE HISAweScrRes
(
    HISope       varchar(20) not NULL,
    HISdat       date        not NULL,
    HISact       varchar(1)  not NULL,
    IdeAweScrRes int         NULL,
    IdeOpe       int         NULL,
    IdePro       int         NULL,
    IdeMod       int         NULL,
    Opt          varchar(40) NULL,
    AccMod       varchar(1)  NULL,
    Act          int         NULL
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweScrResI1')
CREATE INDEX HISAweScrResI1 ON HISAweScrRes (HISope, HISdat, HISact);

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'HISAweQue'
                AND type = 'U')
CREATE TABLE HISAweQue
(
    HISope    varchar(20) not NULL,
    HISdat    date        not NULL,
    HISact    varchar(1)  not NULL,
    IdeAweQue int         NULL,
    Als       varchar(40) NULL,
    Des       varchar(60),
    QueTyp    varchar(5)  NULL,
    ConTyp    varchar(1)  NULL,
    JmsBrk    varchar(60),
    JmsUsr    varchar(40),
    JmsPwd    varchar(60),
    DstNam    varchar(40) NULL,
    Act       int DEFAULT 1
);
IF NOT EXISTS(SELECT *
              FROM sys.indexes
              WHERE name = 'HISAweQueI1')
CREATE INDEX HISAweQueI1 ON HISAweQue (HISope, HISdat, HISact);

--------------------------------------------------------
--  DDL for CONSTRAINTS
--------------------------------------------------------
ALTER TABLE AwePro
    ADD CONSTRAINT fk_AwePro1 FOREIGN KEY (IdeThm) REFERENCES AweThm (IdeThm);
ALTER TABLE AwePro
    ADD CONSTRAINT uq_AwePro UNIQUE (Acr);
ALTER TABLE ope
    ADD CONSTRAINT fk_ope1 FOREIGN KEY (IdePro) REFERENCES AwePro (IdePro);
ALTER TABLE ope
    ADD CONSTRAINT fk_ope2 FOREIGN KEY (IdeThm) REFERENCES AweThm (IdeThm);
ALTER TABLE AweMod
    ADD CONSTRAINT fk_AweMod1 FOREIGN KEY (IdeThm) REFERENCES AweThm (IdeThm);
ALTER TABLE AweSitModDbs
    ADD CONSTRAINT fk_AweSitModDbs1 FOREIGN KEY (IdeSit) REFERENCES AweSit (IdeSit);
ALTER TABLE AweSitModDbs
    ADD CONSTRAINT fk_AweSitModDbs2 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweSitModDbs
    ADD CONSTRAINT fk_AweSitModDbs3 FOREIGN KEY (IdeDbs) REFERENCES AweDbs (IdeDbs);
ALTER TABLE AweModOpe
    ADD CONSTRAINT fk_AweModOpe1 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweModOpe
    ADD CONSTRAINT fk_AweModOpe2 FOREIGN KEY (IdeOpe) REFERENCES ope (IdeOpe);
ALTER TABLE AweModOpe
    ADD CONSTRAINT fk_AweModOpe3 FOREIGN KEY (IdeThm) REFERENCES AweThm (IdeThm);
ALTER TABLE AweModPro
    ADD CONSTRAINT fk_AweModPro1 FOREIGN KEY (IdeMod) REFERENCES AweMod (IdeMod);
ALTER TABLE AweModPro
    ADD CONSTRAINT fk_AweModPro2 FOREIGN KEY (IdePro) REFERENCES AwePro (IdePro);
ALTER TABLE AweScrCnf
    ADD CONSTRAINT fk_AweScrCnf1 FOREIGN KEY (IdeOpe) REFERENCES ope (IdeOpe);
ALTER TABLE AweScrCnf
    ADD CONSTRAINT fk_AweScrCnf2 FOREIGN KEY (IdePro) REFERENCES AwePro (IdePro);

--------------------------------------------------------
--  DDL for TEST AUTO INCREMENT TABLE
--------------------------------------------------------
CREATE TABLE TestAutoIncrement
(
    id    INT IDENTITY (100,1) PRIMARY KEY,
    name  VARCHAR(30),
    email VARCHAR(50)
);


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
    ExeTsk INT      not NULL,
    IniDat DATETIME not NULL,
    EndDat DATETIME,
    ExeTim INT,
    Sta    INT      not NULL,
    LchBy VARCHAR(200),
    Des VARCHAR(2000)
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
    db        VARCHAR(200)  not NULL,
    site      VARCHAR(200)  not NULL
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
CREATE INDEX AweSchExeI1 ON AweSchExe (IdeTsk, GrpTsk, ExeTsk, IniDat);
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