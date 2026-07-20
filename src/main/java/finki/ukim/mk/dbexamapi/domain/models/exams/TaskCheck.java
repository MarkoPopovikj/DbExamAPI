package finki.ukim.mk.dbexamapi.domain.models.exams;

import finki.ukim.mk.dbexamapi.domain.enums.CheckType;
import finki.ukim.mk.dbexamapi.domain.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TASK_CHECKS", uniqueConstraints = {
        @UniqueConstraint(name = "uq_task_checks_task_position", columnNames = {"task_id", "position"})
})
public class TaskCheck extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "position", nullable = false)
    private int position;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_type", nullable = false)
    private CheckType checkType;

    /**
     * Reference material; shape depends on checkType (reference query,
     * expected schema, verification query + expected state, perf threshold).
     */
    @Column(name = "check_config", columnDefinition = "TEXT")
    private String checkConfig;

    /**
     * A task's points are distributed over its checks — partial credit falls
     * out naturally. Per-check points must sum to the task's points
     * (service-layer validated).
     */
    @Column(name = "points", nullable = false)
    private int points;
}
