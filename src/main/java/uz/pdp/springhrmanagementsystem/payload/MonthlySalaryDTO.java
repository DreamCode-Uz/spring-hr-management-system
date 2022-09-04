package uz.pdp.springhrmanagementsystem.payload;

import lombok.Data;
import uz.pdp.springhrmanagementsystem.entity.Month;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class MonthlySalaryDTO {
    @NotNull
    private UUID ownerId;
    @NotNull
    private Double amount;
    @NotNull
    private Integer monthId;
}
