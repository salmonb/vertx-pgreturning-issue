package dev.salmonb.vertx.issue.pgreturning;

import io.vertx.core.Launcher;

/**
 * @author Bruno Salmon
 */
public class Main {

    public static void main(String[] args) {
        Launcher.main(new String[] { "run", PgReturningIssueVerticle.class.getName() });
    }

}