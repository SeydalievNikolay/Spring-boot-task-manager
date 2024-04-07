package org.seydaliev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seydaliev.SpringBootTaskManager;
import org.seydaliev.model.Task;
import org.seydaliev.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTaskById() throws Exception {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        when(taskService.getById(id)).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void testCreateTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("new task");
        newTask.setId(1L);
        when(taskService.createTask(any(Task.class))).thenReturn(newTask);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Long id = 1L;
        Task updateTask = new Task();
        updateTask.setTitle("Updated Task");
        updateTask.setId(id);
        when(taskService.updateTask(eq(id),any(Task.class))).thenReturn(updateTask);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        Long id = 1L;
        doNothing().when(taskService).deleteById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/" + id))
                .andExpect(status().isOk());
    }
}
