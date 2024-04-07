package org.seydaliev.service;

import org.seydaliev.exception.TaskNotFoundException;
import org.seydaliev.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();

    Task getById(Long id);

    Task createTask(Task task);

    Task updateTask(Long id, Task updateTask) throws TaskNotFoundException;

    void deleteById(Long id);
}
