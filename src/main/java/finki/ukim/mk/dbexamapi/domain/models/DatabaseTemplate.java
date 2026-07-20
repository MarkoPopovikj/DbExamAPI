package finki.ukim.mk.dbexamapi.domain.models;

import finki.ukim.mk.dbexamapi.domain.models.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "DATABASE_TEMPLATES")
public class DatabaseTemplate extends BaseAuditableEntity {

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Physical database on the PG server used as CREATE DATABASE ... TEMPLATE source.
     */
    @Column(name = "template_db_name", nullable = false, unique = true)
    private String templateDbName;

    /**
     * The SQL that builds the template (schema + data) — any template can be
     * rebuilt from the API; the server is never the only copy.
     */
    @Column(name = "source_script", columnDefinition = "TEXT")
    private String sourceScript;
}
