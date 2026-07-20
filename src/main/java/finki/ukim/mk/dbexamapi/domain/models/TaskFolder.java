package finki.ukim.mk.dbexamapi.domain.models;

import finki.ukim.mk.dbexamapi.domain.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TASK_FOLDERS", uniqueConstraints = {
        @UniqueConstraint(name = "uq_task_folders_task_folder", columnNames = {"task_id", "folder_id"})
})
public class TaskFolder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;
}
