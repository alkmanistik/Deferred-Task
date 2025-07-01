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
DB_URL=jdbc:postgresql://localhost:5432/deferredThread
DB_USERNAME=/*Поменять на свой*/
DB_PASSWORD=/*Поменять на свой*/
DB_NAME=deferredThread
GRAFANA_USER=/*Поменять на свой*/
GRAFANA_PASSWORD=/*Поменять на свой*/
```

## Base URL
`http://localhost:8080/api/v1`

## URL для мониторинга
`http://localhost:3000/`

## 🚀 Точки входа

### URL документации
`http://localhost:8080/swagger-ui/index.html`

### 1. TaskController
Создание задачи
**Endpoint**: `POST /tasks/schedule`
**Body**:
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
**Endpoint**: `PATCH /tasks/cancel/2`
**Body**:
```json
{
    "category": "email"
}
```
### 2. WorkerController
Старт Worker
**Endpoint**: `POST /workers/start`
**Body**:
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
```
├── controller # Контроллеры API
├── data # Все классы данных
│ ├── entity # Сущности базы данных
│ ├── model # Модели данных (DTO)
│ ├── enums # Перечисления
│ ├── anotation # Аннотации
├── exception # Кастомные исключения
├── repository # Репозитории для работы с БД
├── request # Классы запросов API
├── response # Классы ответов API
├── service # Бизнес-логика сервисов
├── task # Реализации задач
└── DeferredThreadApplication.java # Главный класс приложения
```
### Task
Пример создания своей собственной задачи
- Обязательное наследования из Task
- Аннотация TaskParams должна принимать только строгие параметры, которые должны быть 100%.
```java
import com.alkmanistik.deferred_thread.data.anotation.TaskParams;

import java.util.Map;

@TaskParams(required = {"firstParam", "secondParam"})
public class CustomTask extends Task {

    public CustomTask(Map<String, Object> map) {
        super(map);
    }

    @Override
    protected void execute(Map<String, Object> params) throws InterruptedException {
        // Некоторая логика
        // Пример работы с параметрами
        System.out.println(params.get("firstParam") + " " + params.get("secondParam"));
    }

}

```
### Фичи проекта
1. База данных
- Динамические таблицы: Для каждой категории задач автоматически создается отдельная таблица
- Формат имен таблиц: tasks_<category> (например, tasks_email)
2. Worker
- Получает задачи из БД по категории
- Обрабатывает в многопоточном режиме
- Поддерживает экспоненциальный бекофф для повторных попыток
3. Validator
- Валидация входных параметров
- Не возможность создать и занести в базу заведомо ошибочную задачу без параметров
4. PreDestroy
- Завершение программы гарантирует закрытие всех Worker
- Задачи, которые держит программа обратно вернутся в базу, как запланированные, если их не успели обработать
5. Grafana
- Мониторинг ресурсов программы в графическом виде через Prometheus (использована диаграмма 4701)
6. Unit-Test
- Написаны тесты на бизнес-логику и контроллеры `./gradlew test`
