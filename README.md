# Parking

Учебный веб-сервис имитации парковки: пользователи регистрируются, арендуют
парковочные места, завершают аренду и видят свою статистику. Написан на Spring
Boot с использованием Thymeleaf, PostgreSQL и Flyway.

## Стек

| Слой            | Технология                                  |
|-----------------|---------------------------------------------|
| Язык / JVM      | Java 21                                     |
| Сборка          | Gradle 8, Spring Boot 3.3                   |
| Web / View      | Spring MVC + Thymeleaf + Bootstrap 5        |
| Безопасность    | Spring Security, BCrypt, Spring Session JDBC|
| Хранилище       | PostgreSQL 17                               |
| ORM             | Spring Data JPA / Hibernate 6               |
| Миграции        | Flyway                                      |
| Инфраструктура  | Docker, Docker Compose                      |
| Тесты           | JUnit 5, Mockito                            |

## Возможности

- Регистрация и аутентификация по логину/паролю (пароль хранится BCrypt-хешем).
- Роли пользователей (`ROLE_USER`, `ROLE_ADMIN`).
- Просмотр списка парковочных мест, сортировка по номеру, фильтр по доступности.
- Аренда свободного места одним кликом, JS-секундомер текущей аренды.
- Завершение аренды с подсчётом длительности.
- Личный профиль с историей аренд и ссылкой на карточку места.
- Топ-10 пользователей по количеству арендовок.

## Архитектура

Классическое трёхслойное MVC-приложение:

```
controller/   - HTTP-обработчики, отвечают за разметку ответов
service/      - бизнес-логика, транзакции
repository/   - Spring Data JPA
model/        - JPA-сущности (User, Place, Rent, Role)
dto/          - плоские объекты для view / JPQL constructor expressions
config/       - WebSecurityConfig
exception/    - доменные исключения
```

## Быстрый старт

### Через Docker Compose

Единственная зависимость на хосте - Docker.

```bash
docker compose up --build
```

Compose поднимет PostgreSQL, дождётся её `healthcheck`-готовности и запустит
приложение. UI будет доступен на <http://localhost:8080>.

Пароль БД можно переопределить:

```bash
POSTGRES_PASSWORD=secret docker compose up --build
```

### Локальный запуск

Нужны установленные Java 21 и PostgreSQL 17.

1. Поднимите только БД из compose:

    ```bash
    docker compose up -d postgres-db
    ```

2. Запустите приложение:

    ```bash
    ./gradlew bootRun
    ```

Приложение читает подключение к БД из `application.yml`; любое значение можно
перекрыть стандартной Spring-переменной, например `SPRING_DATASOURCE_URL`,
`SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.

## Конфигурация

Основные параметры - в `src/main/resources/application.yml`:

| Ключ                                  | Значение по умолчанию                       |
|---------------------------------------|---------------------------------------------|
| `spring.datasource.url`               | `jdbc:postgresql://localhost:5432/parkings` |
| `spring.datasource.username`          | `postgres`                                  |
| `spring.datasource.password`          | `123`                                       |
| `spring.jpa.hibernate.ddl-auto`       | `validate` (схему ведёт только Flyway)      |
| `spring.flyway.locations`             | `classpath:db/migrations`                   |
| `spring.sql.init.mode`                | `never`                                     |

## Миграции БД

Flyway применяет миграции из `src/main/resources/db/migrations` при старте
приложения:

- `V1__init_schema.sql` - таблицы, сиквенсы, внешние ключи, индексы.
- `V2__init_places.sql` - стартовые 10 мест и роли `ROLE_USER` / `ROLE_ADMIN`.

История лежит в таблице `flyway_schema_history`. Миграции forward-only: уже
применённые файлы не редактируются - исправления оформляются новым `V3__...`.

Если миграция упала - починить файл и выполнить
```bash
./gradlew flywayRepair
```
(нужен плагин `org.flywaydb.flyway` в `build.gradle`), затем перезапустить
приложение.

## Тесты

```bash
./gradlew test
```

Юнит-тесты (`UserServiceImplementationTest`) изолированы через Mockito - БД не
требуется. Интеграционный `ParkingApplicationTests` грузит `@SpringBootTest` и
ожидает доступную Postgres.

## Маршруты

| Метод | Путь                        | Описание                                   |
|-------|-----------------------------|--------------------------------------------|
| GET   | `/` · `/home`               | Главная, топ-10 пользователей              |
| GET   | `/login` · `/registration`  | Формы входа / регистрации                  |
| POST  | `/registration`             | Регистрация нового пользователя            |
| GET   | `/profile`                  | Профиль + история аренд текущего юзера     |
| GET   | `/places`                   | Список мест                                |
| GET   | `/places/sorted`            | Список мест, отсортированных по номеру     |
| GET   | `/places/filtered`          | Фильтр по доступности (`?available=true`)  |
| GET   | `/places/{placeId}`         | Детали места и все связанные с ним аренды  |
| GET   | `/rents?placeId=…`          | Создать/показать активную аренду места     |
| POST  | `/rents/finish?rentId=…`    | Завершить аренду                           |
| POST  | `/logout`                   | Выйти                                      |

## Структура проекта

```
.
├── docker-compose.yml
├── Dockerfile
├── build.gradle
├── src/
│   ├── main/
│   │   ├── java/ru/bokgosha/parking/
│   │   │   ├── config/           WebSecurityConfig
│   │   │   ├── controller/       PlaceController, RentController, UserController
│   │   │   ├── dto/              PlaceDto, RentDto, UserDto, TopUserDto, ProfileDto
│   │   │   ├── exception/        UserAlreadyExistsException
│   │   │   ├── model/            User, Place, Rent, Role
│   │   │   ├── repository/       *Repository
│   │   │   └── service/          интерфейсы + implementations/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── db/migrations/    V1__init_schema.sql, V2__init_places.sql
│   │       └── templates/        Thymeleaf-шаблоны
│   └── test/java/…
└── README.md
```