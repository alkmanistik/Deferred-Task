package com.alkmanistik.deferred_thread.repository;


import com.alkmanistik.deferred_thread.entity.TaskEntity;
import com.alkmanistik.deferred_thread.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {


}
