package finki.ukim.mk.dbexamapi.service.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.FolderDto;
import finki.ukim.mk.dbexamapi.domain.models.Folder;

import java.util.List;
import java.util.Optional;

public interface FolderService {

    Optional<Folder> findById(String id);

    Folder findByIdNotNull(String id);

    List<Folder> findAll();

    Folder create(FolderDto folderDto);

    Folder update(String id, FolderDto folderDto);

    Folder deleteById(String id);
}
