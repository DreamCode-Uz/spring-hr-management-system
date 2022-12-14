package uz.pdp.springhrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springhrmanagementsystem.entity.InOutStatus;

@Repository
public interface InOutStatusRepository extends JpaRepository<InOutStatus, Integer> {
}
