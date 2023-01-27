
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
- Erlang OTP. You can install the Erlang OTP from [here](https://computingforgeeks.com/how-to-install-latest-erlang-on-ubuntu-linux/), given that you are usig Ubuntu}. Given that you are using Ubuntu. 
- An IDE (IntelliJ, Eclipse, Netbeans)
- [Terminator](https://linuxhint.com/install-terminator-ubuntu-22-04/) terminal.(This one is optional).

Also, do not forget to define the following environment variables:

- `M2_HOME` -> root directory of your Maven installation.
- `JDK_HOME` -> root directory of your JDK installation.
- Update the `PATH` environment variable by adding the `bin` directories of your JDK and Maven installations.

(*) You can avoid doing all these step manually by installing
[SDKMAN](https://sdkman.io/).

**Make sure Erlang Port Mapper Daemon (epmd) is running.**
Try this command on your [Terminator](https://linuxhint.com/install-terminator-ubuntu-22-04/)  to check the status of the epmd:
```bash

 ps ax | grep epmd

```
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

To start the Hello Erlang Server process:(Open a terminal and run the following command).

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
To start the Cleint process:( Open an other terminal and run the following command ).

```bash
erl -name hello_client@127.0.0.1 -setcookie 'hellocookie'
```

After the connection is established between the Server and the Client process. The client can send data to the server as follows.
Client to send message (NB: dont forget the 🟥dot🟥 at the end of the command, it is part of the erlang  syntax):

```bash 

{'HelloMailBox', 'hello_server@127.0.0.1'} ! {self(), "Tesfaye Yimam"}.

```
Finally the client can read the message as follows:
Client to read a message:

```bash
receive T -> T end.
```

For Lab 04 you can follow similar steps and update the the variables accordinglty.
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
