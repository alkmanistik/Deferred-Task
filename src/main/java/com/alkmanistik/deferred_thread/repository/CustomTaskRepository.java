package com.alkmanistik.deferred_thread.repository;

import com.alkmanistik.deferred_thread.data.entity.TaskEntity;
import com.alkmanistik.deferred_thread.data.enums.TaskStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomTaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public CustomTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTableIfNotExists(String category) {
        String tableName = "tasks_" + category.toLowerCase();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "task_class_name VARCHAR(255) NOT NULL," +
                "task_params_json TEXT," +
                "scheduled_time TIMESTAMP NOT NULL," +
                "status VARCHAR(50) NOT NULL," +
                "retry_count INTEGER NOT NULL DEFAULT 0," +
                "created_at TIMESTAMP NOT NULL," +
                "updated_at TIMESTAMP NOT NULL" +
                ")";
        jdbcTemplate.execute(sql);
    }

    public long insert(TaskEntity task) {
        String tableName = "tasks_" + task.getCategory().toLowerCase();
        createTableIfNotExists(task.getCategory());

        String sql = "INSERT INTO " + tableName +
                " (task_class_name, task_params_json, scheduled_time, status, retry_count, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        return jdbcTemplate.queryForObject(sql, Long.class,
                task.getTaskClassName(),
                task.getTaskParamsJson(),
                Timestamp.valueOf(task.getScheduledTime()),
                task.getStatus().name(),
                task.getRetryCount(),
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()));
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
            String insertSql = "INSERT INTO " + tableName +
                    " (task_class_name, task_params_json, scheduled_time, status, retry_count, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

            Long id = jdbcTemplate.queryForObject(insertSql, Long.class,
                    task.getTaskClassName(),
                    task.getTaskParamsJson(),
                    Timestamp.valueOf(task.getScheduledTime()),
                    task.getStatus().name(),
                    task.getRetryCount(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now()));

            task.setId(id);
        } else {
            String updateSql = "UPDATE " + tableName + " SET " +
                    "task_class_name = ?, " +
                    "task_params_json = ?, " +
                    "scheduled_time = ?, " +
                    "status = ?, " +
                    "retry_count = ?, " +
                    "updated_at = ? " +
                    "WHERE id = ?";

            jdbcTemplate.update(updateSql,
                    task.getTaskClassName(),
                    task.getTaskParamsJson(),
                    Timestamp.valueOf(task.getScheduledTime()),
                    task.getStatus().name(),
                    task.getRetryCount(),
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
            task.setRetryCount(rs.getInt("retry_count"));
            task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            task.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return task;
        }, taskStatus.name(), Timestamp.valueOf(now), tasksNumber);
    }

    public Optional<TaskEntity> findById(String category, long taskId) {
        String tableName = "tasks_" + category.toLowerCase();
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            TaskEntity task = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                TaskEntity t = new TaskEntity();
                t.setId(rs.getLong("id"));
                t.setCategory(category);
                t.setTaskClassName(rs.getString("task_class_name"));
                t.setTaskParamsJson(rs.getString("task_params_json"));
                t.setScheduledTime(rs.getTimestamp("scheduled_time").toLocalDateTime());
                t.setStatus(TaskStatus.valueOf(rs.getString("status")));
                t.setRetryCount(rs.getInt("retry_count"));
                t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                t.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                return t;
            }, taskId);
            return Optional.ofNullable(task);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}