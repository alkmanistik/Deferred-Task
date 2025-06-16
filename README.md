# Deferred Task Processor API

## Введение
REST API для задач с отложенными вызовами. Система позволяет планировать задачи на выполнение в будущем, автоматически обрабатывать их с возможностью повторных попыток и управлять worker'ами для обработки задач.

## 📦 Установка и запуск

### Требования
- Java 21+
- Docker и Docker Compose
- Gradle

### Файл переменных окружения (.env)
```
DB_URL=
DB_PASSWORD=
DB_USERNAME=
DB_NAME=
```

## Base URL
`http://localhost:8080/api/v1`

## 🚀 Точки входа

### 1. TaskController
Создание задачи
**Endpoint**: `POST /tasks/schedule`
**Request**:
```json
{
    "category": "email",
    "taskClassName": "com.example.EmailProcessingTask",
    "taskParams": {
        "email": "user@example.com",
        "message": "Hello World"
    },
    "scheduledTime": "2025-06-10T15:30:00"
}
```
Отмена задачи
**Endpoint**: `POST tasks/cancel/2`
**Request**:
```json
{
    "category": "email"
}
```
### 2 WorkerController
Старт Worker
**Endpoint**: `POST /workers/start`
```json
{
    "category": "email",
    "threadNumber": 2,
    "tasksNumber": 5,
    "retryCount": 2,
    "retryBase": 4.0
}
```
Уничтожение Worker
**Endpoint**: `POST /workers/stop/{category}`
## 🏗️ Архитектура системы
### Структура
- controller # Контроллеры API
- data # Все классы данных
    - entity # Сущности базы данных
    - model # Модели данных (DTO)
    - enums # Перечисления
    - anotation # Аннотации
- exception # Кастомные исключения
- repository # Репозитории для работы с БД
- request # Классы запросов API
- response # Классы ответов API
- service # Бизнес-логика сервисов
- task # Реализации задач
- DeferredThreadApplication.java # Главный класс приложения
### Особенности
1. База данных
- Динамические таблицы: Для каждой категории задач автоматически создается отдельная таблица
- Формат имен таблиц: tasks_<category> (например, tasks_email)
2. Worker
- Получает задачи из БД по категории
- Обрабатывает в многопоточном режиме
- Поддерживает экспоненциальный бекофф для повторных попыток
