package finki.ukim.mk.dbexamapi.repository.exams;

import finki.ukim.mk.dbexamapi.domain.models.exams.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    boolean existsByExam_IdAndPosition(String examId, int position);

    boolean existsByExam_IdAndPositionAndIdNot(String examId, int position, String id);

    boolean existsByEnvironment_Id(String environmentId);
}
