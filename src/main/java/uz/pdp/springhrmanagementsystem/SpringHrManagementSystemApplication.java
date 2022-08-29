package uz.pdp.springhrmanagementsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.pdp.springhrmanagementsystem.entity.Role;
import uz.pdp.springhrmanagementsystem.entity.TaskStatus;
import uz.pdp.springhrmanagementsystem.entity.User;
import uz.pdp.springhrmanagementsystem.entity.enums.RoleList;
import uz.pdp.springhrmanagementsystem.repository.RoleRepository;
import uz.pdp.springhrmanagementsystem.repository.TaskStatusRepository;
import uz.pdp.springhrmanagementsystem.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static uz.pdp.springhrmanagementsystem.entity.enums.TaskStatus.*;

@SpringBootApplication
public class SpringHrManagementSystemApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final TaskStatusRepository statusRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.custom.jpa.data.initializer.mode}")
    private String initializer;

    @Autowired
    public SpringHrManagementSystemApplication(RoleRepository roleRepository, TaskStatusRepository statusRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (Objects.equals(initializer, "always")) {
            roleRepository.saveAll(Arrays.asList(
                            new Role(null, RoleList.ROLE_DIRECTOR),
                            new Role(null, RoleList.ROLE_MANAGER),
                            new Role(null, RoleList.ROLE_OWNER)
                    )
            );
            statusRepository.saveAll(Arrays.asList(
                    new TaskStatus(null, TASK_NEW),
                    new TaskStatus(null, TASK_PROGRESS),
                    new TaskStatus(null, TASK_COMPLETED)
            ));
            Optional<Role> roleByRole = roleRepository.findRoleByRole(RoleList.ROLE_DIRECTOR);
            User user = new User("John", "Doe", "john@mail.com", passwordEncoder.encode("root12345"), Collections.singleton(roleByRole.get()));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringHrManagementSystemApplication.class);
    }
}
