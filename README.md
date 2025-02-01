# Setup

In order to run the project, you need to have java 21 installed.
Before you run the app or tests, you need to start the database and create the required table.
You can do this by starting a Postgres docker container using the provided `docker-compose.yaml` file.

```
docker compose -f ./database/docker-compose.yaml up
```

This will start the Postgres database on port `5432` by default.
If you want to change the port, you can specify `DATABASE_PORT` environment variable to `docker compose` command.

```
DATABASE_PORT=5433 docker compose -f ./database/docker-compose.yaml up
```

You can also specify the other parameters, such as user (`DATABASE_USER`), password (`DATABASE_PASSWORD`) and database name (`DATABASE_NAME`).


To execute the tests, run the `test` gradle task using the provided `gradlew` executable.

```
./gradlew test
```

By default tests will try to connect to database on port `5432`. If you are running tests on a different port, provide a `DATABASE_PORT` environment varialbe while executing test:

```
DATABASE_PORT=5433 ./gradlew test
```
Be sure to use the matching credentials in case you changed them in the previous step. You can use the same environment variables.

To start the server, run `bootRun` gradle task.

```
./gradlew bootRun
```

This will start the server on port `8080` by default. If you want to use a differnt port, you can specify it using `SERVER_PORT` environment variable.

Again, make sure you are using the correct port and credentials.


There are three existing endpoints:

### Create an edge

This endpoint will create an edge between two given nodes:

```
curl -X POST -H "Content-Type: application/json" -d '{"fromNode": 1, "toNode": 2}' "http://localhost:8080/api/v1/edges"
```

The response body contains the created edge:

```
{
    "fromNode": 3,
    "toNode": 6
}
```


### Delete edge

This endpoint will delete the edge between two nodes:

```
curl -X DELETE "http://localhost:8080/api/v1/edges?fromNode=1&toNode=2"
```

The response body in this case is empty if the node is successfully deleted or an error message stating that the edge does not exist:

```
{
    "errors": ["Edge from node 1 to node 2 does not exist"]
}
```

### Get tree

This endpoint will return a tree starting from a given node:

```
curl "http://localhost:8080/api/v1/tree?rootNode=1"
```

Response body:

```
{
    "rootNode": 1,
    "connections": {
        "1": [2, 3],
        "2": [5, 6],
        "3": [6, 7, 8]
    }
}
```