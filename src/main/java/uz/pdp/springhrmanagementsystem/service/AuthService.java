package uz.pdp.springhrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.springhrmanagementsystem.entity.User;
import uz.pdp.springhrmanagementsystem.payload.LoginDTO;
import uz.pdp.springhrmanagementsystem.payload.RegisterDTO;
import uz.pdp.springhrmanagementsystem.payload.response.UserResponse;
import uz.pdp.springhrmanagementsystem.repository.RoleRepository;
import uz.pdp.springhrmanagementsystem.repository.UserRepository;
import uz.pdp.springhrmanagementsystem.security.jwt.JWTProvider;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.*;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    @Autowired
    public AuthService(UserRepository repository, RoleRepository roleRepository, @Lazy AuthenticationManager authenticationManager, JWTProvider jwtProvider) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findUserByEmail(username).orElse(null);
    }

    //    ROLE_DIRECTOR, ROLE_MANAGER
    public ResponseEntity<?> getUsers(Integer page, Integer size) {
        Page<User> users = repository.findAll(PageRequest.of(page > 0 ? page - 1 : 0, size > 0 ? size : 10));
        return ok(users.get().map(UserResponse::new).collect(Collectors.toSet()));
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

    public ResponseEntity<?> login(LoginDTO dto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
            return ok(jwtProvider.generateToken(authenticate));
        } catch (BadCredentialsException e) {
            return status(401).body("Password or login error. Please check and try again");
        }
    }

    public ResponseEntity<?> verifyEmail(String email, String emailCode) {
        Optional<User> optionalUser = repository.findUserByEmail(email);
        if (!optionalUser.isPresent()) return notFound().build();
        User user = optionalUser.get();
        if (user.isEnabled()) return ok("Account allaqachon activelashtirilgan");
        if (user.getEmailCode().equals(emailCode)) {
            user.setEmailCode(null);
            repository.save(user);
            return ok("Account muvaffaqiyatli activelashtirildi.");
        }
//        SEND EMAIL
        return ok("Email tasdiqlash code xato. Biz emailingizga qayta link jo'natdik");
    }

    public ResponseEntity<?> register(RegisterDTO dto) {
        return null;
    }
}
