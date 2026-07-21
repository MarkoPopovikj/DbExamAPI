package finki.ukim.mk.dbexamapi.service.exams.impl;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.DatabaseTemplateDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateDbNameAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateDbNameInvalidException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateSourceScriptRequiredException;
import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;
import finki.ukim.mk.dbexamapi.repository.exams.DatabaseTemplateRepository;
import finki.ukim.mk.dbexamapi.service.exams.DatabaseTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DatabaseTemplateServiceImpl implements DatabaseTemplateService {

    /**
     * Safe lowercase PostgreSQL identifier — the engine later splices it into
     * DDL statements, so nothing outside this pattern is ever stored.
     */
    private static final Pattern SAFE_TEMPLATE_DB_NAME = Pattern.compile("^[a-z_][a-z0-9_]{0,62}$");

    private final DatabaseTemplateRepository databaseTemplateRepository;

    public DatabaseTemplateServiceImpl(DatabaseTemplateRepository databaseTemplateRepository) {
        this.databaseTemplateRepository = databaseTemplateRepository;
    }

    @Override
    public Optional<DatabaseTemplate> findById(String id) {
        return databaseTemplateRepository.findById(id);
    }

    @Override
    public DatabaseTemplate findByIdNotNull(String id) {
        return databaseTemplateRepository.findById(id)
                .orElseThrow(() -> new DatabaseTemplateDoesNotExistException(id));
    }

    @Override
    public List<DatabaseTemplate> findAll() {
        return databaseTemplateRepository.findAllByActiveTrue();
    }

    @Override
    @Transactional
    public DatabaseTemplate create(DatabaseTemplateDto databaseTemplateDto) {
        validateRules(databaseTemplateDto);
        if (databaseTemplateRepository.existsByTemplateDbName(databaseTemplateDto.templateDbName())) {
            throw new DatabaseTemplateDbNameAlreadyExistsException(databaseTemplateDto.templateDbName());
        }

        DatabaseTemplate databaseTemplate = new DatabaseTemplate();
        databaseTemplate.setName(databaseTemplateDto.name());
        databaseTemplate.setTemplateDbName(databaseTemplateDto.templateDbName());
        databaseTemplate.setSourceScript(databaseTemplateDto.sourceScript());
        databaseTemplate.setActive(true);

        DatabaseTemplate saved = databaseTemplateRepository.save(databaseTemplate);
        log.info("Created database template with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public DatabaseTemplate update(String id, DatabaseTemplateDto databaseTemplateDto) {
        DatabaseTemplate databaseTemplate = findByIdNotNull(id);

        validateRules(databaseTemplateDto);
        if (databaseTemplateRepository.existsByTemplateDbNameAndIdNot(databaseTemplateDto.templateDbName(), id)) {
            throw new DatabaseTemplateDbNameAlreadyExistsException(databaseTemplateDto.templateDbName());
        }

        databaseTemplate.setName(databaseTemplateDto.name());
        databaseTemplate.setTemplateDbName(databaseTemplateDto.templateDbName());
        databaseTemplate.setSourceScript(databaseTemplateDto.sourceScript());

        DatabaseTemplate saved = databaseTemplateRepository.save(databaseTemplate);
        log.info("Updated database template with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public DatabaseTemplate retireById(String id) {
        DatabaseTemplate databaseTemplate = findByIdNotNull(id);
        databaseTemplate.setActive(false);

        DatabaseTemplate saved = databaseTemplateRepository.save(databaseTemplate);
        log.info("Retired database template with id: {}", id);
        return saved;
    }

    @Override
    @Transactional
    public DatabaseTemplate activateById(String id) {
        DatabaseTemplate databaseTemplate = findByIdNotNull(id);
        databaseTemplate.setActive(true);

        DatabaseTemplate saved = databaseTemplateRepository.save(databaseTemplate);
        log.info("Reactivated database template with id: {}", id);
        return saved;
    }

    private void validateRules(DatabaseTemplateDto databaseTemplateDto) {
        if (databaseTemplateDto.sourceScript() == null || databaseTemplateDto.sourceScript().isBlank()) {
            throw new DatabaseTemplateSourceScriptRequiredException();
        }
        String templateDbName = databaseTemplateDto.templateDbName();
        if (templateDbName == null || !SAFE_TEMPLATE_DB_NAME.matcher(templateDbName).matches()) {
            throw new DatabaseTemplateDbNameInvalidException(templateDbName);
        }
    }
}
