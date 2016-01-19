This project contains a basic solution to the Data Cleaning Service
assignment described [here](docs/SeniorDevAssignment.pdf).

Features
--------
* REST-based services (implemented with the aid of [Jersey](https://jersey.java.net/))
  to allow cleaning of data sets and management of categories
* Categories are managed in-memory.  A read-write locking model (using the Java
  `ReentrantReadWriteLock` ensures that categories can be changed at runtime
  without adversely affecting running clean operations.  Locking is done with
  the aid of AOP Aspects in order to not overly complicate the category service
  itself.
* An application configured with [Spring Boot](http://projects.spring.io/spring-boot/)
  to allow the services to be exposed as a single microservice (and not be
  dependent on an external application server or servlet runner)
* Fully automated unit and integration tests (using [JUnit](http://junit.org/),
  [Mockito](http://mockito.org/), and [REST Assured](https://github.com/jayway/rest-assured))
  to test all code within the system.  Spring's integration test support is
  also leveraged to automatically start and stop the server prior to testing of
  the REST interfaces themselves.


Requirements
------------
Java 1.8 is **required** to build or run this project.  It is also
assumed that access to Maven Central is available to download dependencies.

No other software is required in order to run the project.  It is designed as a
self-contained microservice, and uses a embedded Tomcat server (running on
port 8080) to process requests to the services.

Building
--------
To build the project, simply clone it to a local working copy and execute
`./gradlew build` (`.\gradlew build` on Windows).  This will build all source,
build and run the unit tests, and then build and run the integration tests.
The server itself will be started automatically prior to execution of any tests
against the REST services themselves.

Deployment and Running
-------
The service can easily be started from the command line or via Gradle:

#### Command Line ####
Once the project has been built, simply navigate to the
`${PROJECT_HOME}/build/libs` directory and launch it using `java -jar` as
follows:

    java -jar  dataCleaner-1.0.0-SNAPSHOT.jar

#### Gradle ####
From the command line, simply navigate to the root of the project.  Gradle can
be used to launch the server with the following command:

    ./gradlew bootRun

(On Windows, use `.\gradlew bootRun` instead)

#### Deployment ####
To deploy this to an external server, simply copy
`${PROJECT_HOME}/build/libs/dataCleaner-1.0.0-SNAPSHOT.jar` to the target
server.  It can then be launched directly using a call to the `java` executable
as described above.

Monitoring
----------
Both REST services expose health check endpoints which can be used for
monitoring their life cycle:
* **`http://localhost:8080/category/health`** will report the status of the
  Category Service
* **`http://localhost:8080/data/health`** will report the status of the Data Set
  Service.

(Obviously, if these services are deployed to a different host, use that
 hostname instead of `localhost`)

Code Structure
--------------
This project follows the standard Maven/Gradle structure for projects.  In
addition, it uses the
[Nebula Project Plugin](https://github.com/nebula-plugins/nebula-project-plugin)
to create a separate facet (and runtime steps) for Integration Tests.  Code can
thus be found in the following locations:
* **`src/main/java`** - The main project code
* **`src/test/java`** - Unit tests
* **`src/integTest/java`** - Integration tests

Enhancements
------------
A number of enhancements are possible for this project.  In particular,
clustering could be most easily achieved by moving the Category data into a
RDBMS or other data store, and then fronting that store with a distributed
cache such as [ehcache](http://www.ehcache.org/) or similar.   If such was
done, the locking in the category service should be removed, as it no longer
provides any effective protection.

Note that no other changes would be needed to the existing code for clustering,
as these services are entirely stateless otherwise.