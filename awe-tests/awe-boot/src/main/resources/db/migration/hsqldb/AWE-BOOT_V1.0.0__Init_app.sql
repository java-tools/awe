-- Profiles
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('2','GNR','General','1',null,'general','1');
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('3','OPE','Operator','1',null,'operator','1');
Insert into AwePro (IdePro, Acr, Nam, IdeThm, ScrIni, Res, Act) values ('4','TST','Test','1',null,'test','1');

-- Update ProKey
UPDATE AweKey SET KeyVal = '5' where KeyNam = 'ProKey';

-- Insert AweMod
Insert into AweMod (IdeMod, Nam, ScrIni, IdeThm, Act, Ord) values (1,	'Test',	'Dbs',	4,	1, 2);
Insert into AweMod (IdeMod, Nam, ScrIni, IdeThm, Act, Ord) values (2,	'Base',	'Sit',	2,	1, 1);

-- Update ModKey
UPDATE AweKey SET KeyVal = '3' where KeyNam = 'ModKey';

-- Insert AweDbs
Insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (1	,'awedb1','AWE DB 1','J','hsql','org.hsqldb.jdbc.JDBCDriver','sa', null,'Des','jdbc:hsqldb:file:awe-tests/awe-boot/target/db/awe-boot',1);
Insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (2	,'awedb2','AWE DB 2','J','hsql','org.hsqldb.jdbc.JDBCDriver','sa', null,'Des','jdbc:hsqldb:file:awe-tests/awe-boot/target/db/awe-boot',1);
Insert into AweDbs (IdeDbs, Als, Des, Dct, Dbt, Drv, DbsUsr, DbsPwd, Typ, Dbc, Act) values (3	,'awedb3','AWE DB 3','J','hsql','org.hsqldb.jdbc.JDBCDriver','sa', null,'Des','jdbc:hsqldb:file:awe-tests/awe-boot/target/db/awe-boot',1);

-- Update DbsKey
UPDATE AweKey SET KeyVal = '4' where KeyNam = 'DbsKey';

-- Insert AweModPro
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (1, 1, 1, null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (2, 2, 1, null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (3, 2, 2, null);
insert into AweModPro (IdeModPro, IdeMod, IdePro, Ord) values (4, 2, 3, null);

-- Update ModProKey
UPDATE AweKey SET KeyVal = '5' where KeyNam = 'ModProKey';