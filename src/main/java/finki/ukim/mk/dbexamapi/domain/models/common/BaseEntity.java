package finki.ukim.mk.dbexamapi.domain.models.common;

import finki.ukim.mk.dbexamapi.domain.utils.NanoId;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @NanoId
    @Column(length = 21, updatable = false, nullable = false)
    private String id;
}
