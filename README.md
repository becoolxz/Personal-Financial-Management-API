# Personal-Financial-Management-API
[![Build Status](https://travis-ci.com/becoolxz/Personal-Financial-Management-API.svg?branch=master)](https://travis-ci.com/becoolxz/Personal-Financial-Management-API)

[![codecov](https://codecov.io/gh/becoolxz/Personal-Financial-Management-API/branch/master/graph/badge.svg)](https://codecov.io/gh/becoolxz/Personal-Financial-Management-API)

The motivation of the project is to use all the technologies and concepts that I have learned so far.

The application has as functional requisites:

 - User CRUD
 - Category CRUD
 - User Login

-------------------------------------------------------------------------------------------------------------

## Requirements

For building and running the application you need:

- JDK 1.8
- Maven 3
- MySQL

## Setup

- Create a database with name: personalFinancialManagement or change the name in the properties file: `application-dev.properties` 

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the   
`br.com.lucas.study.personalfinancialmanagementapi.PersonalFinancialManagementApiApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins.html#build-tool-plugins-maven-plugin) like so:

`mvn spring-boot:run`
