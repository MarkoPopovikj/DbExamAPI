package finki.ukim.mk.dbexamapi.service.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.ExamFolderDto;
import finki.ukim.mk.dbexamapi.domain.models.ExamFolder;

import java.util.List;
import java.util.Optional;

public interface ExamFolderService {

    Optional<ExamFolder> findById(String id);

    ExamFolder findByIdNotNull(String id);

    List<ExamFolder> findAll();

    ExamFolder create(ExamFolderDto examFolderDto);

    ExamFolder update(String id, ExamFolderDto examFolderDto);

    ExamFolder deleteById(String id);
}
