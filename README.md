[Open in Eclipse Che](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2329/gr2329?new)

# ElevatU - Workout tracking for the advanced

## Introduction
ElevatU is a workout tracking application.

It consists of a backend service in Java, and a frontend application in React.

ElevatU is an project in the course [IT1901 - Informatics, Project I](https://www.ntnu.edu/studies/courses/IT1901#tab=omEmnet)
and is made using an agile development approach.

## Running the project
### Requirements
The following requirements must be met to run ElevatU:

#### Backend
> JDK17 or above  
> Maven
#### Frontend
> Node.js

### Running

#### Backend
After navigating to the `elevatu` folder, install all dependencies using
> mvn install

Then, navigate to `elevatu-webservice` and run
> mvn spring-boot:run

The tests can be ran using
> mvn test

#### Frontend
After navigating to the `elevatu-web` folder, install all dependencies using
> npm install

Then, run the application using
> npm start

TODO - write how to run npm tests

## Content

### Codebase

#### Backend
The codebase of the backend can be found in the `elevatu` folder.  
That's also where the `pom.xml` file used to build the project is located.  

The source code of the project can be found in `elevatu/{module}/src/main`, while the tests are located in `elevatu/{module}/src/test`.  
The test files are called `{class}Test` where `{class}` is the class the test is supposed to validate.

The codebase consists of two modules:

#### elevatu-webservice
Consists of the REST API for the application.

The logic, persistance and service layer of the application.
Calls upon `elevatu-api` for logic and json storage of the objects,
and uses `protobuf` for communication between the frontend and backend.

#### elevatu-api
Consists of the logic and persistance layers of the application.
Manager available objects and their persistance on the file system.
Uses objects from `elevatu-lib` to represent the data.

#### elevatu-lib
A common library which contains objects used by the backend.
It was created to avoid code duplication between the backend and frontend back when the frontend consisted of JavaFX.

### Documentation
Documentation regarding every iteration of the project can be found in the `docs/release{x}` folder
where `{x}` signals the release version.

## Code quality

### Coding conventions
Adjusted Sun's Java Conventions are used for the application. These can be found in the
`elevatu/elevatu_checks.xml` file.

`checkstyle` is used to check against those, as well as fail build which violate the rules.

The application can manually be checked using
> mvn checkstyle:check

## Test coverage
`JaCoCo` is used to report on test coverate.
The goal is to have 80-90% coverage on most classes.

It can be checked by building the application, and checking the file
> elevatu/{module}/target/site/jacoco/index.html