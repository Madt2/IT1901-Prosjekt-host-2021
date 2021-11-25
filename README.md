[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2132/gr2132)

# BeastBook :muscle:
## Description of the BeastBook:

For in-depth description of documentation see `beastBook/README.md`

The core components are stored under `beastBook/core/src/main/java/beastbook/core`

The json components are stored under `beastBook/core/src/main/java/beastbook/json`

The UI components including fxml files are found under `beastBook/fxui/src/main/java/beastbook/fxui`

The rest components are stored under `beastBook/rest/src/main/java/beastbook/rest`

Documentation for the different releases are found in the `docs` folder

## Developer installation
To install the project, type `git clone https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2132/gr2132.git`

# Useful commands for the project
We use maven to build and run this project:
(Due to use of server, project must be installed without tests first, see instructions underneath:)

1. To **build the project**, type `cd beastBook` and then `mvn install -DskipTests=true`


2. Open a second terminal


3. In the second terminal, start the server:
   To **run the server**, type `mvn spring-boot:run -f rest/pom.xml`


4. Go back to the first terminal


5. To **run the app**, type `mvn javafx:run -f fxui/pom.xml`


7. To **test the project**, type `mvn test`


7. To check for code coverage, spotbugs, checkstyle and tests, type `mvn verify`

You can now see that files will be added to your computer, because both the server and the app is running.
