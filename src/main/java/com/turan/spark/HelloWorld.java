package com.turan.spark;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.turan.spark.model.Person;
import com.turan.spark.service.ClientService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static spark.Spark.*;

public class HelloWorld {

    public static ClientService clientService = new ClientService();

    public static void main(String[] args) {


        testInMemory();

        get("/hello", (req, res) -> "Hello Spark!\n");

        get("/hello/:name", (request, response) -> {
            response.type("application/json");
            return ImmutableMap.of("name", request.params(":name"));
        },  new Gson()::toJson);

        get("/say/*/to/*", (request, response) -> {
            System.out.println(request.splat()[0]);

            return "Number of splat parameters: " + request.splat().length;
        });

        Gson gson = new Gson();


        get("/client", (req, res) -> {
            res.type("application/json");
            return clientService.getAllPeople();
        }, gson ::toJson);

        get("/client/:name", (request, response) -> {
            response.type("application/json");
            return clientService.getPerson(request.params(":name"));
        },  new Gson()::toJson);

        post("/client", (req, res) -> {
            res.type("application/json");
            Person client = gson.fromJson(req.body(), Person.class);
            return clientService.addPerson(client);
        }, gson ::toJson);

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

        System.out.println("Person saved : " + savedPerson.toString());

        session.close();
    }
}

