package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamDto;
import finki.ukim.mk.dbexamapi.domain.models.Exam;

import java.util.List;
import java.util.Optional;

public interface ExamService {

    Optional<Exam> findById(String id);

    Exam findByIdNotNull(String id);

    List<Exam> findAll();

    Exam create(ExamDto examDto);

    Exam update(String id, ExamDto examDto);

    Exam deleteById(String id);
}
