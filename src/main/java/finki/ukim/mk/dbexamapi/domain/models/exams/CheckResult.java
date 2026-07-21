package finki.ukim.mk.dbexamapi.domain.models.exams;

import finki.ukim.mk.dbexamapi.domain.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Grading evidence — one row per (submission, check). Keyed to the submission
 * so grading a different attempt never overwrites history; re-grading the
 * SAME submission updates in place (unique constraint).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CHECK_RESULTS", uniqueConstraints = {
        @UniqueConstraint(name = "uq_check_results_submission_check", columnNames = {"submission_id", "check_id"})
})
public class CheckResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "check_id", nullable = false)
    private TaskCheck taskCheck;

    @Column(name = "passed", nullable = false)
    private boolean passed;

    @Column(name = "points_awarded", nullable = false)
    private int pointsAwarded;

    /**
     * What the grader saw: expected vs got, timing measured, plan excerpt —
     * shown to the student, used in disputes.
     */
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt;
}
