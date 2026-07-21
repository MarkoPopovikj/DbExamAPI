package finki.ukim.mk.dbexamapi.domain.dtos.exams;

public record DatabaseTemplateDto(
        String name,
        String templateDbName,
        String sourceScript
) {
}
