[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2132/gr2132)

# BeastBook :muscle:

For in-depth description of documentation see `beastBook/README.md`

The core components are stored under `beastBook/core/src/main/java/beastbook/core`

The json components are stored under `beastBook/core/src/main/java/beastbook/json`

The UI components including fxml files are found under `beastBook/fxui/src/main/java/beastbook/fxui`

The rest components are stored under `beastBook/rest/src/main/java/beastbook/rest`

Documentation for the different releases are found in the `docs` folder

# Developer installation
To install the project, type `git clone https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2132/gr2132.git`

# Useful commands for the project
We use maven to build and run this project:

1. To **build the project**, type `cd beastBook` and then `mvn install`

2. To **run the server**, type `mvn spring-boot:run -f rest/pom.xml`

3. To **run the app**, type `mvn javafx:run -f fxui/pom.xml`

4. To **test the project**, type `mvn test`

5. To check for code coverage, spotbugs, checkstyles and tests, type `mvn verify`

# How to test and run the project
Testing and running of the projects is done by opening two terminals, terminal A and terminal B

Terminal A is used to run the server and terminal B is used to test the project or/and run the app

*In terminal A:*

Type `cd beastBook` and **run the server**

*In terminal B:*

To test the project, first **Build the project** and then **test the project**.

To run the project, just **run the app** in terminal A.

You can now see that files will be added to your computer, because both the server and the app is running.
