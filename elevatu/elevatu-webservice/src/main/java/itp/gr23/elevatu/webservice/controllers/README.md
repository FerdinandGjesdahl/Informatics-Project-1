# REST endpoints

## Preface

### Protobuf
All objects refered below are protobuf objects as
defined in `gr2329/protobuf/` folder.
When sending or receiving a data, it's important to set
`Content-Type` or `Accept` headers to `application/x-protobuf`.

### User session
An user should first get session secret logging in using
> /users/login

explained below.

After that, every request requiring private data should
append an `UserSession` to maintain security.

## Users

### Create user:
Type: POST
> /userapi/users/createUser

Arguments:  
`username` - username  
`password` - password

Returns `UserSession` object if success.  
`400 Bad Request` if username or password omitted.  
`409 Conflict` if username already exists.

### Login
Type: POST
> /userapi/users/login

Arguments:  
`username` - username  
`password` - password

Returns `UserSession` object if success.  
`403 Forbidden` if username or password is incorrect.

### Check if session correct
Type: POST
> /userapi/sessionValid

Body:
`UserSession` object

Returns same `UserSession` object if session correct.  
`403 Forbidden` if session incorrect.

## Workouts
### !! Important note !!
For all add-methods, you can submit the object without id  
The logic layer will then assign a new id to the object.

The returned object has correct id.

### List workouts (for user):
Type: POST

> /workouts/plans/list/{username}

Arguments:  
`session` - `UserSession` object

Returns `WorkoutPlanList` object if success  
`403 Forbidden` if not authenticated

### View workout (by id)
Type: POST

> /workoutapi/workouts/plans/id/{id}

Arguments:
`session` - `UserSession` object

Returns `WorkoutPlan` object if success  
`403 Forbidden` if not authenticated  
`404 Not Found` if workout not found

### Add workout
Type: POST

> /workoutapi/workouts/plans/add

Body:
`WorkoutPlanAddRequest` object  
(consits of `UserSession` and `WorkoutPlan`)

Returns `'WorkoutPlan'` object (with updated id) if success  
`403 Forbidden` if not authenticated

## Exercises
### List exercises (for user):
Type: POST
> /workoutapi/exercises/list/{username}

Returns `ExerciseList` object if user authenticated,  
`403 Forbidden` if not.

### View exercise (by id):
Type: POST
> /workoutapi/exercises/id/{id}

Arguments:
`session` - `UserSession` object

Returns `Exercise` object  
`403 Forbidden` if not autnenticated  
`404 Not Found` if exercise not found

### Add exercise
Type: POST

> /workoutapi/exercises/add

Body:
`ExerciseAddRequest` object
(consits of `UserSession` and `Exercise`)

Returns `Exercise` object (with updated id) if success  
`403 Forbidden` if not.

## WorkoutLog's
### List workout logs (for user):
Type: POST
> /workoutapi/workouts/logs/list/{username}

Arguments:
`session` - `UserSession` object

Returns `WorkoutLogList` object  
`403 Forbidden` if user not authenticated

### View workout log (by id):
Type: POST
> /workoutapi/workouts/logs/id/{id}

Arguments:
`session` - `UserSession` object

Returns `WorkoutLog` object  
`403 Forbidden` if not authenticated

### Add workout log
Type: POST

> /workoutapi/workouts/logs/add

Body:
`WorkoutLogAddRequest` object
(consits of `UserSession` and `WorkoutLog`)

Returns `WorkoutLog` object (with updated id) if success  
`403 Forbidden` if not authenticated