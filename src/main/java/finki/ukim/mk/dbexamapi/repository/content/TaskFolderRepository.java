package finki.ukim.mk.dbexamapi.repository.content;

import finki.ukim.mk.dbexamapi.domain.models.content.TaskFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskFolderRepository extends JpaRepository<TaskFolder, String> {

    boolean existsByTask_IdAndFolder_Id(String taskId, String folderId);

    boolean existsByTask_IdAndFolder_IdAndIdNot(String taskId, String folderId, String id);

    void deleteAllByTask_Id(String taskId);
}
