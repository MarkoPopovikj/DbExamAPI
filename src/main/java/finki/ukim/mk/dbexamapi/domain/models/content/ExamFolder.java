package finki.ukim.mk.dbexamapi.domain.models.content;

import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;

import finki.ukim.mk.dbexamapi.domain.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "EXAM_FOLDERS", uniqueConstraints = {
        @UniqueConstraint(name = "uq_exam_folders_exam_folder", columnNames = {"exam_id", "folder_id"})
})
public class ExamFolder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;
}
