package com.turan.spark;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.turan.spark.model.Person;
import com.turan.spark.service.ClientService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Spark;

import java.util.Collections;

import static spark.Spark.*;

public class ApplicationMain {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationMain.class);

    private static final Gson GSON = new Gson();
    private static final ResponseTransformer JSON_TRANSFORMER = GSON::toJson;
    private static final String JSON = "application/json";

    private static ClientService clientService = new ClientService();

    public static void main(String[] Args) {
        testInMemory();
        startServer();
    }

    public static int startServer() {
        Spark.init();

        initializeRoutes();

        exception(JsonSyntaxException.class, ApplicationMain::handleInvalidInput);
        LOG.debug("Created exception handlers");

        Spark.awaitInitialization();
        LOG.debug("Ready");
        return Spark.port();
    }

    private static void initializeRoutes() {
        // Set response type to always be JSON
        before((request, response) -> response.type(JSON));

        path("/health", () -> get("", (req, res) -> "healthy"));

        // path for CRUD operations on Person entity

        // get all clients
        get("/client", (req, res) -> {
            res.type("application/json");
            return clientService.getAllPeople();
        }, JSON_TRANSFORMER);

        // get a client
        get("/client/:id", (req, res) -> {
            res.type("application/json");
            return clientService.getPerson(req.params(":id"));
        },  JSON_TRANSFORMER);

        // insert a client
        post("/client", (req, res) -> {
            res.type("application/json");
            Person client = GSON.fromJson(req.body(), Person.class);
            return clientService.savePerson(client);
        }, JSON_TRANSFORMER);

        // update a client
        put("/client", (req, res) -> {
            res.type("application/json");
            Person client = GSON.fromJson(req.body(), Person.class);
            return clientService.savePerson(client);
        }, JSON_TRANSFORMER);

        //Delete all people
        delete("/client/:id", (req, res) -> {
            res.type("application/json");
            return clientService.deletePerson(req.params(":id"));
        }, JSON_TRANSFORMER);

        //Delete all people
        delete("/clear", (req, res) -> {
            res.type("application/json");
            return clientService.clearPeople();
        }, JSON_TRANSFORMER);


        LOG.debug("Initialised routes");
    }

    private static void handleInvalidInput(Exception e, Request request, Response response) {
        response.status(400);
        errorResponse(e, request, response);
    }

    private static void errorResponse(Exception e, Request request, Response response) {
        response.type(JSON);
        response.body(GSON.toJson(Collections.singletonMap("error", e.getMessage())));
    }

    /**
     * For testing, as we want to start and stop the server.
     */
    public static void stopServer() {
        LOG.debug("Asking server to stop");
        Spark.stop();
    }


    public static void testInMemory(){
        Person person = new Person();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");

        SessionFactory sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();

        Session session = sessionFactory.openSession();

        session.save(person);

        Person savedPerson = session.get(Person.class, 1);

        savedPerson.setLastName("Turan");

        session.saveOrUpdate(person);

        System.out.println("Person saved : " + savedPerson.toString());

        session.close();

    }

}
