package org.seydaliev.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seydaliev.exception.TaskNotFoundException;
import org.seydaliev.model.Task;
import org.seydaliev.repository.TaskRepository;
import org.seydaliev.service.impl.TaskServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        Assertions.assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(Long.valueOf(1L), result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTaskByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setTitle("New Task");
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("New Task", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() throws TaskNotFoundException {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Task");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        Task updateTask = new Task();
        updateTask.setTitle("Updated Task");
        when(taskRepository.save(any(Task.class))).thenReturn(updateTask);

        Task result = taskService.updateTask(1L, updateTask);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testDeleteById() throws TaskNotFoundException {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteById(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(any(Task.class));
    }
}
