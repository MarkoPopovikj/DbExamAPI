package finki.ukim.mk.dbexamapi.web.v1.responses.content;

public record DocumentResponse(
        String id,
        String folderId,
        String name,
        String documentMd
) {
}
