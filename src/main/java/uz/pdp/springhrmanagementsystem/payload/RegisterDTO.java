package uz.pdp.springhrmanagementsystem.payload;

import lombok.Data;
import uz.pdp.springhrmanagementsystem.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class RegisterDTO {
    @NotBlank(message = "firstname must be entered")
    private String firstname;
    @NotBlank(message = "lastname must be entered")
    private String lastname;
    @NotNull(message = "email must not be null.")
    @Email(message = "email is not entered correctly.")
    private String email;
    @NotNull(message = "a password must be entered.")
    @Size(message = "the length of the password should not be less than 8 characters")
    private String password;
    private Set<Integer> role;
}
