package uz.pdp.springhrmanagementsystem.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {
    @NotNull(message = "Email must not be null.")
    @Email(message = "Email is not entered correctly.")
    private String email;
    @NotNull(message = "A password must be entered.")
    private String password;
}
