package finki.ukim.mk.dbexamapi.web.v1.extensions.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.DatabaseTemplateDto;
import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.DatabaseTemplateRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.DatabaseTemplateResponse;

import java.util.List;

public final class DatabaseTemplateExtensions {

    private DatabaseTemplateExtensions() {
    }

    public static DatabaseTemplateDto toDto(DatabaseTemplateRequest request) {
        return new DatabaseTemplateDto(
                request.name(),
                request.templateDbName(),
                request.sourceScript()
        );
    }

    public static DatabaseTemplateResponse toResponse(DatabaseTemplate databaseTemplate) {
        return new DatabaseTemplateResponse(
                databaseTemplate.getId(),
                databaseTemplate.getName(),
                databaseTemplate.getTemplateDbName(),
                databaseTemplate.getSourceScript(),
                databaseTemplate.isActive()
        );
    }

    public static List<DatabaseTemplateResponse> toResponse(List<DatabaseTemplate> databaseTemplates) {
        return databaseTemplates.stream()
                .map(DatabaseTemplateExtensions::toResponse)
                .toList();
    }
}
