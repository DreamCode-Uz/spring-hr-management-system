package uz.pdp.springhrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springhrmanagementsystem.payload.InputOutputDTO;
import uz.pdp.springhrmanagementsystem.service.InputOutputService;

import javax.validation.Valid;

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
}
