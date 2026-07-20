package finki.ukim.mk.dbexamapi.domain.models.exams;

import finki.ukim.mk.dbexamapi.domain.models.identity.User;

import finki.ukim.mk.dbexamapi.domain.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "EXAM_PARTICIPANTS", uniqueConstraints = {
        @UniqueConstraint(name = "uq_exam_participants_exam_user", columnNames = {"exam_id", "user_id"})
})
public class ExamParticipant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Authorization scope: an admin only has power over exams where they hold
     * a row with isAdmin = true.
     */
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;
}
