package finki.ukim.mk.dbexamapi.service.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.DocumentDto;
import finki.ukim.mk.dbexamapi.domain.dtos.content.FolderDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.DocumentDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.content.Document;
import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DocumentServiceImplIT {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FolderService folderService;

    private Folder newFolder(String name) {
        return folderService.create(new FolderDto(name));
    }

    @Test
    void create_savesDocumentInFolderAndReturnsIt() {
        Folder folder = newFolder("DOC-CREATE");

        Document created = documentService.create(
                new DocumentDto(folder, "Task sheet", "# Task 1"));

        assertNotNull(created.getId());
        assertEquals(folder.getId(), created.getFolder().getId());
        assertEquals("Task sheet", created.getName());
        assertEquals("# Task 1", created.getDocumentMd());

        Document found = documentService.findByIdNotNull(created.getId());
        assertEquals(folder.getId(), found.getFolder().getId());
        assertEquals("Task sheet", found.getName());
        assertEquals("# Task 1", found.getDocumentMd());
    }

    @Test
    void update_replacesFolderNameAndBody() {
        Folder folder = newFolder("DOC-UPD");
        Folder otherFolder = newFolder("DOC-UPD-2");
        Document created = documentService.create(
                new DocumentDto(folder, "Task sheet", "# Task 1"));

        Document updated = documentService.update(created.getId(),
                new DocumentDto(otherFolder, "Task sheet v2", "# Task 1 revised"));

        assertEquals(created.getId(), updated.getId());
        assertEquals(otherFolder.getId(), updated.getFolder().getId());
        assertEquals("Task sheet v2", updated.getName());
        assertEquals("# Task 1 revised", updated.getDocumentMd());

        Document found = documentService.findByIdNotNull(created.getId());
        assertEquals(otherFolder.getId(), found.getFolder().getId());
        assertEquals("Task sheet v2", found.getName());
        assertEquals("# Task 1 revised", found.getDocumentMd());
    }

    @Test
    void deleteById_removesDocumentAndReturnsIt() {
        Folder folder = newFolder("DOC-DEL");
        Document created = documentService.create(
                new DocumentDto(folder, "Task sheet", "# Task 1"));

        Document deleted = documentService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertEquals("Task sheet", deleted.getName());
        assertThrows(DocumentDoesNotExistException.class,
                () -> documentService.findByIdNotNull(created.getId()));
    }
}
