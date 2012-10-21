scratch-webapp
==============

A very simple webapp that can be used to quickly try out code within a JEE web container.

The webapp can be run with the following command:

    mvn jetty:run

This will start the server which can be accessed at [http://localhost:8080/scratch-webapp/scratch/](http://localhost:8080/scratch-webapp/scratch/ "scratch-webapp")

The  webapp contains only two classes:

The controller class that handles the `/scratch-webapp/scratch/` request mapping.

    scratch.webapp.controller.ScratchController

And the configuration class that configures Spring.

    scratch.webapp.config.ScratchConfiguration
    
There are also three configuration files:

The maven `pom.xml` file, this contains the Jetty plugin configuration and the dependencies for the project.

The `log4j.xml` file that defines the log levels for the webapp and it's libraries. It's currently set to INFO.

The `web.xml` file that configures the Spring dispatcher servlet to use the `ScratchConfiguration` class which in turn registers the `ScratchController`.

That is the entire project, have fun :)