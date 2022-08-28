package uz.pdp.springhrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springhrmanagementsystem.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
