# Description of the backend in this project

**Id system:**

To be able to send individual objects from server to client and vice versa we implemented an id system.
Objects in core that needs to be referenced by other objects has an id as identifier.
Exercise class also has a workout-id in order to keep track of which workout it belongs to, and Workout class has a list of exercise-ids to keep track of what exercises it has.
To manage which ids are already used we have a class called IdHandler which gives id to objects and keep track of ids in use.

**Properties:**

In core there is a properties file that has all property values for core files stored.
Here you can edit properties to change system behavior, like lower character limit for username and password, and exercise validation requirements. It is also here id length and legal characters for id can be changed for each object class with id.

**File system:**

When creating a user a new folder with the user object’s username is created in server’s home folder.
In the user folder there is a userData file which is where the user information is stored (username, password), there is a IDs file which is where data about which ids are in use are stored, and there is a Workouts folder, Exercise folder and Histories folder.
When writing new exercises, workouts and history objects to file, it is stored in these folders.
The filename is the same as the id of the object saved. All exercises created are stored in the same folder, even though they don’t necessarily belong to the same workout.
The id system knows which file to load when.

**Server-client:**

ServerService handles adding new exercise, workout and history objects, updating exercise and workout objects, and deleting exercise, workout and history objects on server side, which stores all information.
It has rules (Not to be mistaken fir rules file in server, which is just rules for what is allowed when adding new exercise, workout or history.
Currently object with same workouts with same name, exercise with same name under same workout, and history with same name and date is not allowed) about when to do which actions based on what id given object has, which is used to validate if action is legal or not.
It also handles getting exercise, workout and history objects on request, as well as getting a hash-map for mapping between id and name (name and date for history) for given object.
ServerController handles communication with client side through http.

**ClientService:**

Handles communication with server side through http.

**ClientController:**

Handles communication between client socket and fxui (gui).
It also makes sure that hash-maps for objects are updated when needed after every action. It also manages what to send in correct order when creating a workout with with exercises.
