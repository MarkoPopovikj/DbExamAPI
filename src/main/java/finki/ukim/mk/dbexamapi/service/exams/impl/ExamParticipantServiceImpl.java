package finki.ukim.mk.dbexamapi.service.exams.impl;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamParticipantDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.ExamParticipantAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.ExamParticipantDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.exams.ExamParticipant;
import finki.ukim.mk.dbexamapi.repository.exams.ExamParticipantRepository;
import finki.ukim.mk.dbexamapi.service.exams.ExamParticipantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ExamParticipantServiceImpl implements ExamParticipantService {

    private final ExamParticipantRepository examParticipantRepository;

    public ExamParticipantServiceImpl(ExamParticipantRepository examParticipantRepository) {
        this.examParticipantRepository = examParticipantRepository;
    }

    @Override
    public Optional<ExamParticipant> findById(String id) {
        return examParticipantRepository.findById(id);
    }

    @Override
    public ExamParticipant findByIdNotNull(String id) {
        return examParticipantRepository.findById(id)
                .orElseThrow(() -> new ExamParticipantDoesNotExistException(id));
    }

    @Override
    public List<ExamParticipant> findAll() {
        return examParticipantRepository.findAll();
    }

    @Override
    @Transactional
    public ExamParticipant create(ExamParticipantDto examParticipantDto) {
        String examId = examParticipantDto.exam().getId();
        String userId = examParticipantDto.user().getId();
        if (examParticipantRepository.existsByExam_IdAndUser_Id(examId, userId)) {
            throw new ExamParticipantAlreadyExistsException(examId, userId);
        }

        ExamParticipant examParticipant = new ExamParticipant();
        examParticipant.setExam(examParticipantDto.exam());
        examParticipant.setUser(examParticipantDto.user());
        examParticipant.setAdmin(examParticipantDto.isAdmin());

        ExamParticipant saved = examParticipantRepository.save(examParticipant);
        log.info("Created exam participant with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public ExamParticipant update(String id, ExamParticipantDto examParticipantDto) {
        ExamParticipant examParticipant = findByIdNotNull(id);

        String examId = examParticipantDto.exam().getId();
        String userId = examParticipantDto.user().getId();
        if (examParticipantRepository.existsByExam_IdAndUser_IdAndIdNot(examId, userId, id)) {
            throw new ExamParticipantAlreadyExistsException(examId, userId);
        }

        examParticipant.setExam(examParticipantDto.exam());
        examParticipant.setUser(examParticipantDto.user());
        examParticipant.setAdmin(examParticipantDto.isAdmin());

        ExamParticipant saved = examParticipantRepository.save(examParticipant);
        log.info("Updated exam participant with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public ExamParticipant deleteById(String id) {
        ExamParticipant examParticipant = findByIdNotNull(id);
        examParticipantRepository.delete(examParticipant);
        log.info("Deleted exam participant with id: {}", id);
        return examParticipant;
    }
}
