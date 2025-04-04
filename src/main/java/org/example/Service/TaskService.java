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

    public Task createTask(String content) {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setContent(content);
        task.setCompleted(false);
        System.out.println("Creating new task - ID: " + task.getId() + ", Content: " + content);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        Iterable<Task> tasks = taskRepository.findAll();
        List<Task> taskList = new ArrayList<>();
        tasks.forEach(taskList::add);
        return taskList;
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public Optional<Task> updateTaskContent(String id, String newContent) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            System.out.println("Updating task content - ID: " + id + ", New Content: " + newContent);
            task.setContent(newContent);
            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }

    public Optional<Task> toggleTaskCompletion(String id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            boolean currentStatus = task.isCompleted();
            System.out.println("Before toggle - Task ID: " + id + ", Completed: " + currentStatus);
            task.setCompleted(!currentStatus);
            Task savedTask = taskRepository.save(task);
            System.out.println("After toggle - Task ID: " + id + ", Completed: " + savedTask.isCompleted());
            // Fetch directly to verify
            Optional<Task> verifiedTask = taskRepository.findById(id);
            System.out.println("Verified fetch after toggle: " + verifiedTask.orElse(null));
            return Optional.of(savedTask);
        }
        System.out.println("Task not found for ID: " + id);
        return Optional.empty();
    }

    public boolean deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}