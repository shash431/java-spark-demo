package com.turan.spark.integration;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turan.spark.ApplicationMain;
import cucumber.api.java8.En;

import static org.junit.jupiter.api.Assertions.*;

public class ClientSteps implements En {

    private int port;

    public ClientSteps() {

        Before(() -> {
            port = ApplicationMain.startServer();
        });

        After(() -> {
            ApplicationMain.stopServer();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Given("^Person table is empty$", () -> {
            HttpResponse<String> deletedResponse;
            try {
                deletedResponse = Unirest.delete("http://localhost:" + port + "/clear").asString();
            } catch (UnirestException e) {
                deletedResponse = null;
            }
            assertNotNull(deletedResponse);
            assertEquals(200, deletedResponse.getStatus());
            assertTrue(deletedResponse.getBody().contains(" client(s) were deleted !"));
        });

        When("I insert client with name (\\w+) and with last name (\\w+) to the clients$",
                (String firstName, String lastName)-> {
            HttpResponse<String> insertResponse;
            String body = "{\"firstName\":\""+firstName+"\",\"lastName\":"+lastName+"}";
            try {
                insertResponse = Unirest.post("http://localhost:" + port + "/client")
                        .body(body)
                        .asString();
            } catch (UnirestException e) {
                insertResponse = null;
            }
            assertNotNull(insertResponse);
            assertEquals(200, insertResponse.getStatus());
        });


        Then("I should list (\\d+) client with the name (\\w+) and last name (\\w+) when I list$",
                (Integer count, String firstName, String lastName)-> {
            HttpResponse<JsonNode> listResponse;
            try {
                listResponse = Unirest.get("http://localhost:" + port + "/client")
                        .asJson();
            } catch (UnirestException e) {
                listResponse = null;
            }
            assertNotNull(listResponse);
            assertEquals(200, listResponse.getStatus());
            assertEquals(listResponse.getBody().getArray().length(), 1);
            assertEquals(listResponse.getBody().getArray().getJSONObject(0).get("firstName").toString(), firstName);
            assertEquals(listResponse.getBody().getArray().getJSONObject(0).get("lastName").toString(), lastName);
        });

        When("^I update the last name of the client number (\\d+) with the name (\\w+) and last name (\\w+) to the new last name (\\w+)$",
                (Integer id,String firstName, String lastName, String newLastName) ->{
            HttpResponse<String> updateResponse;
            String body = "{\"id\":\""+id+"\"firstName\":\""+firstName+"\",\"lastName\":"+lastName+"}";
            try {
                updateResponse = Unirest.put("http://localhost:" + port + "/client")
                        .body(body)
                        .asString();
            } catch (UnirestException e) {
                updateResponse = null;
            }
            assertNotNull(updateResponse);
            assertEquals(200, updateResponse.getStatus());
            assertTrue(updateResponse.getBody().contains("Client record is saved !"));
        });

        Then("I should list (\\d+) client with the name (\\w+) and last name (\\w+) when I list$",
                (Integer count, String firstName, String lastName)-> {
            HttpResponse<JsonNode> listResponse;
            // list items
            try {
                listResponse = Unirest.get("http://localhost:" + port + "/client")
                        .asJson();
            } catch (UnirestException e) {
                listResponse = null;
            }

            assertNotNull(listResponse);
            assertEquals(200, listResponse.getStatus());
            assertEquals(listResponse.getBody().getArray().length(), 1);
            assertEquals(listResponse.getBody().getArray().getJSONObject(0).get("firstName").toString(), firstName);
            assertEquals(listResponse.getBody().getArray().getJSONObject(0).get("lastName").toString(), lastName);
        });

        When("I insert another client with name (\\w+) and with last name (\\w+) to the clients$",
                (String firstName, String lastName)-> {
            HttpResponse<String> insertResponse;
            String body = "{\"firstName\":\""+firstName+"\",\"lastName\":"+lastName+"}";
            try {
                insertResponse = Unirest.post("http://localhost:" + port + "/client")
                        .body(body)
                        .asString();
            } catch (UnirestException e) {
                insertResponse = null;
            }
            assertNotNull(insertResponse);
            assertEquals(200, insertResponse.getStatus());
        });

        Then("^I should list (\\d+) clients when I list with first one being (\\w+) (\\w+) and the second one being (\\w+) (\\w+)$",
                (Integer count, String firstName, String lastName, String firstName2, String lastName2) -> {
            HttpResponse<JsonNode> listResponse;
            // list items
            try {
                listResponse = Unirest.get("http://localhost:" + port + "/client")
                        .asJson();
            } catch (UnirestException e) {
                listResponse = null;
            }

            assertNotNull(listResponse);
            assertEquals(200, listResponse.getStatus());
            assertEquals(listResponse.getBody().getArray().length(), 2);
            assertEquals(listResponse.getBody().getArray().getJSONObject(0).get("firstName").toString(), firstName);
            assertEquals(listResponse.getBody().getArray().getJSONObject(0).get("lastName"), lastName);
            assertEquals(listResponse.getBody().getArray().getJSONObject(1).get("firstName").toString(), firstName2);
            assertEquals(listResponse.getBody().getArray().getJSONObject(1).get("lastName"), lastName2);
        });

        When("^I delete the client with the id (\\d+)$", (String id) -> {
            HttpResponse<String> deletedResponse;
            try {
                deletedResponse = Unirest.delete("http://localhost:" + port + "/client/" + id).asString();
            } catch (UnirestException e) {
                deletedResponse = null;
            }
            assertNotNull(deletedResponse);
            assertEquals(200, deletedResponse.getStatus());
            assertTrue(deletedResponse.getBody().contains("Client number " + id + " deleted !"));
        });

        Then("This time I should list only (\\d+) client with the name (\\w+) and last name (\\w+)$",
                (Integer count, String firstName, String lastName)-> {
            HttpResponse<JsonNode> listResponse;
            // list items
            try {
                listResponse = Unirest.get("http://localhost:" + port + "/client")
                        .asJson();
            } catch (UnirestException e) {
                listResponse = null;
            }

            assertNotNull(listResponse);
            assertEquals(200, listResponse.getStatus());
            assertEquals(listResponse.getBody().getArray().length(), 1);
            assertEquals(listResponse.getBody().getArray().getJSONObject(0).get("firstName").toString(), firstName);
            assertEquals(listResponse.getBody().getArray().getJSONObject(0).get("lastName").toString(), lastName);
        });


        When("^I clear the Person table$", () -> {
            HttpResponse<String> deletedResponse;
            try {
                deletedResponse = Unirest.delete("http://localhost:" + port + "/clear").asString();
            } catch (UnirestException e) {
                deletedResponse = null;
            }
            assertNotNull(deletedResponse);
            assertEquals(200, deletedResponse.getStatus());
            assertTrue(deletedResponse.getBody().contains("1 client(s) were deleted !"));
        });

        Then("^I should list no one when I list$", () -> {
            HttpResponse<JsonNode> listResponse;
            try {
                listResponse = Unirest.get("http://localhost:" + port + "/client")
                        .asJson();
            } catch (UnirestException e) {
                listResponse = null;
            }
            assertNotNull(listResponse);
            assertEquals(200, listResponse.getStatus());
            assertEquals(listResponse.getBody().getArray().length(), 0);
        });
    }
}
