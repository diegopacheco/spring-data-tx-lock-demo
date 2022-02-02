# Database Locking - Issues with Propagation.REQUIRES_NEW

Example Repository to learn about the issue of database deadlocks.

For more information read [the blogpost](https://medium.com/@paul.klingelhuber/e6430d8a8d30).

Run DB Container
```
docker run --detach --name dummy-mariadb --env MARIADB_USER=testuser --env MARIADB_PASSWORD=ThePassword --env MARIADB_ROOT_PASSWORD=root --env MARIADB_DATABASE=test -p3307:3306  mariadb:latest
```

Run the application.

Reproduce issue:

```
curl -XPOST http://localhost:8080/signup
```

Try fixed version:
```
curl -XPOST http://localhost:8080/signupFixed
```

Simplified thread-dump stacktrace taken while the issue is currently happening:

```
"http-nio-8080-exec-2" #43 [...]]
   java.lang.Thread.State: RUNNABLE
        at java.net.SocketInputStream.socketRead0(java.base@12.0.1/Native Method)
[...]
        - locked <0x00000006057dbf78> (a org.mariadb.jdbc.internal.io.input.ReadAheadBufferedStream)
        at org.mariadb.jdbc.internal.io.input.StandardPacketInputStream.getPacketArray(StandardPacketInputStream.java:247)
[...]
        at com.zaxxer.hikari.pool.ProxyPreparedStatement.executeUpdate(ProxyPreparedStatement.java:61)
[...]
        at org.springframework.data.jdbc.core.JdbcAggregateTemplate.save(JdbcAggregateTemplate.java:150)
[...]
        at at.paukl.springDataPlay.UserLogService.logUserAction(UserLogService.kt:20)
[...]
        at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:388)
[...]
        at at.paukl.springDataPlay.UserService.signup(UserService.kt:28)
[...]
        at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:388)
[...]
        at java.lang.Thread.run(java.base@12.0.1/Thread.java:835)
```


Debugging tools/queries for mariadb:

```
SHOW ENGINE INNODB STATUS;

SHOW FULL PROCESSLIST;

select * from INFORMATION_SCHEMA.INNODB_LOCKS;
select * from INFORMATION_SCHEMA.INNODB_TRX;

-- taken from https://aws.amazon.com/premiumsupport/knowledge-center/rds-mysql-server-activity/

SELECT r.trx_id              waiting_trx_id,
       r.trx_mysql_thread_id waiting_thread,
       r.trx_query           waiting_query,
       b.trx_id              blocking_trx_id,
       b.trx_mysql_thread_id blocking_thread,
       b.trx_query           blocking_query
FROM information_schema.innodb_lock_waits w
         INNER JOIN information_schema.innodb_trx b ON b.trx_id = w.blocking_trx_id
         INNER JOIN information_schema.innodb_trx r ON r.trx_id = w.requesting_trx_id;
```

