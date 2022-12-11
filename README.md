# Quiz Tournament

- [About](#about)
- [Features](#features)
- [Build & run locally](#build--run-locally)
  - [From command line](#from-command-line) 
- [Further plans](#further-plans)

## About

Quiz Tournament is a web application that allows you to solve and create quizzes, to have thought-provoking, thrilling, touching or hilarious discussions in the comments section and to have fun in general.

<p align="center">
	<img src="https://user-images.githubusercontent.com/45975127/185566384-adb08e6a-c3ab-4305-bea6-281ea04367b7.PNG" width="600" height="350">
</p>

<p align="center">
	<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"> 
	<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=Spring-Boot&logoColor=white"> 
	<img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"> 
	<img src="https://img.shields.io/badge/Thymeleaf-005F0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white">
</p>

## Features
- Anonymous
  - Solve quizzes
  - View comments
  - create profile
- Logged in
  - Store solved quizzes
  - create, update, and delete your own quizzes
  - write comments
  - update profile

## Build & run locally

To build and run you should already have a database and provide the following variables (in application.properties) for connection:
- spring.datasource.url
- spring.datasource.username
- spring.datasource.password

Also, you will need an S3-compatible storage for storing images. Configure it with these variables: 

- r2.accessKey
- r2.secretKey
- r2.bucketName
- r2.baseUrl *url for creating client bean*
- r2.publicUrl *public bucket url*

You can create application-local.properties which is included in gitignore and specify variables there.
Then set jvm program argument:
```
--spring.profiles.active=local
```

### From command line

To build jar you can specify variables in gradle.properites and run from project's root
```
./gradlew clean build
```
To execute output jar you can run from project's root
```
java -jar ./build/libs/Quiz_Tournament-0.0.1-SNAPSHOT.jar
```

## Further plans
- Add API documentation
- Tags cloud
- Add tournament mode
- Make customizable win/lose feedback
- Add localization
