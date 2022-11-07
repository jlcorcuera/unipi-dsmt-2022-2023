
# Hands-on session 4: Apache Maven & jinterface

In this session, the next topics are covered:

- Java project management with Maven.
- Maven in Intellij IDEA.
- Management of dependencies in application deployment.
- The package JInterface for Java-Erlang interaction.
- Exercises with Maven.

## Required software

For this session it is required to have installed:

- Java SDK 11. (*)
- Apache Maven 3.x version. (*)
- Erlang OTP.
- An IDE (IntelliJ, Eclipse, Netbeans)

Also, do not forget to define the following environment variables:

- `M2_HOME` -> root directory of your Maven installation.
- `JDK_HOME` -> root directory of your JDK installation.
- Update the `PATH` environment variable by adding the `bin` directories of your JDK and Maven installations.

(*) You can avoid doing all these step manually by installing
[SDKMAN](https://sdkman.io/).

**Make sure Erlang Port Mapper Daemon (epmd) is running.**
## Jinterface installation

Install into your Maven local repository the jinterface dependency:

```sh
mvn install:install-file \
   -Dfile=/usr/lib/erlang/lib/jinterface-1.13/priv/OtpErlang.jar \
   -DgroupId=com.ericsson \
   -DartifactId=erlang-jinterface \
   -Dversion=1.13.1 \
   -Dpackaging=jar \
   -DgeneratePom=true
```

## Exercise 03: Hello from Erlang!

Enter to the "erlang_files" directory and compile all erlang files

```bash
  cd erlang_files
  erlc *.erl
```

To start the Hello Erlang Server process:

```bash
  erl -name hello_server@127.0.0.1 \
      -setcookie 'hellocookie' \
      -s hello_server init \
      -noshell
```

The server process should display the following message:

```bash
  Registered MailBox alias: "HelloMailBox" for PID: <0.9.0> 
  hello_server: waiting for new messages... 
```

Update some configuration variables (i.e: serverName, serverMailBox, etc.) in the it.unipi.dsmt.javaerlang.Exercise03 java class and run it.
## Exercise 04: Cookie Quotes Server

Enter to the "erlang_files" directory and compile all erlang files

```bash
  cd erlang_files
  erlc *.erl
```

To start the Hello Erlang Server process:

```bash
  erl -name quotes_server@127.0.0.1 \
      -setcookie 'abcde' \
      -s quotes_server init \
      -noshell
```

The server process should display the following message:

```bash
Registered MailBox alias: "QuotesMailBox" for PID: <0.9.0> 
Loaded quotes: ["A new outlook brightens your image and brings new friends.",
                "Conquer your fears. Otherwise your fears will conquer you.",
                ...
                "Lets get that bread today.",
                "Stop worrying and take a chance."] 
quotes_server: waiting for new messages... 

```

Update some configuration variables (i.e: serverName, serverMailBox, etc.) in the it.unipi.dsmt.javaerlang.Exercise04 java class and run it.
