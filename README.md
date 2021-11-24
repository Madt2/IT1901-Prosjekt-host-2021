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

# How to build and run the project
We use maven to build and run this project:

To build the project, type `cd beastBook` and then `mvn install`

To run the server, type `mvn spring-boot:run -f rest/pom.xml`

To run the app, type `mvn javafx:run -f fxui/pom.xml`

To check for code coverage, spotbugs, checkstyles and tests, type `mvn verify`

# How to test the project
Testing of the projects is done by opening two terminals.

One terminal is used to test the project and one terminal is used to run the server.

First, type `cd beastBook/rest` and then `mvn spring-boot:run`

Open a new terminal and then type `cd beastBook` followed by `mvn test`

By then running the app in this terminal, files will be added to your computer, because the server is running as well.
