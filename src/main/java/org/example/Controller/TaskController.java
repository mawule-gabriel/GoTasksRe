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
        System.out.println("Tasks fetched for rendering: " + tasks);
        int activeTasks = 0;
        int completedTasks = 0;
        if (tasks != null) {
            for (Task task : tasks) {
                if (task.isCompleted()) {
                    completedTasks++;
                } else {
                    activeTasks++;
                }
            }
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("activeTasks", activeTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("newTask", new Task());
        return "index";
    }

    @PostMapping("/add")
    public String addTask(@RequestParam String content) {
        if (content != null && !content.trim().isEmpty()) {
            System.out.println("Adding task with content: " + content);
            taskService.createTask(content);
        }
        return "redirect:/";
    }

    // Update task content (POST request)
    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable String id, @RequestParam String content) {
        taskService.updateTaskContent(id, content);
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTask(@PathVariable String id) {
        System.out.println("Toggling task with ID: " + id);
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