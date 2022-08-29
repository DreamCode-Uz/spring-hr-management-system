package uz.pdp.springhrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springhrmanagementsystem.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final AuthService service;

    @Autowired
    public UserController(AuthService service) {
        this.service = service;
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping("")
    public ResponseEntity<?> getAllUser(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.getUsers(page, size);
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") UUID id) {
        return service.getUser(id);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        return null;
    }
}
