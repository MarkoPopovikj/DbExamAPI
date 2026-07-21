package finki.ukim.mk.dbexamapi.domain.dtos.exams;

import finki.ukim.mk.dbexamapi.domain.enums.EnvironmentMode;
import finki.ukim.mk.dbexamapi.domain.enums.ResetPolicy;
import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;

public record TaskEnvironmentDto(
        Exam exam,
        String name,
        String description,
        EnvironmentMode mode,
        DatabaseTemplate template,
        ResetPolicy resetPolicy,
        Integer poolSize,
        String autoPopulateScript
) {
}
