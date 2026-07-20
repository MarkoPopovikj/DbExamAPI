package finki.ukim.mk.dbexamapi.domain.models.exams;

import finki.ukim.mk.dbexamapi.domain.enums.SubmissionSource;
import finki.ukim.mk.dbexamapi.domain.enums.SubmissionStatus;
import finki.ukim.mk.dbexamapi.domain.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * APPEND-ONLY: never updated, never deleted. Databases are disposable;
 * submissions are sacred — a lost database is rebuildable from the submission
 * log, a lost submission is a lost exam. Deliberately NOT auditable: an
 * updated_at column on an append-only table would be a lie.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "SUBMISSIONS")
public class Submission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private ExamParticipant participant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    /**
     * Which physical db this ran against / was pulled from — the
     * task-database join; also tells which side of a swap the attempt was on.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "managed_database_id", nullable = false)
    private ManagedDatabase managedDatabase;

    /**
     * The answer itself. For student-built schemas, also the replay source
     * after a reset.
     */
    @Column(name = "sql_text", columnDefinition = "TEXT", nullable = false)
    private String sqlText;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private SubmissionSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubmissionStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;
}
