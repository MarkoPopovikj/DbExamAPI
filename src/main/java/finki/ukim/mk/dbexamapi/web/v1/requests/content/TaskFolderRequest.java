package finki.ukim.mk.dbexamapi.web.v1.requests.content;

import jakarta.validation.constraints.NotBlank;

public record TaskFolderRequest(
        @NotBlank String taskId,
        @NotBlank String folderId
) {
}
