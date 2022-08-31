package uz.pdp.springhrmanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import uz.pdp.springhrmanagementsystem.entity.enums.TaskStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "deadline", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private boolean acceptedByOwner = false;

    @ManyToOne
    private User owner;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updateAt;

    @Column(updatable = false)
    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;

    public Task(String name, String description, Date endTime, User owner) {
        this.name = name;
        this.description = description;
        this.endTime = endTime;
        this.owner = owner;
    }
}
