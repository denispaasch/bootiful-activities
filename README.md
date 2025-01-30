# Summary

This app gets activity suggestions from an external endpoint and generates participants for
them using a faker library. On each startup it fetches a configurable amount of activities more.
It stores them into an H2 database which obtains its schema using Flyway.
Both the activities and their participants can be accessed via REST using the below API entry point.

| Task                                                                             |                                                                                                                                                                                                                                                                                                                                         |
|----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Java 11 (or later)                                                               | Java 21                                                                                                                                                                                                                                                                                                                                 |
| Uses Spring Boot                                                                 | Spring Boot 3.4.2                                                                                                                                                                                                                                                                                                                       |
| Is a RESTful service                                                             | See API entry point after starting the app: http://localhost:8080/api/v1                                                                                                                                                                                                                                                                |
| Includes some tests (full test coverage isn't necessary for this sample project) | See [here](https://github.com/denispaasch/bootiful-activities/blob/master/application/src/test/java/be/dpa/bootiful/activities/application/rest/ActivityControllerTest.java) and [here](https://github.com/denispaasch/bootiful-activities/blob/master/distribution/src/test/java/be/dpa/bootiful/activities/BootifulActivitiesIT.java) |
| Bonus (Optional): requests data from an external endpoint                        | See [here](https://github.com/denispaasch/bootiful-activities/blob/master/infrastructure/src/main/java/be/dpa/bootiful/activities/infrastructure/bored/BoredActivityProvider.java)                                                                                                                                                      |


# API entry point

From there the activities and their participants are discoverable:

http://localhost:8080/api/v1

# Swagger-UI

The API documentation can be found here after startup:

http://localhost:8080/swagger-ui/index.html

# Java code coverage

After the build there is a report within target/site/jacoco.

# H2 console

http://localhost:8080/h2-console


