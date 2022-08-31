package uz.pdp.springhrmanagementsystem.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springhrmanagementsystem.entity.Role;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.*;
import static uz.pdp.springhrmanagementsystem.entity.enums.TaskStatus.*;

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
        if (!optionalUser.isPresent()) return status(NOT_FOUND).body("Owner not found");
        Optional<TaskStatus> statusOptional = statusRepository.findById(dto.getStatusId());
        if (!statusOptional.isPresent()) return status(NOT_FOUND).body("Task status not found");
        if ((dto.getDeadline().getTime() - new Date().getTime()) <= 0)
            return badRequest().body("The date range was entered incorrectly");
        Claims claims = jwt.getClaimsObjectFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
        Optional<User> optionalCurrentUser = userRepository.findUserByEmailAndId(claims.getSubject(), UUID.fromString(claims.getId()));
        if (!optionalCurrentUser.isPresent())
            return status(401).body("There was a problem with your access rights. Please log in again :)");
        Task task = checkRole(optionalCurrentUser.get(), optionalUser.get(), dto);
        if (task == null) return badRequest().build();
        task.setStatus(statusOptional.get().getName());
        return status(CREATED).body(repository.save(task));
    }

    public ResponseEntity<?> editTask(UUID taskId, TaskDTO dto, HttpServletRequest request) {
        Optional<Task> optionalTask = repository.findById(taskId);
        if (!optionalTask.isPresent()) return status(NOT_FOUND).body("Task not found");
        Optional<User> optionalUser = userRepository.findById(dto.getOwnerId());
        if (!optionalUser.isPresent()) return status(NOT_FOUND).body("Owner not found");
        Optional<TaskStatus> statusOptional = statusRepository.findById(dto.getStatusId());
        if (!statusOptional.isPresent()) return status(NOT_FOUND).body("Task status not found");
        if ((dto.getDeadline().getTime() - new Date().getTime()) <= 0)
            return badRequest().body("The date range was entered incorrectly");
        Claims claims = jwt.getClaimsObjectFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
        Optional<User> optionalCurrentUser = userRepository.findUserByEmailAndId(claims.getSubject(), UUID.fromString(claims.getId()));
        if (!optionalCurrentUser.isPresent())
            return status(401).body("There was a problem with your access rights. Please log in again :)");
        Task task = checkRole(optionalCurrentUser.get(), optionalUser.get(), dto);
        if (task == null) return badRequest().build();
        Task t = optionalTask.get();
        task.setStatus(statusOptional.get().getName());
        task.setAcceptedByOwner(t.isAcceptedByOwner());
        task.setId(taskId);
        task.setCreatedAt(t.getCreatedAt());
        task.setCreatedBy(t.getCreatedBy());
        return ok(repository.save(task));
    }

    public ResponseEntity<?> activatedTask(UUID taskId, String encodedEmail) {
        String email = new String(Base64.getDecoder().decode(encodedEmail), StandardCharsets.UTF_8);
        System.out.println("Email-Decoded: " + email);
        Optional<Task> optionalTask = repository.findById(taskId);
        if (!optionalTask.isPresent()) return badRequest().body("Task not found");
        if (!repository.existsByOwner_EmailAndId(email, taskId))
            return badRequest().body("Afsuski bu task siz uchun emas :(");
        User user = userRepository.getUserByEmail(email);
        Task task = optionalTask.get();
        task.setAcceptedByOwner(true);
        task.setStatus(TASK_PROGRESS);
//        ANONIM USER HOLATDA ACTIVLASHTIRILGANI UCHUN AYNAN UNING IDISI DATABASEGA YOZIB QO'YILADI
        task.setUpdatedBy(user.getId());
        repository.save(task);
        return ok("Task successfully activated");
    }

    //    ACTION
    public Task checkRole(User currentUser, User owner, TaskDTO dto) {
        boolean check = true;
        if (checkRole(currentUser, RoleList.ROLE_DIRECTOR)) {
            for (Role role : owner.getRoles()) {
                if (role.getRole() == RoleList.ROLE_DIRECTOR) {
                    check = false;
                    break;
                }
            }
        } else {
            for (Role role : owner.getRoles()) {
                if (role.getRole() == RoleList.ROLE_DIRECTOR || role.getRole() == RoleList.ROLE_MANAGER) {
                    check = false;
                    break;
                }
            }
        }
        if (check) {
            return new Task(dto.getName(), dto.getDescription(), dto.getDeadline(), owner);
        }
        return null;
    }

    public boolean checkRole(User user, RoleList roleName) {
        for (Role role : user.getRoles()) {
            if (role.getRole().equals(roleName)) return true;
        }
        return false;
    }
}
