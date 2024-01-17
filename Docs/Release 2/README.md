# Release 2

In Release 2 we have focused more on modularization as well as 
improvement of the codestyle.
We have also further improved the functionality of the application.

## Modularization

The application has been split into three modules, each handling different
parts of the application.

TODO - maybe write about this point
> reflektere over og velge mellom dokumentmetafor (desktop) og implisitt lagring (app)

### elevatu-lib
At the core, the module elevatu-lib consists of the objects both the
presentation and logic layer use, eg. User, Workout, etcetc.

This package is depended upon by the other modules, and by keeping that data
inside it instead of in the other ones, we avoid having to change things twice.

### elevatu-service
This module consists of the logic and persistence layer of the application.
It takes care of all actions requested by the application layer, and stores them to json.

We have gone with the approach of using gson for json storage, as we felt
as it was a good compromise between ease-to-use and documentation.
While Jackson seemed to have more features, it also seemed less used in the community, and 
therefore more difficult to find common problems for. It also seemed "overkill" for our usage.
On the other end, we concidered 'org.json', but it seemed to have much less automatic serialization than 
the selected library.
The hope is for automatic serialization of most objects, even though we had to make
a custom serializer for the User object as we are hashing and salting the passwords.

### elevatu-app
This module consists of the presentation layer of the application.
It lets the user interact with the application.

TODO - write more here

## Code quality
For this release, we have focused more on code quality, and used packages to help us with so.
We have mainly implemented three strategies.

### Checkstyle
We are using the `checkstyle` plugin to handle a uniform, readable codebase.
We used the Sun Java coding conventions, and adjusted them to more fit our project.
The checks can be found in the `elevatu_checks.xml` file.

Checkstyle has been configured to fail the build on violations of the style, and therefore
forces the group to adhere to the conventions.

### JaCoCo
`JaCoCo` has been selected as a plugin to check test coverate.
It is ran on every compilation of the application, and gives us a simple
interface showing which lines are not tested.

The test coverage for most classes seem about 80-90%, which we 
think is a good number.

### Development approach
As in `Release 1`, we have been diligently using the
`Issues` section of `GitLab`together with a one-issue-one-branch approach.
That way, every merge request has been small, and therefore easily readable.
We have created an `Milestone` for this release which has been used for the issues.

We have adopted an approach where another member of the team has to approve 
the request before merging into `master`.

We have also been trying some pair-programming, even though it has 
been much less than we hoped for as many of us have had stressful weeks
and it therefore has been difficult to organize.


### Eclipse Che 
Eclipse Che is being used to work with the project from a link in the root README file. 
This enviroment is customized with the devfile.yaml file in the root of the project. 
This makes sure that everyone working on the project to run it on the same IDE with the same java version and plugins.

### Functionality
The functionality for release 2 is based on user story 2 and it is important to be able to do a few things.
    -Create an excercise 
    -Create a workout
    -Star the workout

So we have made pages for the application where you can do these things so the users can make and use their own training sessions.

### TODO - write more
TODO - write more about different steps done 

### TODO - document architecture
TODO - document architecture as in the requirement:
> dokumentere arkitektur med minst et diagram (bruk PlantUML) i tillegg til teksten i readme