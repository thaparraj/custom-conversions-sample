# custom conversions sample
# setting up casandra locally on Mac
1. download docker desktop

2. run commands:

    docker run --name mycass -p 9042:9042 -d cassandra:3.11.5
    docker exec -it mycass cqlsh 
    cqlsh> CREATE KEYSPACE accountdb WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
    cqlsh> create table account_tbl (name text, status boolean, createts timestamp, userid text, primary key (userid));
    cqlsh> insert into account_tbl (name , status , createts , userid) values ('john', true, toTimeStamp(now()), 'idjohn');   
    cqlsh> select * from account_tbl;
3. start app
4. run following curl command:
   curl --location 'http://localhost:8075/Users/idjohn'
