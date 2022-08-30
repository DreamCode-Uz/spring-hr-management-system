package uz.pdp.springhrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.springhrmanagementsystem.entity.InOutStatus;
import uz.pdp.springhrmanagementsystem.entity.InputOutput;
import uz.pdp.springhrmanagementsystem.entity.User;
import uz.pdp.springhrmanagementsystem.payload.InputOutputDTO;
import uz.pdp.springhrmanagementsystem.repository.InOutStatusRepository;
import uz.pdp.springhrmanagementsystem.repository.InputOutputRepository;
import uz.pdp.springhrmanagementsystem.repository.UserRepository;

import java.util.Optional;

// BU SERVICE FAQAT MAXSUS SCANNER QILUVCHI KOMPUTER UCHUNGINA ISHLAYDI
@Service
public class InputOutputService {
    private final InputOutputRepository repository;
    private final InOutStatusRepository statusRepository;
    private final UserRepository userRepository;

    @Autowired
    public InputOutputService(InputOutputRepository repository, InOutStatusRepository statusRepository, UserRepository userRepository) {
        this.repository = repository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> saveInputOutputHistory(InputOutputDTO dto) {
        Optional<User> optionalUser = userRepository.findById(dto.getUserId());
        if (!optionalUser.isPresent()) return ResponseEntity.notFound().build();
        Optional<InOutStatus> optionalStatus = statusRepository.findById(dto.getStatusId());
        if (!optionalStatus.isPresent()) return ResponseEntity.notFound().build();
        repository.save(new InputOutput(optionalStatus.get().getStatus(), optionalUser.get()));
        return ResponseEntity.ok().build();
    }
}
