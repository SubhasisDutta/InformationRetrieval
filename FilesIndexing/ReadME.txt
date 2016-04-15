Files and Indexing Project
==========================
To Run :
create a folder db in the folder containing the Simple_Database.jar
java -cp .\Simple_Database.jar com.subhasis.devisbase.resource.Main


Features:

1. Show prompt for command input.

2. Commands Supported:
	a. version;                                 Show the program version.
	b. help;                                    Show this help information.
	c. show schemas;                            Displays all schemas defined in your database.
	d. use <schema_name>;                       Chooses a schema as default.
	e. show tables;                             Displays all tables in the currectly chosen schema.
	f. create schema <schema_name>;             Creates a new schema to hold tables.
	g. create table <table_name>(...);          Creates a new table schema, i.e. a new empty table.
	h. insert into table <table_name> ...;      Inserts a row/record into a table. INSERT INTO table_name VALUES (values_list);
	i. delete from <table_name> ... ;           Deletes one or more rows/records from a table.
	j. drop table <table_name>;                 Remove a table schema, and all of its contained data.
	k. select ... from <table_name> where ...;  Query a table.	SELECT * FROM <table_name> WHERE field_name [!=|=|>|>=|<|<=] value;	
	l. exit/quit;                               Exit the program.
	
	
Sample Commands 
===============

1. version; 

DavisBaseLite v1.0

2. help;

********************************************************************************

 version;                                 Show the program version.
 help;                                    Show this help information
 show schemas;                            Displays all schemas defined in your database.
 use <schema_name>;                       Chooses a schema as default.
 show tables;                             Displays all tables in the currectly chosen schema.
 create schema <schema_name>;             Creates a new schema to hold tables.
 create table <table_name>(...);          Creates a new table schema, i.e. a new empty table.
 insert into table <table_name> ...;      Inserts a row/record into a table.
INSERT INTO table_name VALUES (values_list);
 delete from <table_name> ... ;           Deletes one or more rows/records from a table.(Not Supported.)
 drop table <table_name>;                 Remove a table schema, and all of its contained data.
 select ... from <table_name> where ...;  Query a table.
 SELECT * FROM <table_name> WHERE field_name [!=|=|>|>=|<|<=] value;
 exit/quit;                               Exit the program


********************************************************************************

3. show schemas;

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	Database	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  information_schema  |
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 4.379812ms

4. create schema amusement_park;

Created Schema with name amusement_park sucessfully.
Executed in 9.477619ms

4. show schemas;

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	Database	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  information_schema  | 
|  amusement_park  |   
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

5. show tables;

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	Tables_in_information_schema	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  SCHEMATA  |  
|  TABLES  |  
|  COLUMNS  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 5.712696ms

6. SELECT * FROM SCHEMATA;

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	SCHEMA_NAME	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  information_schema  |  
|  amusement_park  |
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 13.402094ms

7. use amusement_park;

Database changed to amusement_park sucessfully.
Executed in 5.002167ms

8. SHOW TABLES;

Empty Set
Executed in 4.161474ms

8. CREATE TABLE park_names1 (
park_id int primary key,
park_name varchar(50) not null
);

Table added to schema amusement_park
Executed in 22.899773ms

9. show tables;

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	Tables_in_amusement_park	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  park_names  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 6.588846ms

10. use information_schema;

Database changed to information_schema sucessfully.
Executed in 6.839841ms

11. select * from tables;

Tables_in information_schema tables
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	TABLE_SCHEMA	TABLE_NAME	TABLE_ROWS	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  information_schema  |  SCHEMATA  |  1  |  
|  information_schema  |  TABLES  |  3  |  
|  information_schema  |  COLUMNS  |  7  |  
|  amusement_park  |  park_names  |  0  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 20.308183ms

12. select * from columns;
Tables_in information_schema columns
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	TABLE_SCHEMA	TABLE_NAME	COLUMN_NAME	ORDINAL_POSITION	COLUMN_TYPE	IS_NULLABLE	COLUMN_KEY	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  information_schema  |  SCHEMATA  |  SCHEMA_NAME  |  1  |  varchar(64)  |  NO  |  |
|  information_schema  |  TABLES  |  TABLE_SCHEMA  |  1  |  varchar(64)  |  NO  |  |
|  information_schema  |  TABLES  |  TABLE_NAME  |  2  |  varchar(64)  |  NO  |  |
|  information_schema  |  TABLES  |  TABLE_ROWS  |  3  |  long int  |  NO  |  |
|  information_schema  |  COLUMNS  |  TABLE_SCHEMA  |  1  |  varchar(64)  |  NO  |  |
|  information_schema  |  COLUMNS  |  TABLE_NAME  |  2  |  varchar(64)  |  NO  |  |
|  information_schema  |  COLUMNS  |  COLUMN_NAME  |  3  |  varchar(64)  |  NO  |  |
|  information_schema  |  COLUMNS  |  ORDINAL_POSITION  |  4  |  int  |  NO  |  |
|  information_schema  |  COLUMNS  |  COLUMN_TYPE  |  5  |  varchar(64)  |  NO  |  |
|  information_schema  |  COLUMNS  |  IS_NULLABLE  |  6  |  varchar(3)  |  NO  |  |
|  information_schema  |  COLUMNS  |  COLUMN_KEY  |  7  |  varchar(3)  |  NO  |  |
|  amusement_park  |  park_names  |  park_id  |  1  |  int  |  NO  |  PRI  |  
|  amusement_park  |  park_names  |  park_name  |  2  |  varchar(50)  |  NO  |  |
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 20.744857ms

13. use amusement_park;

Database changed to amusement_park sucessfully.
Executed in 4.699853ms

14. INSERT INTO TABLE park_names VALUES (1,"Adventure Kingdom");

1 row inserted in 69.570804ms

15. INSERT INTO TABLE park_names VALUES (2,Aquatica);

1 row inserted in 22.364193ms

16. SELECT * FROM park_names;

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	park_id	park_name	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  1  |  Adventure Kingdom  |  
|  2  |  Aquatica  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 14.92999ms

17. INSERT INTO TABLE park_names VALUES (1,"Adventure Kingdom");

Cannot INSERT. Duplicate value for column park_id

18. INSERT INTO TABLE park_names VALUES (3,"");

Cannot INSERT. Value for column park_name cannot be NULL.

19. DROP TABLE AMUSEMENT_TYPES;

Table with name amusement_types does not exiests in schema park_names

20. DROP TABLE park_names;

Table dropped from schema park_names
Executed in 88.032915ms

21. SHOW TABLES;

Empty Set
Executed in 3.997254ms

22. CREATE TABLE parks (
id int primary key,
global_id long primary key,
global_code varchar(10) primary key,
name varchar(50) not null,
address varchar(200),
state char(3),
pin char(5) not null,
revenue_YOY double,
entree_fee float not null,
total_visits long,
avg_daily_visits float,
is_open byte,
is_in_america byte not null,
open_date date not null,
close_date date,
openning_time datetime not null,
closing_time datetime,
days_open_in_week short not null,
days_open_in_month short,
days_open_in_year int
);

Table added to schema amusement_park
Executed in 37.306313ms

22. show Tables;

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	Tables_in_amusement_park	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  parks  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 7.694995ms

23. use information_schema;

24. select * from columns;

Tables_in information_schema columns
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	TABLE_SCHEMA	TABLE_NAME	COLUMN_NAME	ORDINAL_POSITION	COLUMN_TYPE	IS_NULLABLE	COLUMN_KEY	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  information_schema  |  SCHEMATA  |  SCHEMA_NAME  |  1  |  varchar(64)  |  NO  |    |  
|  information_schema  |  TABLES  |  TABLE_SCHEMA  |  1  |  varchar(64)  |  NO  |    |  
|  information_schema  |  TABLES  |  TABLE_NAME  |  2  |  varchar(64)  |  NO  |    |  
|  information_schema  |  TABLES  |  TABLE_ROWS  |  3  |  long int  |  NO  |    |  
|  information_schema  |  COLUMNS  |  TABLE_SCHEMA  |  1  |  varchar(64)  |  NO  |    |  
|  information_schema  |  COLUMNS  |  TABLE_NAME  |  2  |  varchar(64)  |  NO  |    |  
|  information_schema  |  COLUMNS  |  COLUMN_NAME  |  3  |  varchar(64)  |  NO  |    |  
|  information_schema  |  COLUMNS  |  ORDINAL_POSITION  |  4  |  int  |  NO  |    |  
|  information_schema  |  COLUMNS  |  COLUMN_TYPE  |  5  |  varchar(64)  |  NO  |    |  
|  information_schema  |  COLUMNS  |  IS_NULLABLE  |  6  |  varchar(3)  |  NO  |    |  
|  information_schema  |  COLUMNS  |  COLUMN_KEY  |  7  |  varchar(3)  |  NO  |    |  
|  amusement_park  |  parks  |  id  |  1  |  int  |  NO  |  PRI  |  
|  amusement_park  |  parks  |  global_id  |  2  |  long  |  NO  |  PRI  |  
|  amusement_park  |  parks  |  global_code  |  3  |  varchar(10)  |  NO  |  PRI  |  
|  amusement_park  |  parks  |  name  |  4  |  varchar(50)  |  NO  |    |  
|  amusement_park  |  parks  |  address  |  5  |  varchar(200)  |  YES  |    |  
|  amusement_park  |  parks  |  state  |  6  |  char(3)  |  YES  |    |  
|  amusement_park  |  parks  |  pin  |  7  |  char(5)  |  NO  |    |  
|  amusement_park  |  parks  |  revenue_yoy  |  8  |  double  |  YES  |    |  
|  amusement_park  |  parks  |  entree_fee  |  9  |  float  |  NO  |    |  
|  amusement_park  |  parks  |  total_visits  |  10  |  long  |  YES  |    |  
|  amusement_park  |  parks  |  avg_daily_visits  |  11  |  float  |  YES  |    |  
|  amusement_park  |  parks  |  is_open  |  12  |  byte  |  YES  |    |  
|  amusement_park  |  parks  |  is_in_america  |  13  |  byte  |  NO  |    |  
|  amusement_park  |  parks  |  open_date  |  14  |  date  |  NO  |    |  
|  amusement_park  |  parks  |  close_date  |  15  |  date  |  YES  |    |  
|  amusement_park  |  parks  |  openning_time  |  16  |  datetime  |  NO  |    |  
|  amusement_park  |  parks  |  closing_time  |  17  |  datetime  |  YES  |    |  
|  amusement_park  |  parks  |  days_open_in_week  |  18  |  short  |  NO  |    |  
|  amusement_park  |  parks  |  days_open_in_month  |  19  |  short  |  YES  |    |  
|  amusement_park  |  parks  |  days_open_in_year  |  20  |  int  |  YES  |    |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 36.168439ms

use amusement_park;

25. davisql> INSERT INTO TABLE parks VALUES (1,532123653,ASD23DX99,"Adventure Kingdom","345 Park Street",TX,75267,
2345678.50,150.00,900000002,1356.23,1,1,'2016-03-23',"",'0000-00-00_10:00:00','0000-00-00_18:00:00',7,30,360);

1 row inserted in 63.982205ms

davisql> select * from parks;

Tables_in amusement_park parks
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	id	global_id	global_code	name	address	state	pin	revenue_yoy	entree_fee	total_visits	avg_daily_visits	is_open	is_in_america	open_date	close_date	openning_time	closing_time	days_open_in_week	days_open_in_month	days_open_in_year	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  1  |  532123653  |  ASD23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  2345678.5  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  0002-11-30_06:00:00  |  7  |  30  |  360  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 36.843979ms

davisql> INSERT INTO TABLE parks VALUES (1,532123653,ASD23DX99,"Adventure Kingdom","345 Park Street",TX,75267,
2345678.50,150.00,900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,0000-00-00_18:00:00,7,30,360);

Cannot INSERT. Duplicate value for column id

davisql> INSERT INTO TABLE parks VALUES (2,532123653,ASD23DX99,"Adventure Kingdom","345 Park Street",TX,75267,
2345678.50,150.00,900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);

Cannot INSERT. Duplicate value for column global_id

davisql> INSERT INTO TABLE parks VALUES (2,533223653,ASD23DX99,"Adventure Kingdom","345 Park Street",TX,75267,
2345678.50,150.00,900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,0000-00-00_18:00:00,7,30,360);

Cannot INSERT. Duplicate value for column global_code

davisql> INSERT INTO TABLE parks VALUES (2,533223653,PUY23DX99,"Adventure Kingdom","345 Park Street",TX,75267,
"",150.00,900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);

1 row inserted in 62.465506ms

26. select * from parks;

davisql> select * from parks;
Tables_in amusement_park parks
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	id	global_id	global_code	name	address	state	pin	revenue_yoy	entree_fee	total_visits	avg_daily_visits	is_open	is_in_america	open_date	close_date	openning_time	closing_time	days_open_in_week	days_open_in_month	days_open_in_year	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  2  |  533223653  |  PUY23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  NULL  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
|  1  |  532123653  |  ASD23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  2345678.5  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  0002-11-30_06:00:00  |  7  |  30  |  360  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 22.049284ms

davisql> select * from parks where id = 2;
Tables_in amusement_park parks
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	id	global_id	global_code	name	address	state	pin	revenue_yoy	entree_fee	total_visits	avg_daily_visits	is_open	is_in_america	open_date	close_date	openning_time	closing_time	days_open_in_week	days_open_in_month	days_open_in_year	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  2  |  533223653  |  PUY23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  NULL  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 34.802897ms

27.  INSERT INTO TABLE parks VALUES (3,533227653,"","Adventure Kingdom","345 Park Street",TX,75267,
"",150.00,900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);
global_code is a PRIMARY KEY. It cannot be null.
Cannot INSERT. Duplicate value for column global_code
davisql> INSERT INTO TABLE parks VALUES (3,533227653,PUY23DX99,"Adventure Kingdom","345 Park Street",TX,75267,
"",150.00,900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);
Cannot INSERT. Duplicate value for column global_code
davisql> INSERT INTO TABLE parks VALUES (3,533227653,AUY23DX99,"Adventure Kingdom","345 Park Street",TX,75267,
"",150.00,900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);
1 row inserted in 85.993232ms

28. INSERT INTO TABLE parks VALUES (4,53227653,AUY33DX99,"Water Kingdom","3145 South Avn. Street",WA,23456,
"","",900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);
Cannot INSERT. Value for column entree_fee cannot be NULL.
davisql> INSERT INTO TABLE parks VALUES (4,53227653,AUY33DX99,"Water Kingdom","3145 South Avn. Street",WA,23456,
"",100.00,900000002,1356.23,1,1,2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);
1 row inserted in 63.189098ms

29. INSERT INTO TABLE parks VALUES (5,53927653,AUY33WX99,'Silver Space',"3145 South Avn. Street",CA,12456,
"",100.00,900000002,1356.23,0,"",2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);
Cannot INSERT. Value for column is_in_america cannot be NULL.
davisql> INSERT INTO TABLE parks VALUES (5,53927653,AUY33WX99,'Silver Space',"3145 South Avn. Street God knows where",XX,12456,
"",100.00,900000002,1356.23,"",0,2016-03-23,"",0000-00-00_10:00:00,"",7,30,360);
1 row inserted in 58.625005ms
davisql> select * from parks;
Tables_in amusement_park parks
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	id	global_id	global_code	name	address	state	pin	revenue_yoy	entree_fee	total_visits	avg_daily_visits	is_open	is_in_america	open_date	close_date	openning_time	closing_time	days_open_in_week	days_open_in_month	days_open_in_year	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  2  |  533223653  |  PUY23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  NULL  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
|  1  |  532123653  |  ASD23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  2345678.5  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  0002-11-30_06:00:00  |  7  |  30  |  360  |  
|  3  |  533227653  |  AUY23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  NULL  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
|  4  |  53227653  |  AUY33DX99  |  Water Kingdom  |  3145 South Avn. Street  |  WA  |  23456  |  NULL  |  100.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
|  5  |  53927653  |  AUY33WX99  |  Silver Space  |  3145 South Avn. Street God knows where  |  XX  |  12456  |  NULL  |  100.0  |  900000002  |  1356.23  |  NULL  |  0  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 29.761074ms
davisql> select * from parks where id = 5; 
Tables_in amusement_park parks
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	id	global_id	global_code	name	address	state	pin	revenue_yoy	entree_fee	total_visits	avg_daily_visits	is_open	is_in_america	open_date	close_date	openning_time	closing_time	days_open_in_week	days_open_in_month	days_open_in_year	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  5  |  53927653  |  AUY33WX99  |  Silver Space  |  3145 South Avn. Street God knows where  |  XX  |  12456  |  NULL  |  100.0  |  900000002  |  1356.23  |  NULL  |  0  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 32.335404ms

30. INSERT INTO TABLE parks VALUES (6,63927653,ARY33WX99,'Space Park'," ","",00000,
"",0.00,"","","",0,2017-03-23,"",0000-00-00_10:00:00,0,"","","");

INSERT INTO TABLE parks VALUES (6,63927653,ARY33WX99,'Space Park'," ","","",
"",0.00,"","","",0,"","","","","","","");
Cannot INSERT. Value for column pin cannot be NULL.
davisql> INSERT INTO TABLE parks VALUES (6,63927653,ARY33WX99,'Space Park'," ","",00000,
"",0.00,"","","",0,"","","","","","","");
Cannot INSERT. Value for column open_date cannot be NULL.
davisql> INSERT INTO TABLE parks VALUES (6,63927653,ARY33WX99,'Space Park'," ","",00000,
"",0.00,"","","",0,2017-03-23,"","","","","","");
Cannot INSERT. Value for column openning_time cannot be NULL.
davisql> INSERT INTO TABLE parks VALUES (6,63927653,ARY33WX99,'Space Park'," ","",00000,
"",0.00,"","","",0,2017-03-23,"",0000-00-00_10:00:00,"","","","");
Cannot INSERT. Value for column days_open_in_week cannot be NULL.
davisql> INSERT INTO TABLE parks VALUES (6,63927653,ARY33WX99,'Space Park'," ","",00000,
"",0.00,"","","",0,2017-03-23,"",0000-00-00_10:00:00,"","","","");
Date Time format is not correct
davisql> INSERT INTO TABLE parks VALUES (6,63927653,ARY33WX99,'Space Park'," ","",00000,
"",0.00,"","","",0,2017-03-23,"",0000-00-00_10:00:00,"","","","");
Cannot INSERT. Value for column days_open_in_week cannot be NULL.
davisql> INSERT INTO TABLE parks VALUES (6,63927653,ARY33WX99,'Space Park'," ","",00000,
"",0.00,"","","",0,2017-03-23,"",0000-00-00_10:00:00,"",0,"","");
1 row inserted in 68.929788ms


INSERT INTO TABLE parks VALUES (7,6327653,ARY3WX99,'Space Park1'," ","",00000,
"",0.00,"","","",0,2017-03-23,"",0000-00-00_10:00:00,"",0,"",34);


31. select * from parks where global_id > 55555555;
Tables_in amusement_park parks
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	id	global_id	global_code	name	address	state	pin	revenue_yoy	entree_fee	total_visits	avg_daily_visits	is_open	is_in_america	open_date	close_date	openning_time	closing_time	days_open_in_week	days_open_in_month	days_open_in_year	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  6  |  63927653  |  ARY33WX99  |  Space Park  |     |     |  00000  |  NULL  |  0.0  |  NULL  |  NULL  |  NULL  |  0  |  2017-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  0  |  NULL  |  NULL  |  
|  1  |  532123653  |  ASD23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  2345678.5  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  0002-11-30_06:00:00  |  7  |  30  |  360  |  
|  2  |  533223653  |  PUY23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  NULL  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
|  3  |  533227653  |  AUY23DX99  |  Adventure Kingdom  |  345 Park Street  |  TX  |  75267  |  NULL  |  150.0  |  900000002  |  1356.23  |  1  |  1  |  2016-03-23  |  NULL  |  0002-11-30_10:00:00  |  NULL  |  7  |  30  |  360  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 42.221238ms


1. davisql> CREATE TABLE Zoo (
Animal_ID INT PRIMARY KEY,
Name VARCHAR(20),
Sector SHORT INT
);
Table added to schema amusement_park
Executed in 37.469599ms
davisql> INSERT INTO TABLE parks VALUES (57,giraffe,9);
Invalid Syntax.Number of data poins for parks dont match up.
davisql> INSERT INTO TABLE Zoo VALUES (57,giraffe,9);
1 row inserted in 32.529015ms
davisql> INSERT INTO TABLE Zoo VALUES (12,elephant,5);
1 row inserted in 32.429643ms
davisql> INSERT INTO TABLE Zoo VALUES (23,lion,4);
1 row inserted in 31.948648ms
davisql> INSERT INTO TABLE Zoo VALUES (17,hippo,5);
1 row inserted in 32.816865ms
davisql> select * from zoo where name > f;
Tables_in amusement_park zoo
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  	animal_id	name	sector	|
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|  57  |  giraffe  |  9  |  
|  17  |  hippo  |  5  |  
|  23  |  lion  |  4  |  
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Executed in 31.506841ms

