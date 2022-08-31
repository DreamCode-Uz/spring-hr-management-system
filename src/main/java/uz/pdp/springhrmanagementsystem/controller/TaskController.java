package uz.pdp.springhrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springhrmanagementsystem.payload.TaskDTO;
import uz.pdp.springhrmanagementsystem.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService service;

    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    @Secured({"ROLE_DIRECTOR", "ROLE_MANAGER"})
    public ResponseEntity<?> getAll(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAllTasks(page, size));
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOneTask(@PathVariable("taskId") UUID uuid) {
        return service.getOneTask(uuid);
    }

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllStatus() {
        return service.getAllTasksStatus();
    }

    @PostMapping
    @Secured({"ROLE_DIRECTOR", "ROLE_MANAGER"})
    public ResponseEntity<?> saveTask(@RequestBody @Valid TaskDTO dto, HttpServletRequest request) {
        return service.addTask(dto, request);
    }

    @PutMapping("/{taskId}")
    @Secured({"ROLE_DIRECTOR", "ROLE_MANAGER"})
    public ResponseEntity<?> updateTask(@PathVariable("taskId") UUID uuid, @RequestBody @Valid TaskDTO dto, HttpServletRequest request) {
        return service.editTask(uuid, dto, request);
    }

    @GetMapping("/{taskId}/{encodedEmail}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> activatedTask(@PathVariable("taskId") UUID id, @PathVariable("encodedEmail") String email) {
        return service.activatedTask(id, email);
    }
}
