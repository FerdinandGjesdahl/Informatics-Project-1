# General information


## Introduction
ElevatU is a workout tracking application.

It consists of a backend service in Java, and a frontend application in React.

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

#### Frontend
After navigating to the `elevatu-web` folder, install all dependencies using
> npm install

Then, run the application using
> npm start

If you wish to call upon a mock REST controller that
returns dummy data, navigate to
> elevatu-web/src/helpers/DataSource.js

and set the `useMockData` variable to `true`.

## Tests
### Backend
The tests can be ran using
> mvn test

`JaCoCo` is used to report on test coverate.
The goal is to have 80-90% coverage on most classes.

It can be checked by building the application, and checking the file
> elevatu/{module}/target/site/jacoco/index.html

### Frontend
The tests can be ran using
> npm test

## Codebase

The following package diagram shows the codebase of the project.
![Package diagram](Package%20diagram.png)

The following classes are available (and defined in protobuf):
![Class diagram](Class%20diagram.png)

## Sequence Diagram
Both WorkoutPlan, WorkoutLog and Exercise do have the same endpoints
and are treated the same way. The sequence diagram (using Workouts as placeholder of those)
is as follows:
![Sequence Diagram - Workouts](Sequence%20Diagram%20-%20Workouts.png)

The user sessions checked here are the ones retrieved when logging in.
They are only valid for as long as the server is running.

The sequence diagram for the register/login is as follows:
![Sequence Diagram - User](Sequence%20Diagrams%20-%20User.png)

## Backend
The codebase of the backend can be found in the `elevatu` folder.  
That's also where the `pom.xml` file used to build the project is located.

The source code of the project can be found in `elevatu/{module}/src/main`, while the tests are located in `elevatu/{module}/src/test`.  
The test files are called `{class}Test` where `{class}` is the class the test is supposed to validate,
with some exceptions.

The codebase consists of three modules:

### elevatu-webservice
Consists of the REST API for the application.

The logic, persistance and service layer of the application.
Calls upon `elevatu-api` for logic and json storage of the objects,
and uses `protobuf` for communication between the frontend and backend.

### elevatu-api
Consists of the logic and persistance layers of the application.
Manager available objects and their persistance on the file system.

Uses `gson` for json serialization and deserialization.

Uses objects from `elevatu-lib` to represent the data.

### elevatu-lib
A common library which contains objects used by the backend.
It was created to avoid code duplication between the backend and frontend back when the frontend consisted of JavaFX.


### Code quality

#### Coding conventions
Adjusted Sun's Java Conventions are used for the application. These can be found in the
`elevatu/elevatu_checks.xml` file.

`checkstyle` is used to check against those, as well as fail build which violate the rules.

The application can manually be checked using
> mvn checkstyle:check

#### ESLint
For the React files `ESLint` was used to make sure our code is readable and uniform. 
We defined rules for it in out package.json file for making sure we had to include
Javadoc comments and space our imports properly.

## Frontend
This release we changed our frontend from javafx to React javascript and it is all located in
the elevatu-web folder. We changed the frontend to make it easier for further development of different features.
Each webpage has a jsx react file and a css style file that are in a folder named after the page.

For switching between pages we have a sidbar that users can use to access any page and it is located in the components folder.
In the test folder there are also jest tests for the React pages for testing the frontend implementation.

### Communication frontend <-> backend
Protobuf is used to serialize the data sent between the frontend and the backend.
This ensures type safety as well as shared code between both parts of the application.

This is more throughly discussed under 'Release 3' below.

The REST api can be viewed in the `REST_API.md` file.
We have experimented with automatic documentation using `Swagger`, but it turned 
out very laggy and difficult to use with protobuf.
At that point we had big oarts of the REST API done, and decided therefore 
to write the documentation ourselves.


# Release 3

This release, we have focused on turning our application into a REST API.

## Protobuf
For communication between the frontend and the backend, we have used [Google's Protocol Buffers](https://protobuf.dev/).
This is a way to serialize data, and was chosen over JSON mainly for two reasons:
### Type safety
To ensure that the data sent between the frontend and the backend is correct, we wanted to use a type safe way of serializing data.
That means that we can make sure that the data sent is both serialized and deserialized automatically without worrying
about typo's or other errors.
### Shared code
Using protobuf, we can generate code for both the frontend and the backend, and therefore ensure that the data sent is the same on both sides.
We also get the benefit of not having to make the same classes twice, and therefore not having to worry about keeping them in sync.

This is done by using the `protoc` compiler, which takes a `.proto` file and generates code for the desired language.
For Java, we have made this happen automatically on every build using the `protobuf-jar-maven-plugin`. For React,
we have used [protobuf-javascript](https://github.com/protocolbuffers/protobuf-javascript) 
and invoked the `protoc` compiler directly (as described under `elevatu-web)`, and then imported the generated code into the project.

An issue we encountered was that the documentation for using protobuf with React was not very good, and we had to do some
trial and error to get it working. However, we are now happy with the result, and think that it is a good solution.
For Java, we have also had to implement custom `toProto` and `fromProto` methods for some classes,
as we used custom methods in some of those (and they were previously used in the project).  
An alternative would have been to create wrapper classes, but we felt that this was a better solution.
This unfortunately means that we have to keep the `toProto` and `fromProto` methods in sync with the `.proto` file,
but we felt that this was a good compromise.

## Login sessions
To make sure that the user is logged in, we have implemented a login session. This is done by creating a random token
when the user logs in, and then storing this token in a cookie. This cookie is then sent with every request, and the
backend checks if the token is valid. If it is, the user can access their own data.

This is a simple way to make sure that the user is logged in, and we are happy with the result.
The solution is not very secure, as the token is stored in a cookie, and therefore can be stolen by a malicious user,
but it's a good start and was deeped appropiate for the project.

## Password hashing and salting
To avoid exposing raw text passwords
(which would be a huge security risk), the passwords are hashed using SHA-512 as well as salted for extra security.
Only the password hash and the salt are stored in the database, and the salt is randomly generated for each user.
When logging in, we compare the hashed password with a hashed version of whatever the user has entered (using the saved salt).

## GSON for JSON
We have used GSON for JSON serialization and deserialization.
More discussion about this matter can be found under `docs/Release 2`.



