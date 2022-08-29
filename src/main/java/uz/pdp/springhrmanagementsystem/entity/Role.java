package uz.pdp.springhrmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.springhrmanagementsystem.entity.enums.RoleList;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private RoleList role;

    @Override
    public String getAuthority() {
        return role.name();
    }
}
