package dev.salmonb.vertx.issue.pgreturning;

import io.vertx.core.AbstractVerticle;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class PgReturningIssueVerticle extends AbstractVerticle {

    @Override
    public void start() {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("pg_db")
                .setUser("pg_user")
                .setPassword("pg_password");

        // Pool Options
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        // Create the pool from the data object
        Pool pool = PgBuilder.pool()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .using(getVertx())
                .build();

        List<Tuple> batch = List.of(Tuple.of("Julien", "Viet"), Tuple.of("Emad", "Alblueshi"));

        pool.withConnection(connection -> connection
                        .preparedQuery("insert into person (first_name, last_name) values ($1, $2) returning id")
                        .executeBatch(batch))
                .onFailure(System.err::println)
                .onSuccess(rs -> {
                    if (rs.size() == batch.size())
                        System.out.println("Correct RowSet size");
                    else
                        System.out.println("Incorrect RowSet size: " + rs.size() + " instead of " + batch.size());
                    for (Row row : rs) {
                        System.out.println("Returned id = " + row.getValue(0));
                    }
                });
    }
}
