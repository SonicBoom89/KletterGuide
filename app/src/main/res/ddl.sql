CREATE TABLE BusStops ( id integer primary key autoincrement, name varchar(100), stationId varchar(100), latitude float, longitude float);
CREATE TABLE Routes ( id integer primary key autoincrement, FromLoctype varchar(20), type varchar(20), ToLoctype varchar(20), FromID varchar(20), ToID varchar(20), Route varchar(200), FROM_NAME varchar(100), FROM_LAT  float, FROM_LNG float, TO_NAME varchar(100), TO_LAT  float, TO_LNG float);
CREATE TABLE Directions ( Route varchar(200), latitude float, longitude float);
CREATE TABLE RecentQueries ( id varchar(50) primary key, LocationType varchar(20), latitude  integer, longitude integer, place varchar(100), name  varchar(100), querydate timestamp DATE DEFAULT (datetime('now','localtime')));
