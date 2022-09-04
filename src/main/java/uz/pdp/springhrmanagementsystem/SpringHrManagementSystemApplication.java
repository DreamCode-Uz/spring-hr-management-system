package uz.pdp.springhrmanagementsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.pdp.springhrmanagementsystem.entity.*;
import uz.pdp.springhrmanagementsystem.entity.enums.MonthName;
import uz.pdp.springhrmanagementsystem.entity.enums.RoleList;
import uz.pdp.springhrmanagementsystem.repository.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static uz.pdp.springhrmanagementsystem.entity.enums.TaskStatus.*;

@SpringBootApplication
public class SpringHrManagementSystemApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final TaskStatusRepository statusRepository;
    private final MonthRepository monthRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InOutStatusRepository inOutStatusRepository;
    @Value("${spring.custom.jpa.data.initializer.mode}")
    private String initializer;

    @Autowired
    public SpringHrManagementSystemApplication(RoleRepository roleRepository, TaskStatusRepository statusRepository, MonthRepository monthRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, InOutStatusRepository inOutStatusRepository) {
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.monthRepository = monthRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.inOutStatusRepository = inOutStatusRepository;
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
            inOutStatusRepository.saveAll(Arrays.asList(
                    new InOutStatus(null, uz.pdp.springhrmanagementsystem.entity.enums.InOutStatus.INPUT_STATUS),
                    new InOutStatus(null, uz.pdp.springhrmanagementsystem.entity.enums.InOutStatus.OUTPUT_STATUS)
            ));
            Optional<Role> roleByRole = roleRepository.findRoleByRole(RoleList.ROLE_DIRECTOR);
            User user = new User("John", "Doe", "john@mail.com", passwordEncoder.encode("root12345"), Collections.singleton(roleByRole.get()));
            user.setEnabled(true);
            userRepository.save(user);
            monthRepository.saveAll(
                    Arrays.asList(
                            new Month(null, MonthName.YANVAR),
                            new Month(null, MonthName.FEVRAL),
                            new Month(null, MonthName.MART),
                            new Month(null, MonthName.APREL),
                            new Month(null, MonthName.MAY),
                            new Month(null, MonthName.IYUN),
                            new Month(null, MonthName.IYUL),
                            new Month(null, MonthName.AVGUST),
                            new Month(null, MonthName.SENTABR),
                            new Month(null, MonthName.OKTABR),
                            new Month(null, MonthName.NOYABR),
                            new Month(null, MonthName.DEKABR)
                    )
            );
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringHrManagementSystemApplication.class);
    }
}
