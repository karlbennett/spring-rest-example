scratch-spring-webapp
==============

A very simple webapp that can be used to quickly try out code within a Java Servlet 3.0 web container.

The webapp can be run with the following command (Note: "`tomcat7`" this is required for async support):

    mvn tomcat7:run

This will start the server which can be accessed at [http://localhost:8080/scratch/spring/](http://localhost:8080/scratch/spring/ "scratch-spring-webapp")

It is also possible to carry out CRUD operation on simple users:

###### Create
    $ curl -XPOST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:8080/scratch/spring/users -d '{
        "email": "some.one@there.com",
        "firstName": "Some",
        "lastName": "One",
    }'

###### Retrieve
    $ curl -XGET -H "Accept:application/json" http://localhost:8080/scratch/spring/users
    $ curl -XGET -H "Accept:application/json" http://localhost:8080/scratch/spring/users/1

###### Update
    $ curl -XPUT -H "Content-Type:application/json" http://localhost:8080/scratch/spring/users/1 -d '{
        "email": "some.one@there.com",
        "firstName": "Some",
        "lastName": "Two",
    }'

###### Delete
    $ curl -XDELETE -H "Accept:application/json" http://localhost:8080/scratch/spring/users/1


The  webapp only contains four classes:

The controller class that handles the `/scratch/spring/`, `/scratch/spring/users`, and `/scratch/spring/users/{id}` request mappings.

[`scratch.spring.webapp.controller.ScratchController`](https://github.com/karlbennett/scratch-spring-webapp/blob/master/src/main/java/scratch/spring/webapp/controller/ScratchController.java "ScratchController")

The the configuration class that configures Spring MVC and Spring Data.

[`scratch.spring.webapp.config.ScratchConfiguration`](https://github.com/karlbennett/scratch-spring-webapp/blob/master/src/main/java/scratch/spring/webapp/config/ScratchConfiguration.java "ScratchConfiguration")
    
The the domain class that can be persisted into an in memory database using the CRUD endpoints.

[`scratch.spring.webapp.data.User`](https://github.com/karlbennett/scratch-spring-webapp/blob/master/src/main/java/scratch/spring/webapp/data/User.java "User")

And lastly the repository class that is used to persisted the User class.

[`scratch.spring.webapp.data.UserRepository`](https://github.com/karlbennett/scratch-spring-webapp/blob/master/src/main/java/scratch/spring/webapp/data/UserRepository.java "UserRepository")

There are also three configuration files:

The maven [`pom.xml`](https://github.com/karlbennett/scratch-spring-webapp/blob/master/pom.xml "pom.xml") file, this contains the Jetty plugin configuration and the dependencies for the project.

The [`log4j.xml`](https://github.com/karlbennett/scratch-spring-webapp/blob/master/src/main/resources/log4j.xml "log4j.xml") file that defines the log levels for the webapp and it's libraries. It's currently set to INFO.

The [`web.xml`](https://github.com/karlbennett/scratch-spring-webapp/blob/master/src/main/webapp/WEB-INF/web.xml "web.xml") file that configures the Spring dispatcher servlet to use the `ScratchConfiguration` class which in turn registers the `ScratchController`.

That is the entire project, have fun :)
