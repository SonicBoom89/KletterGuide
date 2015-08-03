CREATE TABLE CLIMBINGAREAS ( id integer primary key autoincrement, name varchar(100), latitude float, longitude float, ranking float);
CREATE TABLE ROUTES ( id integer primary key autoincrement, climbingarea integer, name varchar(100), ranking float, uiaa varchar(10), foreign key(climbingarea) references CLIMBINGAREAS(id));
