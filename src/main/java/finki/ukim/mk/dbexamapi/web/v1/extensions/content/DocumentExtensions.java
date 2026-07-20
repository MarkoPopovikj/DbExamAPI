package finki.ukim.mk.dbexamapi.web.v1.extensions.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.DocumentDto;
import finki.ukim.mk.dbexamapi.domain.models.content.Document;
import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.DocumentRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.DocumentResponse;

import java.util.List;

public final class DocumentExtensions {

    private DocumentExtensions() {
    }

    public static DocumentDto toDto(DocumentRequest request, Folder folder) {
        return new DocumentDto(
                folder,
                request.name(),
                request.documentMd()
        );
    }

    public static DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getFolder().getId(),
                document.getName(),
                document.getDocumentMd()
        );
    }

    public static List<DocumentResponse> toResponse(List<Document> documents) {
        return documents.stream()
                .map(DocumentExtensions::toResponse)
                .toList();
    }
}
