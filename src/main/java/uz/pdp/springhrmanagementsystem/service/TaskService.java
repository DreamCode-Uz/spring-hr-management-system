package uz.pdp.springhrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springhrmanagementsystem.repository.TaskRepository;
import uz.pdp.springhrmanagementsystem.repository.TaskStatusRepository;

import static org.springframework.http.ResponseEntity.*;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final TaskStatusRepository statusRepository;

    @Autowired
    public TaskService(TaskRepository repository, TaskStatusRepository statusRepository) {
        this.repository = repository;
        this.statusRepository = statusRepository;
    }

    public ResponseEntity<?> getAllTasks(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : 0, size > 0 ? size : 10);
        return ok(repository.findAll(pageRequest));
    }

    public ResponseEntity<?> getAllTasksStatus() {
        return ok(statusRepository.findAll());
    }
}
