package org.example.Controller;

import org.example.Entity.Task;
import org.example.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Display the task list (GET request)
    @GetMapping
    public String getTaskList(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new Task()); // For the form
        return "index"; // Renders src/main/resources/templates/index.html
    }

    // Create a new task (POST request)
    @PostMapping("/add")
    public String addTask(@RequestParam String content) {
        taskService.createTask(content);
        return "redirect:/"; // Redirect to refresh the task list
    }

    // Update task content (POST request)
    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable String id, @RequestParam String content) {
        taskService.updateTaskContent(id, content);
        return "redirect:/";
    }

    // Toggle task completion (POST request)
    @PostMapping("/toggle/{id}")
    public String toggleTask(@PathVariable String id) {
        taskService.toggleTaskCompletion(id);
        return "redirect:/";
    }

    // Delete a task (POST request)
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }
}