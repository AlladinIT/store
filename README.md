# Movie rental store

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- [PostgreSQL](https://www.postgresql.org/download/)


## Running the application locally
Before running the application, make sure to create a database in PostgresSQL.
You can change application properties in `store.src.main.resources.application.properties`

```
spring.datasource.url=jdbc:postgresql://localhost:5432/"database_name"
spring.datasource.username="your_username"
spring.datasource.password="password_for_the_user"
```

## Usage
When the project is running all endpoints are available on:
http://localhost:8080/swagger-ui.html

JavaDoc is available at `store.docs`


