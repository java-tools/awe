
--------------------------------------------------------
--  DDL for INSERT DATA
--------------------------------------------------------
-- Insert sequences
Insert into AweKey (KeyNam, KeyVal) values ('OpeKey', 2);
Insert into AweKey (KeyNam, KeyVal) values ('ThmKey', 19);
Insert into AweKey (KeyNam, KeyVal) values ('ProKey', 5);
Insert into AweKey (KeyNam, KeyVal) values ('ModKey', 3);
Insert into AweKey (KeyNam, KeyVal) values ('DbsKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('SitKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ModOpeKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ModProKey', 5);
Insert into AweKey (KeyNam, KeyVal) values ('SitModDbsKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ScrOpeKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('ScrProKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('EmlSrvKey', 0);
Insert into AweKey (KeyNam, KeyVal) values ('AppParKey', 20);
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

-- Insert default profiles
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('1','ADM','administrator','1',null,'administrator','1');
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('2','GNR','general','1',null,'general','1');
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('3','OPE','operator','1',null,'operator','1');
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('4','TST','test','1',null,'test','1');

-- Insert default user
Insert into OPE (IdeOpe, l1_nom, l1_pas, l1_con, l1_dev, l1_act, l1_trt, l1_uti, l1_opr, l1_dat, imp_nom, dat_mod, l1_psd, l1_lan, l1_sgn, PcPrn, EmlSrv, EmlAdr, OpeNam, IdePro, IdeThm) values (1,'test','5e52fee47e6b070565f74372468cdc699de89107',0,null,1,null,0,null,null,'none',null,null,'ENG',1,null,null,'test@test.com','Manager',(select IdePro from AwePro where Nam = 'administrator'),(select IdeThm from AweThm where Nam = 'sunset'));

-- Insert AweMod
delete AweMod;
insert into AweMod (IdeMod, Nam, ScrIni, IdeThm, Act) values (1,	'Base',	'Sit',	2,	1);
insert into AweMod (IdeMod, Nam, ScrIni, IdeThm, Act) values (2,	'Test',	'Dbs',	4,	1);

-- Insert AweMod
delete AweDbs;

-- Insert AweSitModDbs
delete AweSitModDbs;

-- Insert AweModPro
delete AweModPro;
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (1,	2,	1,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (2,	1,	1,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (3,	1,	2,	null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (4,	1,	3,	null);


