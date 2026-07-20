package finki.ukim.mk.dbexamapi.domain.models;

import finki.ukim.mk.dbexamapi.domain.enums.StudentTaskStatus;
import finki.ukim.mk.dbexamapi.domain.models.common.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "STUDENT_TASKS", uniqueConstraints = {
        @UniqueConstraint(name = "uq_student_tasks_participant_task", columnNames = {"participant_id", "task_id"})
})
public class StudentTask extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private ExamParticipant participant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentTaskStatus status = StudentTaskStatus.NOT_STARTED;

    /**
     * Which submission counts as THE answer for grading. Insert the
     * submission first, then point this at it.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_submission_id")
    private Submission finalSubmission;

    /**
     * Cached sum of the final submission's CheckResult points — CHECK_RESULTS
     * is the ground truth; never edit this directly.
     */
    @Column(name = "score")
    private Integer score;
}
