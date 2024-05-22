# vertx-pgreturning-issue

The [Main](src/main/java/dev/salmonb/vertx/issue/pgreturning/Main.java) class starts [PgReturningIssueVerticle](src/main/java/dev/salmonb/vertx/issue/pgreturning/PgReturningIssueVerticle.java) which runs `insert into person (first_name, last_name) values ($1, $2) returning id` with a batch of 2 Tuples, therefore 2 ids are expected, but only 1 is returned 🤷

## How to reproduce

Create a local Postgres database with the following connection details (or update [PgReturningIssueVerticle](src/main/java/dev/salmonb/vertx/issue/pgreturning/PgReturningIssueVerticle.java) to run with different details)

```java
PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(5432)
        .setHost("localhost")
        .setDatabase("pg_db")
        .setUser("pg_user")
        .setPassword("pg_password");
```
Create a `person` table as follows:
```sql
CREATE TABLE person (
    id                   INT GENERATED ALWAYS AS IDENTITY,
    first_name           VARCHAR(45),
    last_name            VARCHAR(45)
);
```
When I run the [Main](src/main/java/dev/salmonb/vertx/issue/pgreturning/Main.java) class, I'm getting:

```
Incorrect RowSet size: 1 instead of 2
```

Although 2 rows have been created:

```sql
select * from person;
```
| id | first\_name | last\_name |
| :--- | :--- | :--- |
| 1 | Julien | Viet |
| 2 | Emad | Alblueshi |

The Vert.x RowSet returns only 1 row.

Is it a Vert.x bug?