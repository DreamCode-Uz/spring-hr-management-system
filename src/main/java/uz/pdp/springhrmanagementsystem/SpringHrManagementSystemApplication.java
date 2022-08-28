package uz.pdp.springhrmanagementsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.pdp.springhrmanagementsystem.entity.Role;
import uz.pdp.springhrmanagementsystem.entity.enums.RoleList;
import uz.pdp.springhrmanagementsystem.repository.RoleRepository;

import java.util.Arrays;

@SpringBootApplication
public class SpringHrManagementSystemApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Autowired
    public SpringHrManagementSystemApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        roleRepository.saveAll(Arrays.asList(
                        new Role(null, RoleList.ROLE_DIRECTOR),
                        new Role(null, RoleList.ROLE_MANAGER),
                        new Role(null, RoleList.ROLE_OWNER)
                )
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringHrManagementSystemApplication.class);
    }
}
