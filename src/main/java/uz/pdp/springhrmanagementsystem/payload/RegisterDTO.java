package uz.pdp.springhrmanagementsystem.payload;

import lombok.Data;

@Data
public class LoginDTO {
    private String firstname;
    private String lastname;
    private String email;
}
