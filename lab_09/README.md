
# Hands-on session 9: EJBs

In this session, the next topics are covered:

- Java Enterprise Application Project
- Step 1: defining the interfaces - pojos project
- Step 2: defining the EJB Application project
- Step 3: defining the Web Application project
- Step 4: deploying our EJB & Web Application in Glassfish
- Referring to a JDBC Resource


## Required software

For this session it is required to have installed:

- Java SDK 11. (*)
- Apache Maven 3.x version. (*)
- [Glassfish 6.2.5](https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.2.5.zip) version.
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
mysql -u <user> -p < world.sql
```

## Exercise 01: BeersEJB

The solution will be uploaded Nov 10th.

## Exercise 02: BeersEJB v.2.0

The solution will be uploaded Nov 10th.

## Exercise 03: Shopping Cart EJB

The solution will be uploaded Nov 10th.