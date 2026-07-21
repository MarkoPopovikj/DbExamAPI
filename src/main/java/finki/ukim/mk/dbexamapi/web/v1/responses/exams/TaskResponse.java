package finki.ukim.mk.dbexamapi.web.v1.responses.exams;

import finki.ukim.mk.dbexamapi.domain.enums.SubmissionMode;
import finki.ukim.mk.dbexamapi.domain.enums.TaskType;

public record TaskResponse(
        String id,
        String examId,
        String environmentId,
        int position,
        String title,
        String description,
        TaskType taskType,
        SubmissionMode submissionMode,
        int points
) {
}
