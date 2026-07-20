package finki.ukim.mk.dbexamapi.service.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.DocumentDto;
import finki.ukim.mk.dbexamapi.domain.dtos.content.ExamFolderDto;
import finki.ukim.mk.dbexamapi.domain.dtos.content.FolderDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.ExamFolderAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.ExamFolderDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.content.Document;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.domain.models.content.ExamFolder;
import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import finki.ukim.mk.dbexamapi.service.exams.CourseService;
import finki.ukim.mk.dbexamapi.service.exams.ExamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ExamFolderServiceImplIT {

    @Autowired
    private ExamFolderService examFolderService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ExamService examService;

    @Autowired
    private CourseService courseService;

    private Exam newExam(String courseCode) {
        Instant startsAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        return examService.create(new ExamDto(
                courseService.create(new CourseDto(courseCode, "Databases")),
                "Midterm", startsAt, startsAt.plus(2, ChronoUnit.HOURS)));
    }

    private Folder newFolder(String name) {
        return folderService.create(new FolderDto(name));
    }

    @Test
    void create_attachesFolderToExamAndReturnsIt() {
        Exam exam = newExam("EF-CREATE");
        Folder folder = newFolder("Task Sheets");

        ExamFolder created = examFolderService.create(new ExamFolderDto(exam, folder));

        assertNotNull(created.getId());
        assertEquals(exam.getId(), created.getExam().getId());
        assertEquals(folder.getId(), created.getFolder().getId());

        ExamFolder found = examFolderService.findByIdNotNull(created.getId());
        assertEquals(exam.getId(), found.getExam().getId());
        assertEquals(folder.getId(), found.getFolder().getId());
    }

    @Test
    void create_sameExamAndFolderTwice_throwsExamFolderAlreadyExists() {
        Exam exam = newExam("EF-DUP");
        Folder folder = newFolder("Task Sheets");
        examFolderService.create(new ExamFolderDto(exam, folder));

        assertThrows(ExamFolderAlreadyExistsException.class,
                () -> examFolderService.create(new ExamFolderDto(exam, folder)));
    }

    @Test
    void create_sameFolderOnSecondExam_succeeds() {
        Exam firstExam = newExam("EF-REUSE-1");
        Exam secondExam = newExam("EF-REUSE-2");
        Folder folder = newFolder("Shared Instructions");
        examFolderService.create(new ExamFolderDto(firstExam, folder));

        ExamFolder created = examFolderService.create(new ExamFolderDto(secondExam, folder));

        assertNotNull(created.getId());
        assertEquals(secondExam.getId(), created.getExam().getId());
        assertEquals(folder.getId(), created.getFolder().getId());
    }

    @Test
    void deleteById_detachesFolderLeavingFolderAndDocumentsIntact() {
        Exam exam = newExam("EF-DETACH");
        Folder folder = newFolder("Detachable");
        Document document = documentService.create(
                new DocumentDto(folder, "Task sheet", "# Task 1"));
        ExamFolder attached = examFolderService.create(new ExamFolderDto(exam, folder));

        examFolderService.deleteById(attached.getId());

        assertThrows(ExamFolderDoesNotExistException.class,
                () -> examFolderService.findByIdNotNull(attached.getId()));
        Folder foundFolder = folderService.findByIdNotNull(folder.getId());
        assertEquals("Detachable", foundFolder.getName());
        Document foundDocument = documentService.findByIdNotNull(document.getId());
        assertEquals("Task sheet", foundDocument.getName());
        assertEquals(folder.getId(), foundDocument.getFolder().getId());
    }

    @Test
    void update_keepingOwnExamAndFolder_succeeds() {
        Exam exam = newExam("EF-KEEP");
        Folder folder = newFolder("Kept");
        ExamFolder created = examFolderService.create(new ExamFolderDto(exam, folder));

        ExamFolder updated = examFolderService.update(created.getId(),
                new ExamFolderDto(exam, folder));

        assertEquals(created.getId(), updated.getId());
        assertEquals(exam.getId(), updated.getExam().getId());
        assertEquals(folder.getId(), updated.getFolder().getId());
    }

    @Test
    void update_toAnotherRowsExamAndFolder_throwsExamFolderAlreadyExists() {
        Exam exam = newExam("EF-TAKEN");
        Folder takenFolder = newFolder("Taken");
        examFolderService.create(new ExamFolderDto(exam, takenFolder));
        ExamFolder other = examFolderService.create(
                new ExamFolderDto(exam, newFolder("Other")));

        assertThrows(ExamFolderAlreadyExistsException.class,
                () -> examFolderService.update(other.getId(),
                        new ExamFolderDto(exam, takenFolder)));
    }

    @Test
    void deleteById_thenFetch_throwsExamFolderDoesNotExist() {
        Exam exam = newExam("EF-DEL");
        Folder folder = newFolder("Deleted");
        ExamFolder created = examFolderService.create(new ExamFolderDto(exam, folder));

        ExamFolder deleted = examFolderService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertThrows(ExamFolderDoesNotExistException.class,
                () -> examFolderService.findByIdNotNull(created.getId()));
    }
}
