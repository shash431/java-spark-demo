package com.turan.spark.service;

import com.turan.spark.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ClientService {

    private SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        return new Configuration()
                .configure()
                .buildSessionFactory();
    }

    public String addPerson(Person person){

        Session session = sessionFactory.openSession();
        session.save(person);
        session.close();

        return "person added";
    }

    public List<Person> getAllPeople(){

        Session session = sessionFactory.openSession();
        List<Person> list = session.createQuery("FROM Person").list();
        session.close();

        if(list != null){
            return list;
        }
        return null;
    }

    public Person getPerson(String clientId) {
        Session session = sessionFactory.openSession();
        Person client;
        try {
            client = session.get(Person.class, Integer.parseInt(clientId));
        }catch(Exception ex) {
            throw new RuntimeException("Client not found");
        }
        finally{
            session.close();
        }
        return client;
    }
}
