package finki.ukim.mk.dbexamapi.repository;

import finki.ukim.mk.dbexamapi.domain.models.exams.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
}
