package uz.pdp.springhrmanagementsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.pdp.springhrmanagementsystem.entity.Role;
import uz.pdp.springhrmanagementsystem.entity.User;
import uz.pdp.springhrmanagementsystem.entity.enums.RoleList;
import uz.pdp.springhrmanagementsystem.repository.RoleRepository;
import uz.pdp.springhrmanagementsystem.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
public class SpringHrManagementSystemApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public SpringHrManagementSystemApplication(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        roleRepository.saveAll(Arrays.asList(
                        new Role(null, RoleList.ROLE_DIRECTOR),
                        new Role(null, RoleList.ROLE_MANAGER),
                        new Role(null, RoleList.ROLE_OWNER)
                )
        );
        Optional<Role> roleByRole = roleRepository.findRoleByRole(RoleList.ROLE_DIRECTOR);
        User user = new User("John", "Doe", "john@mail.com", "root12345", Collections.singleton(roleByRole.get()));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringHrManagementSystemApplication.class);
    }
}
