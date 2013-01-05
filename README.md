scratch-webapp
==============

A very simple webapp that can be used to quickly try out code within a JEE web container.

The webapp can be run with the following command:

    mvn jetty:run

This will start the server which can be accessed at [http://localhost:8080/scratch-webapp/scratch/](http://localhost:8080/scratch-webapp/scratch/ "scratch-webapp")

It is also possible to carry out CRUD operation on simple users:

###### Create
    $ curl -XPOST http://localhost:8080/scratch-webapp/scratch/users -d '{
        "email": "some.one@there.com",
        "firstName": "Some",
        "lastName": "One",
    }'

###### Retrieve
    $ curl -XGET http://localhost:8080/scratch-webapp/scratch/users
    $ curl -XGET http://localhost:8080/scratch-webapp/scratch/users/1

###### Update
    $ curl -XPUT http://localhost:8080/scratch-webapp/scratch/users/1 -d '{
        "email": "some.one@there.com",
        "firstName": "Some",
        "lastName": "Two",
    }'

###### Delete
    $ curl -XDELETE http://localhost:8080/scratch-webapp/scratch/users/1


The  webapp contains only four classes:

The controller class that handles the `/scratch-webapp/scratch/`, `/scratch-webapp/scratch/users`, and `/scratch-webapp/scratch/users/{id}` request mappings.

[`scratch.webapp.controller.ScratchController`](https://github.com/karlbennett/scratch-webapp/blob/master/src/main/java/scratch/webapp/controller/ScratchController.java "ScratchController")

The the configuration class that configures Spring MVC and Spring Data.

[`scratch.webapp.config.ScratchConfiguration`](https://github.com/karlbennett/scratch-webapp/blob/master/src/main/java/scratch/webapp/config/ScratchConfiguration.java "ScratchConfiguration")
    
The the domain class that can be persisted into an in memory database using the CRUD endpoints.

[`scratch.webapp.data.User`](https://github.com/karlbennett/scratch-webapp/blob/master/src/main/java/scratch/webapp/data/User.java "User")

And lastly the repository class that is used to persisted the User class.

[`scratch.webapp.data.UserRepository`](https://github.com/karlbennett/scratch-webapp/blob/master/src/main/java/scratch/webapp/data/UserRepository.java "UserRepository")

There are also three configuration files:

The maven [`pom.xml`](https://github.com/karlbennett/scratch-webapp/blob/master/pom.xml "pom.xml") file, this contains the Jetty plugin configuration and the dependencies for the project.

The [`log4j.xml`](https://github.com/karlbennett/scratch-webapp/blob/master/src/main/resources/log4j.xml "log4j.xml") file that defines the log levels for the webapp and it's libraries. It's currently set to INFO.

The [`web.xml`](https://github.com/karlbennett/scratch-webapp/blob/master/src/main/webapp/WEB-INF/web.xml "web.xml") file that configures the Spring dispatcher servlet to use the `ScratchConfiguration` class which in turn registers the `ScratchController`.

That is the entire project, have fun :)
