# patient-registry
System Requirements:
-Java 21
-Maven 3.9.9
-Docker Desktop
-MySQL Workbench 8.0 CE (Optional, for DB inspection)

Project Details:
-Project name: patient-registry
-Spring Data JPA
-MySQL JDBC Driver
-Maven
-Spring Boot 3.4.3

Usage:
1) Compile with: mvn clean install
2) Start the containers: docker-compose up --build
3) Get auth token: http://localhost:8080/auth/login
4) Call the desired endpoints. Set the Bearer token with the results of /auth/login. Every endpoint (except for login) requires authentication!

Other useful commands:
-Open terminal and issue the desired command in the project's root directory:
-Maven build:
mvn clean install

-Build and start the containers:
docker-compose up --build

-Build and start the containers (faster, less output in console):
docker-compose up --build -d

-Stop the containers:
docker-compose stop

-Start the containers:
docker-compose up

-Stop and remove containers:
docker-compose down

-Stop and remove containers, delete volume:
docker-compose down -v

MySQL Workbench DB Connection:
-Hostname: 127.0.0.1
-Port: 3306
-Username: patient
-Password: patientpass
-Root Username: root
-Root Password: root

