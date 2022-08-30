package uz.pdp.springhrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springhrmanagementsystem.payload.LoginDTO;
import uz.pdp.springhrmanagementsystem.payload.RegisterDTO;
import uz.pdp.springhrmanagementsystem.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginToSystem(@RequestBody @Valid LoginDTO dto) {
        return service.login(dto);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam(name = "email") String email, @RequestParam(name = "emailCode") String emailCode) {
        return service.verifyEmail(email, emailCode);
    }
}
