--------------------------------------------------------
--  NOTIFIER DDL
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Table AweSub
--  Subscriptions
--------------------------------------------------------
create TABLE AweSub
(
    Ide INTEGER           not NULL,
    Acr VARCHAR(10)       not NULL,
    Nom VARCHAR(30)       not NULL,
    Des VARCHAR(250),
    Act INTEGER DEFAULT 1 not NULL
);

--------------------------------------------------------
--  DDL for Table AweSubUsr
--  User subscriptions
--------------------------------------------------------
create TABLE AweSubUsr
(
    Ide    INTEGER   not NULL,
    IdeOpe INTEGER   not NULL,
    IdeSub INTEGER   not NULL,
    SubNot INTEGER DEFAULT 1 not NULL,
    SubEma INTEGER DEFAULT 0 not NULL
);

--------------------------------------------------------
--  DDL for Table AweNot
--  Notifications
--------------------------------------------------------
create TABLE AweNot
(
    Ide    INTEGER     not NULL,
    IdeSub INTEGER     not NULL,
    Typ    VARCHAR(10) not NULL,
    Ico    VARCHAR(30) not NULL,
    Nom    VARCHAR(30) not NULL,
    Des    VARCHAR(250),
    Scr    VARCHAR(100),
    Cod    VARCHAR(100),
    Dat    DATETIME    not NULL
);

--------------------------------------------------------
--  DDL for Table AweNotUsr
--  User notifications
--------------------------------------------------------
create TABLE AweNotUsr
(
    Ide    INTEGER           not NULL,
    IdeOpe INTEGER           not NULL,
    IdeNot INTEGER           not NULL,
    Unr    INTEGER DEFAULT 1 not NULL
);

--------------------------------------------------------
--  DDL for HISTORIC TABLES
--------------------------------------------------------

create TABLE HisAweSub
(
    HISope VARCHAR(20),
    HISdat DATE,
    HISact VARCHAR(1),
    Ide    INTEGER,
    Acr    VARCHAR(10),
    Nom    VARCHAR(30),
    Des    VARCHAR(250),
    Act    INTEGER
);

create TABLE HisAweSubUsr
(
    HISope VARCHAR(20),
    HISdat DATE,
    HISact VARCHAR(1),
    Ide    INTEGER,
    IdeOpe INTEGER,
    IdeSub INTEGER,
    SubNot INTEGER,
    SubEma INTEGER
);

create TABLE HisAweNot
(
    HISope VARCHAR(20) not NULL,
    HISdat DATE        not NULL,
    HISact VARCHAR(1)  not NULL,
    Ide    INTEGER,
    IdeSub INTEGER,
    Typ    VARCHAR(10),
    Ico    VARCHAR(30),
    Nom    VARCHAR(30),
    Des    VARCHAR(250),
    Scr    VARCHAR(100),
    Cod    VARCHAR(100),
    Dat    DATETIME
);

create TABLE HisAweNotUsr
(
    HISope    VARCHAR(20) not NULL,
    HISdat    DATE        not NULL,
    HISact    VARCHAR(1)  not NULL,
    Ide       INTEGER,
    IdeOpe    INTEGER,
    IdeNot    INTEGER,
    Unr       INTEGER
);

--------------------------------------------------------
--  DDL for CONSTRAINTS
--------------------------------------------------------

create UNIQUE INDEX PK_AWESUB ON AweSub (Ide);
create UNIQUE INDEX PK_AWESUBUSR ON AweSubUsr (Ide);
create UNIQUE INDEX PK_AWENOT ON AweNot (Ide);
create UNIQUE INDEX PK_AWENOTUSR ON AweNotUsr (Ide);
create INDEX HisAweSubI1 ON HisAweSub (HISope, HISdat, HISact);
create INDEX HisAweSubUsrI1 ON HisAweSubUsr (HISope, HISdat, HISact);
create INDEX HisAweNotI1 ON HisAweNot (HISope, HISdat, HISact);
create INDEX HisAweNotUsrI1 ON HisAweNotUsr (HISope, HISdat, HISact);
alter table AweSub add CONSTRAINT PK_AWESUB PRIMARY KEY (Ide);
alter table AweSubUsr add CONSTRAINT PK_AWESUBUSR PRIMARY KEY (Ide);
alter table AweNot add CONSTRAINT PK_AWENOT PRIMARY KEY (Ide);
alter table AweNotUsr add CONSTRAINT PK_AWENOTUSR PRIMARY KEY (Ide);

-- Notifier sequences
insert into AweKey (KeyNam, KeyVal) values ('Sub', (select coalesce(max(Ide),0) + 1 from AweSub));
insert into AweKey (KeyNam, KeyVal) values ('SubUsr', (select coalesce(max(Ide),0) + 1 from AweSubUsr));
insert into AweKey (KeyNam, KeyVal) values ('Not', (select coalesce(max(Ide),0) + 1 from AweNot));
insert into AweKey (KeyNam, KeyVal) values ('NotUsr', (select coalesce(max(Ide),0) + 1 from AweNotUsr));

