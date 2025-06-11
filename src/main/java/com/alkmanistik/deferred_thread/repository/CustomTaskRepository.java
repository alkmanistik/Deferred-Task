package com.alkmanistik.deferred_thread.repository;

import com.alkmanistik.deferred_thread.data.entity.TaskEntity;
import com.alkmanistik.deferred_thread.data.enums.TaskStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CustomTaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Создает таблицу для категории, если её нет
     */
    public void createTableIfNotExists(String category) {
        String tableName = "tasks_" + category.toLowerCase();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "task_class_name VARCHAR(255) NOT NULL," +
                "task_params_json TEXT," +
                "scheduled_time TIMESTAMP NOT NULL," +
                "status VARCHAR(50) NOT NULL," +
                "created_at TIMESTAMP NOT NULL," +
                "updated_at TIMESTAMP NOT NULL" +
                ")";
        jdbcTemplate.execute(sql);
    }

    public long insert(TaskEntity task) {
        String tableName = "tasks_" + task.getCategory().toLowerCase();
        createTableIfNotExists(task.getCategory());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO " + tableName +
                " (task_class_name, task_params_json, scheduled_time, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                task.getTaskClassName(),
                task.getTaskParamsJson(),
                Timestamp.valueOf(task.getScheduledTime()),
                "SCHEDULED",
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()));

        return id;
    }

    public boolean cancel(String category, long taskId) {
        String tableName = "tasks_" + category.toLowerCase();

        String sql = "UPDATE " + tableName +
                " SET status = ?, updated_at = ? " +
                "WHERE id = ? AND status = ?";

        int rowsUpdated = jdbcTemplate.update(
                sql,
                TaskStatus.CANCELLED.name(),
                Timestamp.valueOf(LocalDateTime.now()),
                taskId,
                TaskStatus.SCHEDULED.name()
        );

        return rowsUpdated > 0;
    }


    public void save(TaskEntity task) {
        String tableName = "tasks_" + task.getCategory().toLowerCase();
        createTableIfNotExists(task.getCategory());

        if (task.getId() == null) {
            // Вставка новой задачи
            String insertSql = "INSERT INTO " + tableName +
                    " (task_class_name, task_params_json, scheduled_time, status, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

            Long id = jdbcTemplate.queryForObject(insertSql, Long.class,
                    task.getTaskClassName(),
                    task.getTaskParamsJson(),
                    Timestamp.valueOf(task.getScheduledTime()),
                    task.getStatus().name(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now()));

            task.setId(id);
        } else {
            // Обновление существующей задачи
            String updateSql = "UPDATE " + tableName + " SET " +
                    "task_class_name = ?, " +
                    "task_params_json = ?, " +
                    "scheduled_time = ?, " +
                    "status = ?, " +
                    "updated_at = ? " +
                    "WHERE id = ?";

            jdbcTemplate.update(updateSql,
                    task.getTaskClassName(),
                    task.getTaskParamsJson(),
                    Timestamp.valueOf(task.getScheduledTime()),
                    task.getStatus().name(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    task.getId());
        }
    }

    public List<TaskEntity> findDeferred(String category, TaskStatus taskStatus, LocalDateTime now, int tasksNumber) {
        String tableName = "tasks_" + category.toLowerCase();

        String sql = "SELECT * FROM " + tableName +
                " WHERE status = ? AND scheduled_time <= ? " +
                "ORDER BY scheduled_time ASC LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TaskEntity task = new TaskEntity();
            task.setId(rs.getLong("id"));
            task.setCategory(category);
            task.setTaskClassName(rs.getString("task_class_name"));
            task.setTaskParamsJson(rs.getString("task_params_json"));
            task.setScheduledTime(rs.getTimestamp("scheduled_time").toLocalDateTime());
            task.setStatus(TaskStatus.valueOf(rs.getString("status")));
            task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            task.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return task;
        }, taskStatus.name(), Timestamp.valueOf(now), tasksNumber);
    }
}