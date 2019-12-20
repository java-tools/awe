
--------------------------------------------------------
--  DDL for INSERT DATA
--------------------------------------------------------

Delete From AweSitModDbs;
Delete From AweAppPar;
Delete From AweModPro;
Delete From Ope;
Delete From AwePro;
Delete From AweMod;
Delete From AweDbs;
Delete From AweThm;
Delete From AweKey;

-- Insert sequences
Insert into AweKey (KeyNam, KeyVal) values ('OpeKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ThmKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ProKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ModKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('DbsKey', 4);
Insert into AweKey (KeyNam, KeyVal) values ('SitKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ModOpeKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ModProKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('SitModDbsKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ScrOpeKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ScrProKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('EmlSrvKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('AppParKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('JmsKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ScrCnfKey', 1);
Insert into AweKey (KeyNam, KeyVal) values ('ScrResKey', 1);

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
Insert into AweAppPar (IdeAweAppPar,ParNam,ParVal,Cat,Des,Act) values ('18','DjrHdgPag','0','2','Remove heading and pagination when exporting to excel','1');
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
Insert into ope (IdeOpe, l1_nom, l1_pas, l1_con, l1_dev, l1_act, l1_trt, l1_uti, l1_opr, l1_dat, imp_nom, dat_mod, l1_psd, l1_lan, l1_sgn, PcPrn, EmlSrv, EmlAdr, OpeNam, IdePro, IdeThm) values (1,'test','5e52fee47e6b070565f74372468cdc699de89107',0,null,1,null,0,null,null,'none',null,null,'ENG',1,null,null,'test@test.com','Manager',(select IdePro from AwePro where Nam = 'administrator'),(select IdeThm from AweThm where Nam = 'sunset'));

-- Update OpeKey
UPDATE AweKey SET KeyVal = '2' where KeyNam = 'OpeKey';

-- Insert AweMod
Insert into AweMod (IdeMod, Nam, ScrIni, IdeThm, Act, Ord) values (916,	'Test',	'Dbs',	4,	1, 2);
Insert into AweMod (IdeMod, Nam, ScrIni, IdeThm, Act, Ord) values (28,	'Base',	'Sit',	2,	1, 1);

-- Insert AweDbs
Insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (1	,'awedb1','AWE DB 1','J','hsql','${spring.datasource.driver-class-name}','${spring.datasource.username}','IKvBjXjD26bm2TY7m7DorU36kI6AuXY1DFKxB3C7LE8=','Des','${spring.datasource.url}',1);
Insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (2	,'awedb2','AWE DB 2','J','hsql','${spring.datasource.driver-class-name}','${spring.datasource.username}','IKvBjXjD26bm2TY7m7DorU36kI6AuXY1DFKxB3C7LE8=','Des','${spring.datasource.url}',1);
Insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (3	,'awedb3','AWE DB 3','J','hsql','${spring.datasource.driver-class-name}','${spring.datasource.username}','IKvBjXjD26bm2TY7m7DorU36kI6AuXY1DFKxB3C7LE8=','Des','${spring.datasource.url}',1);

-- Insert AweSitModDbs

-- Insert AweModPro
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (937,	916,	1,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (62,	28,	1,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (65,	28,	2,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (74,	28,	3,	null);

-- Scheduler sequences
Insert into AweKey (KeyNam, KeyVal) values ('SchTskSrv', (select coalesce(max(Ide),0) + 1 from AweSchSrv));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskCal', (select coalesce(max(Ide),0) + 1 from AweSchCal));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskCalDat', (select coalesce(max(Ide),0) + 1 from AweSchCalDat));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskKey', (select coalesce(max(Ide),0) + 1 from AweSchTsk));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskLch', (select coalesce(max(Ide),0) + 1 from AweSchTskLch));
Insert into AweKey (KeyNam, KeyVal) values ('SchTskPar', (select coalesce(max(Ide),0) + 1 from AweSchTskPar));

