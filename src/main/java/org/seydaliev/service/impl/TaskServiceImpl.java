package org.seydaliev.service.impl;

import org.seydaliev.exception.TaskNotFoundException;
import org.seydaliev.model.Task;
import org.seydaliev.repository.TaskRepository;
import org.seydaliev.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Transactional
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    @Transactional
    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + id));
    }
    @Transactional
    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    @Transactional
    @Override
    public Task updateTask(Long id, Task updateTask) throws TaskNotFoundException {
        Task task = getById(id);
        if (task != null) {
            task.setTitle(updateTask.getTitle());
            task.setDescription(updateTask.getDescription());
            task.setDueDate(updateTask.getDueDate());
            task.setCompleted(updateTask.isCompleted());
            return taskRepository.save(task);
        }
        throw new TaskNotFoundException("Task not found with id " + id);
    }
    @Transactional
    @Override
    public void deleteById(Long id) {
        Task task = getById(id);
        taskRepository.delete(task);
    }
}
