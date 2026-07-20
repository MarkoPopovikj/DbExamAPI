package finki.ukim.mk.dbexamapi.web.v1.mappers.content;

import finki.ukim.mk.dbexamapi.service.content.FolderService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.content.FolderExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.FolderRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.FolderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FolderMapper {

    private final FolderService folderService;

    public FolderMapper(FolderService folderService) {
        this.folderService = folderService;
    }

    public List<FolderResponse> findAll() {
        return FolderExtensions.toResponse(folderService.findAll());
    }

    public FolderResponse findById(String id) {
        return FolderExtensions.toResponse(folderService.findByIdNotNull(id));
    }

    public FolderResponse create(FolderRequest request) {
        return FolderExtensions.toResponse(folderService.create(FolderExtensions.toDto(request)));
    }

    public FolderResponse update(String id, FolderRequest request) {
        return FolderExtensions.toResponse(folderService.update(id, FolderExtensions.toDto(request)));
    }

    public FolderResponse deleteById(String id) {
        return FolderExtensions.toResponse(folderService.deleteById(id));
    }
}
