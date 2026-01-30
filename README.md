# Kantor Backend

REST API dla aplikacji mobilnej do wymiany walut. System zarządza kontami użytkowników, portfelami walutowymi oraz transakcjami wymiany opartymi na aktualnych kursach z NBP API.

## Technologie

- Java 17
- Spring Boot 3.5.9
- Spring Security + JWT
- PostgreSQL
- Maven

## Konfiguracja

Utwórz bazę danych PostgreSQL i skonfiguruj `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/kantor
spring.datasource.username=postgres
spring.datasource.password=postgres
jwt.secret=your-secret-key
```

## Uruchomienie
```bash
mvn clean install
mvn spring-boot:run
```

Aplikacja wystartuje na porcie 8080.

## Dokumentacja API

Szczegółowa dokumentacja znajduje się w pliku [KANTOR_API_DOCUMENTATION.txt](KANTOR_API_DOCUMENTATION.txt)

## Mobile App

[kantor-mobile](https://github.com/milmanart/kantor-mobile)