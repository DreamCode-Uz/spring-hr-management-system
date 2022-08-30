package uz.pdp.springhrmanagementsystem.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class InputOutputDTO {
    @NotBlank
    private UUID userId;
    @NotNull
    private Integer statusId;
}
