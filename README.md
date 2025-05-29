# ThesisManagement

A web application for managing academic theses, built with Spring Framework, Jakarta EE, and supporting tools. The project is structured as a Maven-based Java web application.

## Features

- User management and authentication with Spring Security
- Thesis submission and management
- Integration with MySQL database for data persistence
- MVC architecture via Spring WebMVC
- Templating with Thymeleaf
- PDF export functionality (using Flying Saucer)
- Email notifications (SMTP support)
- OpenAPI documentation (Swagger UI integration)
- Cloudinary integration for media uploads

## Project Structure

```
ThesisManagement/
│
├── SpringThesis/
│   ├── pom.xml                 # Maven build configuration
│   ├── src/                    # Main source code
│   │   └── main/
│   │       ├── java/           # Java code
│   │       └── resources/
│   │           └── application.properties  # App configuration
│   ├── nb-configuration.xml    # NetBeans configuration
│   └── ...
├── thesis.txt                  # (Likely project documentation)
└── .gitignore
```

## Getting Started

### Prerequisites

- Java 22 or later
- Maven 3.9+
- MySQL server

### Setup

1. **Clone the repository**
   ```sh
   git clone https://github.com/HTToann/ThesisManagement.git
   cd ThesisManagement/SpringThesis
   ```

2. **Configure the database**
   - Set your MySQL credentials and database details in `src/main/resources/application.properties`.
   - Example:
     ```
     spring.datasource.url=jdbc:mysql://localhost:3306/thesisdb
     spring.datasource.username=your_mysql_user
     spring.datasource.password=your_mysql_password
     ```

3. **Configure mail and third-party services**
   - Update email and Cloudinary settings as needed in `application.properties`.

4. **Build the project**
   ```sh
   mvn clean install
   ```

5. **Run the application**
   - Deploy the generated WAR file to your preferred servlet container (e.g., Tomcat, Payara, etc.)
   - Or run directly if supported.

6. **Access API Documentation**
   - Visit `http://localhost:8080/swagger-ui/` for OpenAPI/Swagger docs.

## Main Dependencies

- Jakarta EE
- Spring WebMVC, Spring Security, Spring ORM, Thymeleaf
- Hibernate ORM
- MySQL Connector/J
- Jackson (JSON processing)
- Cloudinary SDK
- Springdoc OpenAPI & Swagger UI

## Contribution

Contributions are welcome! Fork this repo and submit a pull request.

## License

This project is for academic purposes. License can be specified here.
