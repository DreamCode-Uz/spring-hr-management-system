package uz.pdp.springhrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springhrmanagementsystem.entity.Role;
import uz.pdp.springhrmanagementsystem.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findUserByEmailAndId(String email, UUID id);

    User getUserByEmail(String email);
}
