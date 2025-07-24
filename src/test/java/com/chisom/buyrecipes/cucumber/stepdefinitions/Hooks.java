package com.chisom.buyrecipes.cucumber.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
public class Hooks {

    private final DataSource dataSource;

    public Hooks(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Seeds the database with initial data by executing an SQL script.
     * <p>
     * This method should be executed before scenarios annotated with the @DatabaseSeeding tag.
     * It runs the INSERT_seed_data.sql script located in the scripts folder
     * to populate the database with predefined seed data. This allows test scenarios
     * that depend on specific database states to execute against a pre configured dataset.
     * <p>
     * Logs an error if the seeding process fails due to an SQL exception.
     */
    @Before("@DatabaseSeeding")
    public void seedDatabase() {
        logger.info("🌱 Running seeding script.....");
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("scripts/INSERT_seed_data.sql"));
            conn.commit(); // Explicit commit
            logger.info("✅ Database seeding committed");
        } catch (SQLException e) {
            logger.error("❌ Database seeding failed: {}", e.getMessage());
        }
    }

    /**
     * Clears the content of the connected database.
     * <p>
     * This method is executed after scenarios annotated with the @DatabaseCleanUp tag.
     * It ensures that the database is reset to an initial state after the execution
     * of specific test scenarios, maintaining data integrity and isolation between tests.
     */
    @After("@DatabaseCleanUp")
    public void clearDatabase() {
        logger.info("🧹 Running cleanup script....");
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("scripts/DELETE_cleanup_database.sql"));
            conn.commit(); // Explicit commit
            logger.info("✅ Database cleanup completed");
        } catch (SQLException e) {
            logger.error("❌ Database clean up failed: {}", e.getMessage());
        }
    }

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }
}
