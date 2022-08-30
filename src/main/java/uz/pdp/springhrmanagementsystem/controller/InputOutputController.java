package uz.pdp.springhrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springhrmanagementsystem.payload.InputOutputDTO;
import uz.pdp.springhrmanagementsystem.service.InputOutputService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/input/output")
public class InputOutputController {
    private final InputOutputService service;

    @Autowired
    public InputOutputController(InputOutputService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addHistory(@Valid @RequestBody InputOutputDTO dto) {
        return service.saveInputOutputHistory(dto);
    }

    @GetMapping
    public ResponseEntity<?> getAllHistory(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.getInputOutputHistory(page, size);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserAllHistory(@PathVariable("userId") UUID id) {
        return service.getInputOutputForUser(id);
    }

}
