The application is a java spark rest api which provides end points for making some of the CRUD operations on an in 
memory H2 database which has an entity named Person. Hibernate is used for mapping the Person class to the relevant
table.
 
The application has integration tests written with Cucumber and unit tests written with JUnit. Follow the instructions
to run theses tests. Application runs on port 4567.

. To compile, package and run the application, go to the root folder and run the following commands;

If you have maven installed you may compile and package with;

mvn clean compile

mvn package


. You may run the from your IDE  by running ApplicationMain.java

. To run from the command line, after going to the root of your application;

cd target

. Run;

java -jar java-spark-demo-0.0.1-SNAPSHOT.jar

. Doing CRUD operations with the api;

If you have Postman installed you may try all the CRUD operations that the api provides.

GET http://localhost:4567/client (Selects all clients)

GET http://localhost:4567/client/{id} (Selects a client by Id)   

POST http://localhost:8080/client (Inserts a client)

Body;

    { 
		"firstName": "Jane",
		"lastName": "Austen"
    }
    
Returns : "Client record is saved !"

POST http://localhost:8080/client (Updates an existing client)

Body;

    { 
        "Id" : "1"
		"firstName": "Jane",
		"lastName": "Simple"
    }
    
Returns : "Client record is saved !"
    

DELETE http://localhost:8080/client/{id} (Deletes a client)

Returns : "Client number {id} deleted !"


. For testing please run CucumberRunnerIt under integration package and you may also make the 
unit tests for ClientService CRUD methods by running CLientServiceTest.

