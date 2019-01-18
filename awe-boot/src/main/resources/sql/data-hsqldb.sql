
--------------------------------------------------------
--  DDL for INSERT DATA
--------------------------------------------------------
-- Insert sequences
Insert into AweKey (KeyNam, KeyVal) values ('OpeKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ThmKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ProKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ModKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('DbsKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('SitKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ModOpeKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ModProKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('SitModDbsKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ScrOpeKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ScrProKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('EmlSrvKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('AppParKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('JmsKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ScrCnfKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ScrResKey', 0);

-- Insert themes
Insert into AweThm (IdeThm, Nam, Act) values ('1','sunset','1');
Insert into AweThm (IdeThm, Nam, Act) values ('2','sky','1');
Insert into AweThm (IdeThm, Nam, Act) values ('3','eclipse','1');
Insert into AweThm (IdeThm, Nam, Act) values ('4','grass','1');
Insert into AweThm (IdeThm, Nam, Act) values ('5','sunny','1');
Insert into AweThm (IdeThm, Nam, Act) values ('6','purple-hills','1');
Insert into AweThm (IdeThm, Nam, Act) values ('7','frost','1');
Insert into AweThm (IdeThm, Nam, Act) values ('8','fresh','1');
Insert into AweThm (IdeThm, Nam, Act) values ('9','silver','1');
Insert into AweThm (IdeThm, Nam, Act) values ('10','clean','1');
Insert into AweThm (IdeThm, Nam, Act) values ('11','default','1');
Insert into AweThm (IdeThm, Nam, Act) values ('12','adminflare','1');
Insert into AweThm (IdeThm, Nam, Act) values ('13','dust','1');
Insert into AweThm (IdeThm, Nam, Act) values ('14','white','1');
Insert into AweThm (IdeThm, Nam, Act) values ('15','asphalt','1');
Insert into AweThm (IdeThm, Nam, Act) values ('16','orange','1');
Insert into AweThm (IdeThm, Nam, Act) values ('17','sea','1');
Insert into AweThm (IdeThm, Nam, Act) values ('18','ferrari','1');
-- Update ThmKey
UPDATE AweKey SET KeyVal = '19' where KeyNam = 'ThmKey';

-- Insert Awe parameters
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('1','DjrRepHisPth','/tmp/','2','Reports historize directory','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('2','DjrSubTitStl','0','2','Put subtitle in different lines. ( Value=0 Inactive / Value=1 Active)','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('3','DjrMgeOpt','0','2','Merge Reports trying to put two or more grids or charts in same page (0 Inactive/ 1 Active)','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('4','DjrRepPth','/tmp/','2','Generated report save directory','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('5','DjrSepTck','1','2','Thickness for separator type columns','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('6','MinPwd','3','1','Minimal number of characters in the password','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('7','PwdPat',null,'1','Password pattern to validate','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('8','PwdMaxNumLog','3','1','Number of attempts to login','0');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('9','PwdExp','30','1','Number of days in which the password will expire','0');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('10','DjrFntVer','8','2','Select minimun font size for vertically oriented reports','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('11','DjrFntHor','8','2','Select minimun font size for horizontally oriented reports','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('12','DjrCrtNum','5','2','Set the number of criteria to PROMPT in one column','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('13','DjrMinMar ','20','2','Select minimun margin that will be used for Jasper','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('14','DjrRmvLin','0','2','Remove all the cell borders except the ones in the column header','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('15','DjrDefFnt',null,'2','Default jasper reports font','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('16','MaxFntHor','10','2','Maximum font size for horizontal alignment','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('17','MaxFntVer','10','2','Maximum font size for vertical alignment','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('18','DjrHdgPag','0','2','Remove heading and pagination when exorting to excel','1');
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('19','DjrBokTit','0','2','Include book in report title','1');
-- Update AppParKey
UPDATE AweKey SET KeyVal = '20' where KeyNam = 'AppParKey';

-- Insert default profiles
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('1','ADM','administrator','1',null,'administrator','1');
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('2','GNR','general','1',null,'general','1');
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('3','OPE','operator','1',null,'operator','1');
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('4','TST','test','1',null,'test','1');
-- Update ProKey
UPDATE AweKey SET KeyVal = '5' where KeyNam = 'ProKey';

-- Insert default user
Insert into OPE (IdeOpe, l1_nom, l1_pas, l1_con, l1_dev, l1_act, l1_trt, l1_uti, l1_opr, l1_dat, imp_nom, dat_mod, l1_psd, l1_lan, l1_sgn, PcPrn, EmlSrv, EmlAdr, OpeNam, IdePro, IdeThm) values ((select KeyVal from AweKey where KeyNam = 'OpeKey' ),'test','5e52fee47e6b070565f74372468cdc699de89107',0,null,1,null,0,null,null,'none',null,null,'ENG',1,null,null,'test@test.com','Manager',(select IdePro from AwePro where Nam = 'administrator'),(select IdeThm from AweThm where Nam = 'sunset'));
-- Update OpeKey
UPDATE AweKey SET KeyVal = '2' where KeyNam = 'OpeKey';

-- Insert AweMod
delete AweMod;
insert into AweMod (IdeMod, Nam, ScrIni, IdeThm, Act) values (916,	'Test',	'Dbs',	4,	1);
insert into AweMod (IdeMod, Nam, ScrIni, IdeThm, Act) values (28,	'Base',	'Sit',	2,	1);

-- Insert AweMod
delete AweDbs;
insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (9	,'awesybase1','AWE SYBASE 1','J','syb','com.sybase.jdbc3.jdbc.SybDriver','awesybase1','xxx','Des','jdbc:sybase:Tds:localhost:5005?ServiceName=awesybase1',	1	);
insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (8	,'awesqs1','AWE SQL SERVER 1','J','sqs','com.microsoft.sqlserver.jdbc.SQLServerDriver','awesqs1','xxx','Des','	jdbc:sqlserver://localhost;databaseName=awesqs1',	1	);
insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (6	,'aweora1','AWE ORACLE 1','J','ora','oracle.jdbc.driver.OracleDriver','aweora1','xxx','Des','jdbc:oracle:thin:@localhost:1521:oracle1',	1	);
insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (7	,'aweora2','AWE ORACLE 2','J','ora','oracle.jdbc.driver.OracleDriver','aweora2','xxx','Des','jdbc:oracle:thin:@localhost:1521:oracle2',	1	);
insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (15  , 'awesqs2','AWE SQL SERVER 2','J','sqs','com.microsoft.sqlserver.jdbc.SQLServerDriver','awesqs2','xxx','Des','	jdbc:sqlserver://localhost;databaseName=awealmsqs05',	1	);
insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (16  , 'awesybase2','AWE SYBASE 2','J','syb','com.sybase.jdbc3.jdbc.SybDriver','awesybase2','xxx','Des','jdbc:sybase:Tds:localhost:5005?ServiceName=awesybase2',	1	);

-- Insert AweSitModDbs
delete AweSitModDbs;
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (2579,	17,	916,	7,	1);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (2580,	10,	916,	6,	1);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (2581,	17,	916,	15,	2);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (2582,	10,	916,	8,	2);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (2583,	17,	916,	16,	3);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (2584,	10,	916,	9,	3);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (75,	10,	28,	6,	1);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (60,	17,	28,	7,	1);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (76,	10,	28,	8,	2);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (78,	17,	28,	15,	2);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (77,	10,	28,	9,	3);
insert into AweSitModDbs (IdeSitModDbs, IdeSit, IdeMod, IdeDbs, Ord) values (79,	17,	28,	16,	3);

-- Insert AweModPro
delete AweModPro;
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (937,	916,	1,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (62,	28,	1,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (65,	28,	2,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (74,	28,	3,	null);

