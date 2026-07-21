package finki.ukim.mk.dbexamapi.domain.models.identity;

import finki.ukim.mk.dbexamapi.domain.models.common.BaseAuditableEntity;
import finki.ukim.mk.dbexamapi.domain.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "USER_DETAILS")
public class UserDetail extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "index", unique = true)
    private String index;

    @Column(name = "email", unique = true)
    private String email;
}
