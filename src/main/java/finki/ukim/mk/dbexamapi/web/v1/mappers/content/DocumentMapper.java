package finki.ukim.mk.dbexamapi.web.v1.mappers.content;

import finki.ukim.mk.dbexamapi.domain.models.Folder;
import finki.ukim.mk.dbexamapi.service.content.DocumentService;
import finki.ukim.mk.dbexamapi.service.content.FolderService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.content.DocumentExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.DocumentRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.DocumentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentMapper {

    private final DocumentService documentService;
    private final FolderService folderService;

    public DocumentMapper(DocumentService documentService, FolderService folderService) {
        this.documentService = documentService;
        this.folderService = folderService;
    }

    public List<DocumentResponse> findAll() {
        return DocumentExtensions.toResponse(documentService.findAll());
    }

    public DocumentResponse findById(String id) {
        return DocumentExtensions.toResponse(documentService.findByIdNotNull(id));
    }

    public DocumentResponse create(DocumentRequest request) {
        Folder folder = folderService.findByIdNotNull(request.folderId());
        return DocumentExtensions.toResponse(
                documentService.create(DocumentExtensions.toDto(request, folder)));
    }

    public DocumentResponse update(String id, DocumentRequest request) {
        Folder folder = folderService.findByIdNotNull(request.folderId());
        return DocumentExtensions.toResponse(
                documentService.update(id, DocumentExtensions.toDto(request, folder)));
    }

    public DocumentResponse deleteById(String id) {
        return DocumentExtensions.toResponse(documentService.deleteById(id));
    }
}
