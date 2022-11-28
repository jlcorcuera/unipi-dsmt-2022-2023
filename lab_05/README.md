
# Hands-on session 5: Apache Tomcat


In this session, the next topics are covered:

- What is Apache Tomcat?
- Installation
- Folder structure
- Configuration files
- Execution
- Tomcat Manager
- Mounting a static resource context
- Tomcat as a static resource server


## Required software

For this session it is required to have installed:

- Java SDK 11. (*)
- Apache Maven 3.x version. (*)
- [Apache Tomcat 9.x](https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.69/bin/apache-tomcat-9.0.69.zip) version.
- An IDE (IntelliJ, Eclipse, Netbeans)

Also, do not forget to define the following environment variables:

- `M2_HOME` -> root directory of your Maven installation.
- `JDK_HOME` -> root directory of your JDK installation.
- Update the `PATH` environment variable by adding the `bin` directories of your JDK and Maven installations.

(*) You can avoid doing all these step manually by installing
[SDKMAN](https://sdkman.io/).

## Exercise 01: Installing and running Apache Tomcat

- Once you downloaded Apache Tomcat, uncompress the zip file.
- Grant execution permission to all sh files inside the bin directory
```bash
  cd bin
  chmod +x *.sh
```
- Run the Apache Tomcat by executing the following command
```bash
  cd bin
  sh startup.sh
```
- Open a browser and access to the following address http://localhost:8080

## Exercise 02: Configuring Tomcat Manager access

- Make sure your instance of Apache Tomcat is shutdown. If it is running, run the following command:
```bash
  cd bin
  sh shutdown.sh
```
- Edit the ${CATALINA_HOME}/conf/tomcat-users.xml file by adding the following XML elements inside the tomcat-users XML tag:
```xml
<role rolename="manager-gui"/>
<role rolename="manager-status"/>
<user username="manager" password="manager" roles="manager-gui"/>
<user username="status" password="status" roles="manager-status"/>
```
- Run the Apache Tomcat by executing the following command
```bash
  cd bin
  sh startup.sh
```
- Open a browser and access to manager web page by using this url: http://localhost:8080/manager/html

## Exercise 03: Mounting a static resource context

- Make sure your instance of Apache Tomcat is shutdown. If it is running, run the following command:
```bash
  cd bin
  sh shutdown.sh
```
- Create a the following xml file: ${CATALINA_HOME}/conf/Catalina/localhost/resources.xml. Put the following content inside the xml file:
```xml
<Context docBase="/home/jose/Pictures" path="/resources"/>
```
- Run the Apache Tomcat by executing the following command
```bash
  cd bin
  sh startup.sh
```
- Open a browser and try to load a resource that is located inside the "resource" context. For example: http://localhost:8080/resources/parrot.jpg.


