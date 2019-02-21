package com.turan.spark.service;

import com.turan.spark.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class CartServiceTest {

    private static SessionFactory sessionFactory;
    ClientService clientService;

    @Before
    public void setUp() throws Exception{
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
        clientService = new ClientService();
        clearPeople();
    }

    @Test
    public void testSavePerson(){
        //given
        Person person = new Person();
        person.setFirstName("Pamela");
        person.setLastName("Anderson");

        //when
        String result = clientService.savePerson(person);

        //then
        Session session = sessionFactory.openSession();
        Person savedPerson = session.get(Person.class, 1);

        session.close();

        assertNotNull(savedPerson);
        assertTrue(result.contains("Client record is saved !"));
        assertEquals(savedPerson.getFirstName(), person.getFirstName());
        assertEquals(savedPerson.getLastName(), person.getLastName());
    }

    @Test
    public void testGetAllPeople(){
        //given
        Person person = new Person();
        person.setFirstName("Pamela");
        person.setLastName("Anderson");

        Person person2 = new Person();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");

        clientService.savePerson(person);
        clientService.savePerson(person2);

        //when
        List<Person> people = clientService.getAllPeople();

        //then
        assertNotNull(people);
        assertEquals(people.size(), 2);
    }

    @Test
    public void testUpdatePerson(){
        //given
        Person person = new Person();
        person.setFirstName("Pamela");
        person.setLastName("Anderson");

        String result = clientService.savePerson(person);

        //when
        Session session = sessionFactory.openSession();
        person = clientService.getPerson("1");
        person.setLastName("Ericsson");
        result = clientService.savePerson(person);

        //then
        session = sessionFactory.openSession();
        Person savedPerson = session.get(Person.class, 1);

        session.close();

        assertNotNull(savedPerson);
        assertTrue(result.contains("Client record is saved !"));
        assertEquals(savedPerson.getFirstName(), person.getFirstName());
        assertEquals(savedPerson.getLastName(), "Ericsson");
    }

    @Test
    public void testDeletePerson() {
        //given
        Person person = new Person();
        person.setFirstName("Pamela");
        person.setLastName("Anderson");

        clientService.savePerson(person);

        //when
        String result = clientService.deletePerson("1");

        //then
        List<Person> people = clientService.getAllPeople();

        assertTrue(result.contains("Client number 1 deleted !"));
        assertEquals(people.size(), 0);
    }


    @Test
    public void testClearPeople(){
        //given
        Person person = new Person();
        person.setFirstName("Pamela");
        person.setLastName("Anderson");

        Person person2 = new Person();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");

        clientService.savePerson(person);
        clientService.savePerson(person2);

        //when
        String result = clientService.clearPeople();

        //then
        List<Person> people = clientService.getAllPeople();

        assertTrue(result.contains("2 client(s) were deleted !"));
        assertEquals(people.size(), 0);
    }


    // Utility method to clear all records from Person table
    public void clearPeople() {
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
    }
}
