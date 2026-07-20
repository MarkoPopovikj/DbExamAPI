package finki.ukim.mk.dbexamapi.service.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.FolderDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.FolderDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FolderServiceImplIT {

    @Autowired
    private FolderService folderService;

    @Test
    void create_savesFolderAndReturnsIt() {
        Folder created = folderService.create(new FolderDto("Task Sheets"));

        assertNotNull(created.getId());
        assertEquals("Task Sheets", created.getName());

        Folder found = folderService.findByIdNotNull(created.getId());
        assertEquals("Task Sheets", found.getName());
    }

    @Test
    void update_replacesName() {
        Folder created = folderService.create(new FolderDto("Instructions"));

        Folder updated = folderService.update(created.getId(), new FolderDto("Instructions Renamed"));

        assertEquals(created.getId(), updated.getId());
        assertEquals("Instructions Renamed", updated.getName());

        Folder found = folderService.findByIdNotNull(created.getId());
        assertEquals("Instructions Renamed", found.getName());
    }

    @Test
    void deleteById_removesFolderAndReturnsIt() {
        Folder created = folderService.create(new FolderDto("Diagrams"));

        Folder deleted = folderService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertEquals("Diagrams", deleted.getName());
        assertThrows(FolderDoesNotExistException.class,
                () -> folderService.findByIdNotNull(created.getId()));
    }

    @Test
    void findByIdNotNull_missingId_throwsFolderDoesNotExist() {
        assertThrows(FolderDoesNotExistException.class,
                () -> folderService.findByIdNotNull("missing-folder-id"));
    }
}
