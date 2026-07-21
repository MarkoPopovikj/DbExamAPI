package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.DatabaseTemplateDto;
import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;

import java.util.List;
import java.util.Optional;

public interface DatabaseTemplateService {

    Optional<DatabaseTemplate> findById(String id);

    DatabaseTemplate findByIdNotNull(String id);

    List<DatabaseTemplate> findAll();

    DatabaseTemplate create(DatabaseTemplateDto databaseTemplateDto);

    DatabaseTemplate update(String id, DatabaseTemplateDto databaseTemplateDto);

    DatabaseTemplate retireById(String id);

    DatabaseTemplate activateById(String id);
}
