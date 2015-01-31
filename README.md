spring-rest-example
==============

A very simple webapp that can be used to quickly try out code within a Spring project.

#### Run

The webapp can be run with the following command:

    mvn spring-boot:run

Or after building the project with `mvn clean package` you can run the executable war.

    java -jar target/example-spring-rest-1.0-SNAPSHOT.war

This will start the server which can be accessed at [http://localhost:8080/rest/](http://localhost:8080/rest/ "example-spring-rest")

It is also possible to carry out CRUD operations on simple users:

###### Create
    $ curl -XPOST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8080/rest/users -d '{
        "email": "some.one@there.com",
        "firstName": "Some",
        "lastName": "One",
        "phoneNumber": "5551234",
        "address": {
            "number": 3,
            "street": "That Road",
            "suburb": "This Place",
            "city": "Your City",
            "postcode": "ABC123"
        }
    }'

###### Retrieve
    $ curl -XGET -H "Accept:application/json" http://localhost:8080/rest/users
    $ curl -XGET -H "Accept:application/json" http://localhost:8080/rest/users/1

###### Update
    $ curl -XPUT -H "Content-Type:application/json" http://localhost:8080/rest/users/1 -d '{
        "email": "some.one@there.com",
        "firstName": "Some",
        "lastName": "Two",
        "phoneNumber": "5551234",
            "address": {
                "id": 1,
                "number": 3,
                "street": "That Road",
                "suburb": "This Place",
                "city": "Your City",
                "postcode": "ABC123"
            }
    }'

###### Delete
    $ curl -XDELETE -H "Accept:application/json" http://localhost:8080/rest/users/1


The  webapp only contains seven classes:

The entity classes that are used to persist the users to the database as well as to define the endpoint API.

[`example.rest.spring.data.Id`](https://github.com/karlbennett/spring-rest-example/blob/master/src/main/java/example/rest/spring/data/Id.java "Id")

[`example.rest.spring.data.User`](https://github.com/karlbennett/spring-rest-example/blob/master/src/main/java/example/rest/spring/data/User.java "User")

[`example.rest.spring.data.Address`](https://github.com/karlbennett/spring-rest-example/blob/master/src/main/java/example/rest/spring/data/Address.java "Address")

The controller class that handles the `/rest/` request mapping.

[`example.rest.spring.controller.ScratchController`](https://github.com/karlbennett/spring-rest-example/blob/master/src/main/java/example/rest/spring/controller/ScratchController.java "ScratchController")

The controller class that handles the `/rest/users` and `/rest/users/{id}` request mappings.

[`example.rest.spring.controller.UserController`](https://github.com/karlbennett/spring-rest-example/blob/master/src/main/java/example/rest/spring/controller/UserController.java "UserController")

The the application class that starts and configures Spring Boot.

[`example.rest.spring.SpringBootRestServlet`](https://github.com/karlbennett/spring-rest-example/blob/master/src/main/java/example/rest/spring/SpringBootRestServlet.java "SpringBootRestServlet")

The repository class that is used to persisted the `User` class.

[`example.rest.spring.data.UserRepository`](https://github.com/karlbennett/spring-rest-example/blob/master/src/main/java/example/rest/spring/data/UserRepository.java "UserRepository")

There are also two configuration files:

The maven [`pom.xml`](https://github.com/karlbennett/spring-rest-example/blob/master/pom.xml "pom.xml") file, this contains the plugin configurations and the dependencies for the project.

The [`application.properties`](https://github.com/karlbennett/spring-rest-example/blob/master/src/main/resources/application.properties "application.properties") file, this currently only contains the log levels for the application, but could contain any properties that are relevant to the application.

That is the entire project, have fun :)
