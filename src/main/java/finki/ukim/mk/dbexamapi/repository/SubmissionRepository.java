package finki.ukim.mk.dbexamapi.repository;

import finki.ukim.mk.dbexamapi.domain.models.exams.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SUBMISSIONS is append-only: save new rows, never update or delete existing
 * ones (enforced by discipline in the service layer).
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
}
