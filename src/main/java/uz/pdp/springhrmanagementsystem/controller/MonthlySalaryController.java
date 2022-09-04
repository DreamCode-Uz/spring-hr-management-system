package uz.pdp.springhrmanagementsystem.controller;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springhrmanagementsystem.payload.MonthlySalaryDTO;
import uz.pdp.springhrmanagementsystem.service.MonthSalaryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class MonthlySalaryController {
    private final MonthSalaryService service;

    @Autowired
    public MonthlySalaryController(MonthSalaryService service) {
        this.service = service;
    }

    //    har qanday ro'yhatdan o'tgan userning oyligini qaytaradi
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMeSalary(HttpServletRequest request) {
        return service.getMeSalary(request);
    }

    @GetMapping("/{userId}")
    @Secured({"ROLE_DIRECTOR", "ROLE_MANAGER"})
    public ResponseEntity<?> getOneUserSalary(@PathVariable("userId") UUID id) {
        return service.getOneSalary(id);
    }

    @PostMapping
    @Secured({"ROLE_DIRECTOR", "ROLE_MANAGER"})
    public ResponseEntity<?> addUserSalary(@RequestBody @Valid MonthlySalaryDTO monthlySalaryDTO) {
        return service.addSalary(monthlySalaryDTO);
    }
}
