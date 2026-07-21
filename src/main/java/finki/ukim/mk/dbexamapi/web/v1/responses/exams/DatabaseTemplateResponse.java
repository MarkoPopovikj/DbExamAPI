package finki.ukim.mk.dbexamapi.web.v1.responses.exams;

public record DatabaseTemplateResponse(
        String id,
        String name,
        String templateDbName,
        String sourceScript,
        boolean active
) {
}
