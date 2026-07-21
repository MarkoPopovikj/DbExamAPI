package finki.ukim.mk.dbexamapi.repository.content;

import finki.ukim.mk.dbexamapi.domain.models.content.ExamFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamFolderRepository extends JpaRepository<ExamFolder, String> {

    boolean existsByExam_IdAndFolder_Id(String examId, String folderId);

    boolean existsByExam_IdAndFolder_IdAndIdNot(String examId, String folderId, String id);
}
