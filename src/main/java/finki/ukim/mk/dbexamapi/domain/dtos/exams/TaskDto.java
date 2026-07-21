package finki.ukim.mk.dbexamapi.domain.dtos.exams;

import finki.ukim.mk.dbexamapi.domain.enums.SubmissionMode;
import finki.ukim.mk.dbexamapi.domain.enums.TaskType;
import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;

/**
 * Carries no exam — the service derives the task's exam from its environment,
 * making an exam/environment mismatch unrepresentable.
 */
public record TaskDto(
        TaskEnvironment environment,
        int position,
        String title,
        String description,
        TaskType taskType,
        SubmissionMode submissionMode,
        int points
) {
}
