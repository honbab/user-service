package startspring.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import startspring.entity.common.BaseEntity;

@Entity(name = "users")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String password;
    private String name;
    private String email;
    private String phone;

    public User(String userId, String password, String name, String email, String phone) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void setEncodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
