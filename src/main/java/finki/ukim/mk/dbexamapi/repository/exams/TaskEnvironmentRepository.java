package finki.ukim.mk.dbexamapi.repository.exams;

import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskEnvironmentRepository extends JpaRepository<TaskEnvironment, String> {

    List<TaskEnvironment> findAllByActiveTrue();

    boolean existsByTemplate_IdAndActiveTrue(String templateId);
}
