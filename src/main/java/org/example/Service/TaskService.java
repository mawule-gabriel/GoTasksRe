package org.example.Service;

import org.example.Entity.Task;
import org.example.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create a new task
    public Task createTask(String content) {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setContent(content);
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        Iterable<Task> tasks = taskRepository.findAll();
        List<Task> taskList = new ArrayList<>();
        tasks.forEach(taskList::add);
        return taskList;
    }

    // Get a task by ID
    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    // Update task content
    public Optional<Task> updateTaskContent(String id, String newContent) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setContent(newContent);
            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }

    // Toggle task completion status
    public Optional<Task> toggleTaskCompletion(String id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(!task.isCompleted());
            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }

    // Delete a task
    public boolean deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}