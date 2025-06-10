package com.alkmanistik.deferred_thread.repository;


import com.alkmanistik.deferred_thread.entity.TaskEntity;
import com.alkmanistik.deferred_thread.entity.enums.TaskStatus;
import com.alkmanistik.deferred_thread.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {


    @Query("SELECT t FROM TaskEntity t WHERE t.category = :category AND t.status = :status " +
            "AND t.scheduledTime <= :now ORDER BY t.scheduledTime ASC")
    List<TaskEntity> findDeferred(
            @Param("category") String category,
            @Param("status") TaskStatus status,
            @Param("now") LocalDateTime now,
            @Param("limit") int limit);
}

