package dev.salmonb.vertx.issue.pgreturning;

import io.vertx.core.AbstractVerticle;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.*;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class PgReturningIssueVerticle extends AbstractVerticle {

    @Override
    public void start() {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(6534)
                .setHost("localhost")
                .setDatabase("pg_db")
                .setUser("postgres")
                .setPassword("ultraboa");

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
                    int totalRowCount = 0;
                    for (RowSet<Row> rows = rs; rows != null; rows = rows.next()) {
                        totalRowCount += rows.size();
                        for (Row row : rows) {
                            Object personId = row.getValue(0);
                            System.out.println("generated key: " + personId);
                        }
                    }
                    if (totalRowCount == batch.size())
                        System.out.println("Correct RowSet size");
                    else
                        System.out.println("Incorrect RowSet size: " + totalRowCount + " instead of " + batch.size());
                });
    }
}
