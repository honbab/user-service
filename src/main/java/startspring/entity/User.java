package startspring.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import startspring.entity.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private String status;
    private String address;
}
