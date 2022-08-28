package uz.pdp.springhrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.springhrmanagementsystem.entity.User;
import uz.pdp.springhrmanagementsystem.payload.RegisterDTO;
import uz.pdp.springhrmanagementsystem.payload.response.UserResponse;
import uz.pdp.springhrmanagementsystem.repository.RoleRepository;
import uz.pdp.springhrmanagementsystem.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(UserRepository repository, RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    //    ROLE_DIRECTOR, ROLE_MANAGER
    public ResponseEntity<?> getUsers(Integer page, Integer size) {
        Page<User> users = repository.findAll(PageRequest.of(page > 0 ? page - 1 : 0, size > 0 ? size : 10));
        Set<UserResponse> responses = new HashSet<>();
        for (User user : users) {
            responses.add(new UserResponse(user));
        }
        return ok(responses);
    }

    //    ROLE_DIRECTOR, ROLE_MANAGER
    public ResponseEntity<?> getUser(UUID id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) return ok(new UserResponse(optionalUser.get()));
        return status(HttpStatus.NOT_FOUND).body("User not found");
    }

    public ResponseEntity<?> saveUser(RegisterDTO dto) {
        return null;
    }
}
