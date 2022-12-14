
# Hands-on session 11: Microservices

In this session, the next topics are covered:

- The traditional Monolith Application
- What are Microservices?
- Microservice Architecture and Design
- 12 Factors Application
- Restful Web Services with Spring Boot Demo

## Required software

For this session it is required to have installed:

- Java SDK 17. (*)
- Apache Maven 3.x version. (*)
- An IDE (IntelliJ, Eclipse, Netbeans)

Also, do not forget to define the following environment variables:

- `M2_HOME` -> root directory of your Maven installation.
- `JDK_HOME` -> root directory of your JDK installation.
- Update the `PATH` environment variable by adding the `bin` directories of your JDK and Maven installations.

(*) You can avoid doing all these step manually by installing
[SDKMAN](https://sdkman.io/).

## Restoring a MySQL dump file

Run the following command in a terminal by using (do not forget to update/provide your MySQL credentials):

```sh
mysql -u <user> -p < beers.sql
```
