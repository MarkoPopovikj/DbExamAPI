package finki.ukim.mk.dbexamapi.web.v1.responses.exams;

import finki.ukim.mk.dbexamapi.domain.enums.EnvironmentMode;
import finki.ukim.mk.dbexamapi.domain.enums.ResetPolicy;

public record TaskEnvironmentResponse(
        String id,
        String examId,
        String name,
        String description,
        EnvironmentMode mode,
        String templateId,
        ResetPolicy resetPolicy,
        Integer poolSize,
        String autoPopulateScript,
        boolean active
) {
}
