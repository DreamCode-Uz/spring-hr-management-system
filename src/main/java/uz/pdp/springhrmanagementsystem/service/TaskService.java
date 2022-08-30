package uz.pdp.springhrmanagementsystem.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springhrmanagementsystem.entity.Task;
import uz.pdp.springhrmanagementsystem.entity.TaskStatus;
import uz.pdp.springhrmanagementsystem.entity.User;
import uz.pdp.springhrmanagementsystem.entity.enums.RoleList;
import uz.pdp.springhrmanagementsystem.payload.TaskDTO;
import uz.pdp.springhrmanagementsystem.repository.TaskRepository;
import uz.pdp.springhrmanagementsystem.repository.TaskStatusRepository;
import uz.pdp.springhrmanagementsystem.repository.UserRepository;
import uz.pdp.springhrmanagementsystem.security.jwt.JWTProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.*;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final TaskStatusRepository statusRepository;
    private final UserRepository userRepository;
    private final JWTProvider jwt;

    @Autowired
    public TaskService(TaskRepository repository, TaskStatusRepository statusRepository, UserRepository userRepository, JWTProvider jwt) {
        this.repository = repository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.jwt = jwt;
    }

    public ResponseEntity<?> getAllTasks(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : 0, size > 0 ? size : 10);
        return ok(repository.findAll(pageRequest));
    }

    public ResponseEntity<?> getAllTasksStatus() {
        return ok(statusRepository.findAll());
    }

    public ResponseEntity<?> getOneTask(UUID taskId) {
        Optional<Task> optionalTask = repository.findById(taskId);
        if (!optionalTask.isPresent()) return notFound().build();
        return ok(optionalTask.get());
    }

    public ResponseEntity<?> addTask(TaskDTO dto, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findById(dto.getOwnerId());
        if (!optionalUser.isPresent()) status(NOT_FOUND).body("Owner not found");
        Optional<TaskStatus> statusOptional = statusRepository.findById(dto.getStatusId());
        if (!statusOptional.isPresent()) status(NOT_FOUND).body("Task status not found");
        if ((dto.getDeadline().getTime() - new Date().getTime()) <= 0)
            badRequest().body("The date range was entered incorrectly");
        checkPermission(request, RoleList.ROLE_DIRECTOR);
        return ok("ok");
    }

    //    ACTION
    public boolean checkPermission(HttpServletRequest request, RoleList roleName) {
        Claims claims = jwt.getClaimsObjectFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
        return false;
    }
}
