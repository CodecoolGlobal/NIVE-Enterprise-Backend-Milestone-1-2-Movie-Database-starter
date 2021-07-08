# Movie Database

In this task, you will need to implement a few features for the backend of a Movie Database.

You will be provided the description of REST endpoints and the JSON entities.

How you solve the task is entirely up to you, but you mustn't modify or add anything under the `/src/test` directory, 
and your solution must satisfy the provided test cases. 

## JSON Entities
### Movie
example:
```json
{
  "title": "The Million Dollar Hotel",
  "rating": 5.9,
  "director": "Wim Wenders"
}
```

## Endpoints
- `PUT /movie/add`: accepts a Movie JSON object in the request's body and stores it
- `GET /movie/list`: accepts a `director` request parameter, 
  returns the list of the movies directed by the provided director, in descending order, by rating. 
  The list must not contain duplicates.
  ```%info
  Note: Two movies are considered equal, if their titles and directors are the same.
  ```
- `GET /director/list/all`: returns a list of directors, simply as a list of their names, in any order. 
  The list must not contain duplicates. In case there are no directors to list, it should return a list with one item, 
  which should be: `There are no directors yet in the database.`
