package uz.pdp.springhrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springhrmanagementsystem.service.TaskService;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService service;

    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAllTasks(page, size));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAllStatus() {
        return service.getAllTasksStatus();
    }

//    @PostMapping
//    public ResponseEntity<?> save(@RequestBody) {
//
//    }
}
