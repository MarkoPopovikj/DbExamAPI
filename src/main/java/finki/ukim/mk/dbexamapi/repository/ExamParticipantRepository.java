package finki.ukim.mk.dbexamapi.repository;

import finki.ukim.mk.dbexamapi.domain.models.ExamParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamParticipantRepository extends JpaRepository<ExamParticipant, String> {

    boolean existsByExam_IdAndUser_Id(String examId, String userId);

    boolean existsByExam_IdAndUser_IdAndIdNot(String examId, String userId, String id);
}
