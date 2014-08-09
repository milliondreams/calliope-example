DROP KEYSPACE cql3_test;

CREATE KEYSPACE cql3_test WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '3'
};

USE cql3_test;

CREATE TABLE emp_read_test (
  deptid int,
  empid int,
  first_name text,
  last_name text,
  PRIMARY KEY (deptid, empid)
);

CREATE TABLE emp_write_test (
  deptid int,
  empid int,
  first_name text,
  last_name text,
  PRIMARY KEY (deptid, empid)
);
