package com.turan.spark.service;

import com.turan.spark.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class ClientService {

    private SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        return new Configuration()
                .configure()
                .buildSessionFactory();
    }

    public String savePerson(Person person){

        Session session = sessionFactory.openSession();

        try {
            Transaction trx = session.beginTransaction();
            session.saveOrUpdate(person);
            trx.commit();
        }catch(Exception ex) {
            throw new RuntimeException("Client not found !");
        }
        finally{
            session.close();
        }

        return "Client record is saved !";
    }

    public List<Person> getAllPeople(){

        Session session = sessionFactory.openSession();
        List<Person> list = null;
        try {
            list = session.createQuery("FROM Person").list();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
            finally{
            session.close();
        }

        if(list != null){
            return list;
        }
        return null;
    }

    public Person getPerson(String clientId) {
        Person client;
        Session session = sessionFactory.openSession();;
        try {

            client = session.get(Person.class, Integer.parseInt(clientId));
        }catch(Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        finally{
            session.close();
        }
        return client;
    }

    public String deletePerson(String clientId) {
        Session session = sessionFactory.openSession();

        try {
            Transaction trx = session.beginTransaction();

            // delete object with primary key
            Query query = session.createQuery("DELETE FROM Person where id=:clientId");
            query.setParameter("clientId", Integer.parseInt(clientId));
            query.executeUpdate();

            trx.commit();
        }catch(Exception ex) {
            throw new RuntimeException("Client not found !");
        }
        finally{
            session.close();
        }
        return "Client number " + clientId + " deleted !";
    }

    // Clear all records from Person table
    public String clearPeople() {
        Session session = sessionFactory.openSession();
        int i = 0;

        try {
            Transaction trx = session.beginTransaction();

            Query query = session.createQuery("DELETE FROM Person");
            i = query.executeUpdate();

            trx.commit();
        }catch(Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        finally{
            session.close();
        }

        return i + " client(s) were deleted !";
    }
}
