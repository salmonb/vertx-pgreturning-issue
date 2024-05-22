/**
 * @author Bruno Salmon
 */

module vertx.pg.returning.issue {

    requires io.vertx.client.sql;
    requires io.vertx.client.sql.pg;
    requires io.vertx.core;
    requires java.sql;

    exports dev.salmonb.vertx.issue.pgreturning;

}