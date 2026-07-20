package finki.ukim.mk.dbexamapi.domain.models;

import finki.ukim.mk.dbexamapi.domain.enums.ManagedDatabaseStatus;
import finki.ukim.mk.dbexamapi.domain.models.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * One row per physical database the system ever created — a complete,
 * self-contained connection descriptor (name, user, password, state, holder).
 * Rows are never reused or deleted; the discard trail survives for disputes.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MANAGED_DATABASES", indexes = {
        @Index(name = "ix_managed_databases_env_status", columnList = "environment_id, status")
})
public class ManagedDatabase extends BaseAuditableEntity {

    /**
     * Denormalized for cheap ops queries; must always equal the environment's
     * exam (service-layer validated).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "environment_id", nullable = false)
    private TaskEnvironment environment;

    /**
     * Physical name on the PG server, strict convention
     * exam_{exam}_env_{env}_db_{n} — the reconciliation handle.
     */
    @Column(name = "db_name", nullable = false, unique = true)
    private String dbName;

    /**
     * PG role created together with the database — pool spares are born
     * connectable; claiming is a metadata-only update.
     */
    @Column(name = "db_username")
    private String dbUsername;

    /**
     * Recoverable by necessity (shown in the UI) — encrypt at rest;
     * throwaway credential dropped with the database.
     */
    @Column(name = "db_password")
    private String dbPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ManagedDatabaseStatus status;

    /**
     * Participant currently/last holding it; NULL while in pool and forever
     * for SHARED environments. Set once per row — a swap discards this row
     * and claims a new one.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private ExamParticipant assignedTo;

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @Column(name = "discarded_at")
    private Instant discardedAt;
}
