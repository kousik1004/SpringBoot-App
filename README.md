# Qualifier Java Submission

This project is my solution for the **Bajaj Finserv Health Java Qualifier**.

## 🚀 Features
- Built with **Spring Boot 3.x** and **Java 17**
- Uses **WebClient** to call required APIs
- Auto-start flow using `ApplicationRunner` (no controller needed)
- Reads candidate details from `application.yml`
- Decides SQL question based on **last two digits of regNo**
- Loads final SQL from `src/main/resources/answer.sql`
- Submits SQL answer with **Authorization: JWT accessToken**

## 🛠️ How to Build
```bash
./mvnw clean package -DskipTests

## How to Run
java -jar target/qualifier-java-1.0.0.jar
