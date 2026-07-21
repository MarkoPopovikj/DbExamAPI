package finki.ukim.mk.dbexamapi.web.v1.mappers.exams;

import finki.ukim.mk.dbexamapi.service.exams.DatabaseTemplateService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.exams.DatabaseTemplateExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.DatabaseTemplateRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.DatabaseTemplateResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseTemplateMapper {

    private final DatabaseTemplateService databaseTemplateService;

    public DatabaseTemplateMapper(DatabaseTemplateService databaseTemplateService) {
        this.databaseTemplateService = databaseTemplateService;
    }

    public List<DatabaseTemplateResponse> findAll() {
        return DatabaseTemplateExtensions.toResponse(databaseTemplateService.findAll());
    }

    public DatabaseTemplateResponse findById(String id) {
        return DatabaseTemplateExtensions.toResponse(databaseTemplateService.findByIdNotNull(id));
    }

    public DatabaseTemplateResponse create(DatabaseTemplateRequest request) {
        return DatabaseTemplateExtensions.toResponse(
                databaseTemplateService.create(DatabaseTemplateExtensions.toDto(request)));
    }

    public DatabaseTemplateResponse update(String id, DatabaseTemplateRequest request) {
        return DatabaseTemplateExtensions.toResponse(
                databaseTemplateService.update(id, DatabaseTemplateExtensions.toDto(request)));
    }

    public DatabaseTemplateResponse retireById(String id) {
        return DatabaseTemplateExtensions.toResponse(databaseTemplateService.retireById(id));
    }

    public DatabaseTemplateResponse activateById(String id) {
        return DatabaseTemplateExtensions.toResponse(databaseTemplateService.activateById(id));
    }
}
