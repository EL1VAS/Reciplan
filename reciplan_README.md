# Reciplan

Reciplan is a full-stack recipe planning application designed to help users organize recipes, manage pantry ingredients, and turn available ingredients into practical meal ideas. The project emphasizes clean backend structure, secure API design, and an architecture built with future growth in mind.

## Overview

The application brings together recipe browsing, pantry tracking, shopping list generation, and user authentication in one system. It was created as both a practical solution to a real-world problem and a portfolio project to showcase backend development skills with Java and Spring Boot.

## Features

- User registration and login with JWT-based authentication.
- Pantry management for tracking ingredients already available at home.
- Recipe catalog with categories, difficulty levels, and dietary filters such as vegetarian, lactose-free, and gluten-free.
- Recipe matching based on pantry contents, including support for identifying what can be made immediately and what is nearly possible with a few missing ingredients.
- Shopping list generation for ingredients missing from selected recipes.
- Admin management for recipes, ingredients, categories, and users.
- API documentation through Swagger/OpenAPI and endpoint exploration with Insomnia.

## Architecture

Reciplan is organized into modular packages with clearly separated responsibilities, including areas such as auth, category, ingredient, pantry, recipe, shopping-list, and user. This structure supports maintainability and reflects scalable architecture principles, while advanced production-scaling concerns such as caching, performance profiling, and multi-instance deployment remain planned future improvements.

The backend follows a layered architecture built around controllers, services, repositories, DTOs, configuration, and security components. This separation keeps business logic organized, reduces coupling between layers, and makes the codebase easier to extend over time.

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

### API and Testing Tools

- Swagger / OpenAPI
- Insomnia

### Frontend

- HTML
- CSS
- JavaScript

## Core Domain Model

The project includes entities centered around users, ingredients, recipes, pantry items, recipe ingredients, shopping list items, and categories. The relationship between recipes and ingredients is modeled through a join entity, allowing recipes to contain multiple ingredients while preserving a clear and normalized relational database design.

This model supports pantry-based recipe matching, grouped shopping lists, and admin-managed recipe creation. It also provides a strong foundation for future enhancements such as smarter matching logic, recommendation features, and AI-assisted recipe suggestions.

## Security

Authentication and authorization are built into the core of the application rather than added as an afterthought. Reciplan uses Spring Security with JWT-based authentication and includes support for protected endpoints and admin-only functionality.

## Project Goals

Reciplan was built as both a practical application and a portfolio project. Its goals are to demonstrate:

- Clean REST API design.
- Thoughtful database modeling.
- Secure backend development practices.
- Maintainable and modular project organization.
- Scalable architecture thinking, with a structure prepared for future growth.

## Current Status

The project is actively being developed and refined. The current implementation already covers core backend functionality and project structure, while leaving room for future improvements in testing, performance, deployment strategy, and advanced scalability features.

## Future Improvements

Planned next steps for the project include:

- Expanding automated testing with JUnit and Spring Boot Test, including unit and integration coverage.
- Improving query performance analysis.
- Exploring caching for frequent read operations.
- Strengthening deployment readiness.
- Expanding frontend functionality and user experience.
- Exploring AI-assisted recipe suggestions.

## Notes

This project is currently under active development and focuses primarily on backend architecture, security, and core application features. Setup and run instructions will be added as the project becomes more deployment-ready.
