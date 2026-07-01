# Reciplan

Reciplan is a full-stack recipe planning application built to help users organize recipes, manage pantry ingredients, and turn available ingredients into practical meal ideas. It focuses on clean backend structure, secure API design, and a project architecture that can grow over time.[cite:179][cite:180]

## Overview

The application combines recipe browsing, pantry tracking, shopping list generation, and user authentication in one system. It was designed to demonstrate backend development skills with Java and Spring Boot while keeping the project grounded in a realistic user problem: deciding what to cook with what is already at home.[cite:179][cite:180]

## Features

- User registration and login with JWT-based authentication.[cite:180][cite:184]
- Pantry management for tracking ingredients already available at home.[cite:179][cite:185]
- Recipe catalog with recipe details, categories, difficulty, and dietary filters such as vegetarian, lactose-free, and gluten-free.[cite:179][cite:182]
- Recipe matching based on pantry contents, including support for identifying what can be made or what is almost possible with a few missing ingredients.[cite:179][cite:180][cite:187]
- Shopping list support for missing ingredients needed for selected recipes.[cite:179][cite:185]
- Admin management for recipes, ingredients, categories, and users.[cite:179][cite:180]
- API documentation and testing support through Swagger/OpenAPI, Postman, and Insomnia.[cite:179]

## Architecture

Reciplan is structured with modular packages to keep responsibilities clearly separated, including areas such as auth, category, ingredient, pantry, recipe, shopping-list, and user. This modular structure supports maintainability and reflects scalable architecture principles, even though advanced production-scaling features such as caching, performance profiling, and multi-instance deployment are not yet part of the implementation.[cite:179][cite:181]

The backend follows a layered approach built around controllers, services, repositories, DTOs, configuration, and security components. That separation helps keep business logic organized, reduces coupling between layers, and makes the codebase easier to extend as new features are added.[cite:181][cite:184]

## Tech Stack

### Backend

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- JPA / Hibernate
- Maven
- Lombok
- MapStruct
- SLF4J

### Database

- PostgreSQL

### API and Testing

- Swagger / OpenAPI
- Postman
- Insomnia

### Frontend

- HTML
- CSS
- JavaScript

## Core Domain Model

The project includes entities centered around users, ingredients, recipes, pantry items, recipe ingredients, shopping list items, and categories. The recipe-to-ingredient relationship is modeled through a join entity, which allows recipes to contain many ingredients while preserving structured relationships in a relational database design.[cite:185][cite:186]

This structure supports features such as pantry-based recipe matching, grouped shopping lists, and admin-managed recipe creation. It also provides a strong foundation for future enhancements such as smarter matching logic, recommendation features, or AI-assisted recipe suggestions.[cite:179][cite:180]

## Security

Authentication and authorization are core parts of the application rather than afterthoughts. Reciplan includes token-based authentication with Spring Security and JWT, and the project design already accounts for protected endpoints and admin-only capabilities.[cite:180][cite:184]

## Project Goals

Reciplan was built as both a practical application and a portfolio project. Its goals are to demonstrate:

- Clean REST API design.[cite:180]
- Thoughtful database modeling.[cite:182][cite:186]
- Secure backend development practices.[cite:180][cite:184]
- Maintainable and modular project organization.[cite:181][cite:184]
- Scalable architecture thinking, with a structure prepared for future growth.[cite:179][cite:181]

## Current Status

The project is actively being developed and refined. The existing implementation already covers core backend functionality and project structure, while leaving room for future improvements in performance, deployment strategy, and advanced scalability features.[cite:179][cite:181]

## Future Improvements

Possible next steps for the project include:

- Adding caching for frequent read operations.
- Measuring and improving query performance.
- Expanding the frontend experience.
- Improving deployment readiness.
- Exploring AI-assisted recipe generation or recommendation features.[cite:179][cite:180]

## Local Setup

```bash
git clone <your-repository-url>
cd reciplan
```

Configure the database connection in `application.properties` or `application.yml`, then run the project with Maven:

```bash
./mvnw spring-boot:run
```

If Maven Wrapper is not included, use:

```bash
mvn spring-boot:run
```

## Notes

This repository highlights architecture, security, and backend design decisions as much as end-user functionality. The project is intentionally built with long-term maintainability in mind, aiming to balance practical features with a structure that can evolve over time.[cite:179][cite:181]
