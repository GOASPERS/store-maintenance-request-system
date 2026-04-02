# Store Maintenance Request System

Store Maintenance Request System — backend-only pet-проект на Java и Spring Boot для управления заявками на техническое обслуживание магазинов и коммерческих объектов.

Проект моделирует реалистичный внутренний workflow обработки заявок: пользователи могут создавать заявки, привязывать их к магазинам и оборудованию, назначать инженеров, оставлять комментарии, менять статусы и отслеживать историю изменений через защищенное JWT-based REST API.

Это pet-проект, вдохновленный реальными внутренними сервисными workflow, которые встречались мне в компании с эксплуатацией, техобслуживанием и обслуживанием коммерческих объектов. Репозиторий является самостоятельной учебно-практической реализацией, а не копией какого-либо корпоративного продукта.

## Зачем этот проект

Цель проекта — показать backend-навыки на примере правдоподобного бизнес-сценария, а не абстрактного CRUD над случайными сущностями.

В реализации сделан упор на вещи, которые часто встречаются во внутренних бизнес-системах:
- workflow заявок
- роли пользователей и доступы
- REST API
- реляционная модель данных
- бизнес-правила
- JWT-аутентификация
- комментарии и история статусов
- локальный запуск через Docker


## Стек технологий

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Hibernate ORM
- Spring Security
- JWT
- PostgreSQL
- Flyway
- Maven
- Docker и Docker Compose
- JUnit 5
- Mockito
- Springdoc OpenAPI / Swagger

## Основные возможности

- регистрация и логин пользователей через JWT
- CRUD для магазинов
- CRUD для оборудования
- CRUD для заявок на обслуживание
- фильтрация заявок по статусу, магазину и назначенному инженеру
- комментарии к заявкам
- отдельный endpoint для смены статуса
- автоматическое создание истории статусов
- базовые бизнес-правила для назначения инженера и request workflow
- Swagger / OpenAPI
- запуск через Docker Compose
- dev seed data для быстрого локального демо
- unit-тесты сервисного слоя на ключевые сценарии

## Бизнес-сценарий

Приложение моделирует внутренний процесс обработки заявок на обслуживание магазинов и коммерческих объектов.

Пример сценария:
1. сотрудник создает заявку на обслуживание;
2. заявка привязывается к магазину и, при необходимости, к конкретному оборудованию;
3. ответственный пользователь назначает инженера;
4. участники процесса оставляют комментарии по ходу работы;
5. статус заявки меняется по контролируемому workflow;
6. каждая успешная смена статуса сохраняется в истории.

## Роли

В системе предусмотрены следующие роли:
- ADMIN
- DISPATCHER
- ENGINEER
- MANAGER

Текущее поведение ролей:
- все endpoint’ы, кроме auth, требуют аутентификацию;
- данные о роли доступны в security context;
- правила назначения инженеров проверяются внутри workflow заявок.

## Основные сущности

User
- id
- fullName
- email
- password
- role
- active
- createdAt
- updatedAt

Store
- id
- name
- address
- city
- contactPerson
- contactPhone
- createdAt
- updatedAt

Equipment
- id
- name
- type
- serialNumber
- store
- createdAt
- updatedAt

MaintenanceRequest
- id
- title
- description
- priority
- status
- store
- equipment
- createdBy
- assignedEngineer
- dueDate
- createdAt
- updatedAt

Comment
- id
- maintenanceRequest
- author
- text
- createdAt

StatusHistory
- id
- maintenanceRequest
- oldStatus
- newStatus
- changedBy
- changedAt

## Архитектура

Проект использует классическую layered backend-структуру:
- controller — HTTP / REST API слой
- service — бизнес-логика
- repository — работа с БД
- entity — внутренняя persistence-модель
- dto — request / response объекты API
- mapper — преобразование entity ↔ dto
- security — JWT и Spring Security
- exception — пользовательские исключения
- config — конфигурация приложения

Такой подход делает проект читаемым, практичным и близким к тому, как обычно выглядит junior backend-кодовая база.

## API Overview

Auth:
- POST /api/auth/register
- POST /api/auth/login

Stores:
- POST /api/stores
- GET /api/stores
- GET /api/stores/{id}
- PUT /api/stores/{id}
- DELETE /api/stores/{id}

Equipment:
- POST /api/equipment
- GET /api/equipment
- GET /api/equipment/{id}
- PUT /api/equipment/{id}
- DELETE /api/equipment/{id}
- GET /api/equipment/store/{storeId}

Maintenance Requests:
- POST /api/requests
- GET /api/requests
- GET /api/requests/{id}
- PUT /api/requests/{id}
- DELETE /api/requests/{id}

Фильтры заявок:
- GET /api/requests?status=...
- GET /api/requests?storeId=...
- GET /api/requests?assignedEngineerId=...

Comments:
- POST /api/requests/{id}/comments
- GET /api/requests/{id}/comments

Status / History:
- PATCH /api/requests/{id}/status
- GET /api/requests/{id}/history

## Аутентификация

Проект использует JWT-аутентификацию.

Как это работает:
1. пользователь регистрируется или логинится;
2. backend выдает JWT token;
3. клиент отправляет токен в заголовке:
   Authorization: Bearer <your_token>
4. backend валидирует токен и определяет текущего пользователя;
5. защищенные endpoint’ы становятся доступны только авторизованным пользователям.

## Бизнес-правила по заявкам

Текущие workflow-правила включают:

Назначение инженера:
- только ADMIN, DISPATCHER и MANAGER могут назначать или переназначать инженера;
- assignedEngineerId должен указывать на пользователя с ролью ENGINEER.

Изменение статуса:
- новый статус должен отличаться от текущего;
- заявку нельзя перевести в IN_PROGRESS без назначенного инженера;
- переходы статусов валидируются по текущей MVP-карте:

- NEW -> ASSIGNED, CANCELLED
- ASSIGNED -> IN_PROGRESS, CANCELLED
- IN_PROGRESS -> WAITING_PARTS, DONE, CANCELLED
- WAITING_PARTS -> IN_PROGRESS, CANCELLED
- DONE -> переходов нет
- CANCELLED -> переходов нет

История статусов:
- каждая успешная смена статуса автоматически создает запись в StatusHistory.

## Локальный запуск

1. Поднять PostgreSQL

Можно использовать локальный PostgreSQL или поднять только БД через Docker Compose:

docker compose up -d postgres

2. Проверить настройки приложения

Базовая локальная конфигурация уже есть в src/main/resources/application.yml:
- БД: maintenance_requests_db
- пользователь: postgres
- пароль: postgres
- порт приложения: 8080

3. Запустить приложение

mvn spring-boot:run

После старта:
- Flyway применит миграции;
- backend будет доступен по адресу http://localhost:8080

## Запуск через Docker Compose

docker compose up --build

После запуска:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## Swagger / OpenAPI

Swagger endpoint’ы открыты публично:
- /v3/api-docs
- /v3/api-docs/**
- /swagger-ui.html
- /swagger-ui/**

Swagger UI:
http://localhost:8080/swagger-ui.html

OpenAPI docs:
http://localhost:8080/api-docs

После логина нажмите Authorize в Swagger UI и вставьте:

Bearer <your-jwt-token>

## Demo Accounts

Demo seed data создается автоматически при старте приложения только если база данных пустая.

Доступные demo-пользователи:
- admin@demo.local / Admin123!
- dispatcher@demo.local / Dispatcher123!
- engineer@demo.local / Engineer123!
- manager@demo.local / Manager123!

Seed data также включает:
- 2 магазина
- 2 записи оборудования
- 2 примерные заявки на обслуживание

## Пример auth flow

Вариант 1. Войти под demo-аккаунтом

POST /api/auth/login
Content-Type: application/json

{
  "email": "manager@demo.local",
  "password": "Manager123!"
}

Вариант 2. Зарегистрировать нового пользователя

POST /api/auth/register
Content-Type: application/json

{
  "fullName": "Test Manager",
  "email": "test.manager@example.com",
  "password": "Password123!"
}

Дальше:
1. скопировать token из ответа;
2. добавлять его в запросы как Bearer <token>;
3. вызывать защищенные endpoint’ы, например:
   - GET /api/stores
   - POST /api/equipment
   - POST /api/requests
   - PATCH /api/requests/{id}/status

## Тесты

В проекте есть unit-тесты сервисного слоя на JUnit 5 и Mockito.

Покрытые сервисы:
- StoreServiceImpl
- EquipmentServiceImpl
- MaintenanceRequestServiceImpl

Тесты покрывают важные сценарии, включая:
- успешное CRUD-поведение
- обработку отсутствующих сущностей
- создание заявок
- валидацию переходов статусов
- невалидные изменения статуса
- валидацию назначения инженера

Это делает тестовый слой практичным и полезным именно для текущего MVP.

## Notes по проекту

- Backend-only проект
- Монолит по дизайну
- Без frontend
- Без refresh tokens
- Без OAuth
- Без microservices
- Без сложного deployment setup
- Основной фокус: realistic CRUD + workflow + auth logic

## Возможные улучшения

В будущем проект можно развивать в сторону: - глобальной обработки ошибок 
через @RestControllerAdvice - более явных role-based ограничений на 
endpoint’ы - integration tests - пагинации и сортировки - расширенного 
поиска и фильтрации - soft delete - file attachments - refresh token flow 
- CI/CD pipeline

