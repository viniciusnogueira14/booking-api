# Technical Test
This project was created as a test for a technical evaluation.

## About the project
The project was created using the following technologies and frameworks:
* [Java 17](https://docs.oracle.com/en/java/javase/17/docs/api/index.html)
* [Spring Boot 3.2.0](https://docs.spring.io/spring-boot/docs/3.2.0/api/)
* [Maven](https://maven.apache.org/index.html)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
* [H2 Database](https://www.h2database.com/html/main.html) - [[Maven Dependency]](https://mvnrepository.com/artifact/com.h2database/h2)
* [Project Lombok](https://projectlombok.org/) - [[Maven Dependency]](https://mvnrepository.com/artifact/org.projectlombok/lombok)
* [Model Mapper](https://modelmapper.org/user-manual/) - [[Maven Dependency]](https://mvnrepository.com/artifact/org.modelmapper/modelmapper)
* [Spring Doc Open API](https://springdoc.org/) - [[Maven Repository]](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui)


## Known Backlog
### General
* Improve the Suite of Tests creating more integrated and API Tests;
* Increase the coverage of Unit Tests, specially in the Service Classes;
* Implement the CI/CD strategy (Docker, Kubernetes, Pipelines, etc.);
* Create an integration with SonarQube and define a level for the Quality Gate;

### Controller Module
* Improve the ExceptionHandler adding more Exceptions, for example create a new GenericException for the unexpected errors;

### Repository Module
* Implement the methods of finding using the Specification or other Criteria objects

### Service Module
* As mentioned previously, create a GenericException to handle all the Unexpected error;
* Improve the validation of the data by thinking in new edge cases; 

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.0/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#using.devtools)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#appendix.configuration-metadata.annotation-processor)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#io.validation)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.2.0/reference/htmlsingle/index.html#actuator)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

