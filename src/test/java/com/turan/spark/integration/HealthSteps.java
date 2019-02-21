package com.turan.spark.integration;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turan.spark.ApplicationMain;
import cucumber.api.java8.En;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HealthSteps implements En {

    private int port;

    public HealthSteps() {

        Before(() -> {
            port = ApplicationMain.startServer();
        });

        After(() -> {
            ApplicationMain.stopServer();
            // This is terrible.
            // Spark has a problem where it doesn't clear the running state flag until shutdown has completed, and
            // has no blocking stop method.
            // See https://github.com/perwendel/spark/issues/705
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Then("^the health check endpoint should return status (\\d+)$", (Integer statusCode) -> {
            HttpResponse<String> health;
            try {
                health = Unirest.get("http://localhost:" + port + "/health").asString();
            } catch (UnirestException e) {
                health = null;
            }
            assertNotNull(health);
            assertEquals(statusCode.intValue(), health.getStatus());
            assertNotNull(health.getBody());
        });

    }
}
