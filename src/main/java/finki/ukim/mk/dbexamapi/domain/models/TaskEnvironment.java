package finki.ukim.mk.dbexamapi.domain.models;

import finki.ukim.mk.dbexamapi.domain.enums.EnvironmentMode;
import finki.ukim.mk.dbexamapi.domain.enums.ResetPolicy;
import finki.ukim.mk.dbexamapi.domain.models.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A declarative database setup, scoped to one exam. Tasks point at an
 * environment; tasks sharing an environment share a physical database —
 * that is how "task 2 continues in the database task 1 built" works.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TASK_ENVIRONMENTS")
public class TaskEnvironment extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Scenario text shown with every task that uses this environment.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false)
    private EnvironmentMode mode;

    /**
     * Starting state; NULL = created empty (students write the DDL themselves).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private DatabaseTemplate template;

    @Enumerated(EnumType.STRING)
    @Column(name = "reset_policy", nullable = false)
    private ResetPolicy resetPolicy;

    /**
     * SWAP_FROM_POOL only: ready databases kept pre-created.
     */
    @Column(name = "pool_size")
    private Integer poolSize;

    /**
     * NULL = no auto-populate. For student-built schemas: what the engine runs
     * after the DDL task is accepted. Also the replay source after a RECREATE reset.
     */
    @Column(name = "auto_populate_script", columnDefinition = "TEXT")
    private String autoPopulateScript;
}
