package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamParticipantDto;
import finki.ukim.mk.dbexamapi.domain.models.exams.ExamParticipant;

import java.util.List;
import java.util.Optional;

public interface ExamParticipantService {

    Optional<ExamParticipant> findById(String id);

    ExamParticipant findByIdNotNull(String id);

    List<ExamParticipant> findAll();

    ExamParticipant create(ExamParticipantDto examParticipantDto);

    ExamParticipant update(String id, ExamParticipantDto examParticipantDto);

    ExamParticipant deleteById(String id);
}
