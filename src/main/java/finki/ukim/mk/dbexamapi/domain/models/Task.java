package finki.ukim.mk.dbexamapi.domain.models;

import finki.ukim.mk.dbexamapi.domain.enums.SubmissionMode;
import finki.ukim.mk.dbexamapi.domain.enums.TaskType;
import finki.ukim.mk.dbexamapi.domain.models.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TASKS", uniqueConstraints = {
        @UniqueConstraint(name = "uq_tasks_exam_position", columnNames = {"exam_id", "position"})
})
public class Task extends BaseAuditableEntity {

    /**
     * Must equal the environment's exam (service-layer validated).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "environment_id", nullable = false)
    private TaskEnvironment environment;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(name = "submission_mode", nullable = false)
    private SubmissionMode submissionMode;

    @Column(name = "points", nullable = false)
    private int points;
}
