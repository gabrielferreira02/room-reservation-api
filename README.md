# Room reservation API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

This api implements a simple room reservation system using jwt authentication and documentation with swagger

To use the application you need to clone the repository
```bash
  git clone https://github.com/gabrielferreira02/room-reservation-api.git
```
Go to the directory of the project
```bash
  cd room-reservation-api
```
Use maven to generate the .jar file
```bash
  mvn clean install
```
Now you can start the application using docker. First you need to build the images
```bash
  docker-compose build
```
Then you can start the api using the following command
```bash
  docker-compose up -d
```

With the api started you can see the documentation in this endpoint
```bash
  http://localhost:8080/swagger-ui.html
```

All users in the database has a password "12345678". So to loggging in an user you can list the users and put the username and the password to authenticate

To test the application use maven with the following command
```bash
  mvn test
```
