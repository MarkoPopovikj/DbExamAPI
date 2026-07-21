package finki.ukim.mk.dbexamapi.service.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.DocumentDto;
import finki.ukim.mk.dbexamapi.domain.dtos.content.FolderDto;
import finki.ukim.mk.dbexamapi.domain.dtos.content.TaskFolderDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskEnvironmentDto;
import finki.ukim.mk.dbexamapi.domain.enums.EnvironmentMode;
import finki.ukim.mk.dbexamapi.domain.enums.ResetPolicy;
import finki.ukim.mk.dbexamapi.domain.enums.SubmissionMode;
import finki.ukim.mk.dbexamapi.domain.enums.TaskType;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.TaskFolderAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.TaskFolderDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.content.Document;
import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import finki.ukim.mk.dbexamapi.domain.models.content.TaskFolder;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.domain.models.exams.Task;
import finki.ukim.mk.dbexamapi.service.exams.CourseService;
import finki.ukim.mk.dbexamapi.service.exams.ExamService;
import finki.ukim.mk.dbexamapi.service.exams.TaskEnvironmentService;
import finki.ukim.mk.dbexamapi.service.exams.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TaskFolderServiceImplIT {

    @Autowired
    private TaskFolderService taskFolderService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskEnvironmentService taskEnvironmentService;

    @Autowired
    private ExamService examService;

    @Autowired
    private CourseService courseService;

    private Task newTask(String courseCode) {
        Instant startsAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Exam exam = examService.create(new ExamDto(
                courseService.create(new CourseDto(courseCode, "Databases")),
                "Midterm", startsAt, startsAt.plus(2, ChronoUnit.HOURS)));
        return taskService.create(new TaskDto(
                taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "Own schema", null, EnvironmentMode.PER_STUDENT, null,
                        ResetPolicy.NONE, null, null)),
                1, "Create the schema", "Write the DDL for the model.",
                TaskType.SQL_DDL, SubmissionMode.DB_STATE, 10));
    }

    private Folder newFolder(String name) {
        return folderService.create(new FolderDto(name));
    }

    @Test
    void create_attachesFolderToTaskAndReturnsIt() {
        Task task = newTask("TF-CREATE");
        Folder folder = newFolder("Task Sheets");

        TaskFolder created = taskFolderService.create(new TaskFolderDto(task, folder));

        assertNotNull(created.getId());
        assertEquals(task.getId(), created.getTask().getId());
        assertEquals(folder.getId(), created.getFolder().getId());

        TaskFolder found = taskFolderService.findByIdNotNull(created.getId());
        assertEquals(task.getId(), found.getTask().getId());
        assertEquals(folder.getId(), found.getFolder().getId());
    }

    @Test
    void create_sameTaskAndFolderTwice_throwsTaskFolderAlreadyExists() {
        Task task = newTask("TF-DUP");
        Folder folder = newFolder("Task Sheets");
        taskFolderService.create(new TaskFolderDto(task, folder));

        assertThrows(TaskFolderAlreadyExistsException.class,
                () -> taskFolderService.create(new TaskFolderDto(task, folder)));
    }

    @Test
    void create_sameFolderOnSecondTask_succeeds() {
        Task firstTask = newTask("TF-REUSE-1");
        Task secondTask = newTask("TF-REUSE-2");
        Folder folder = newFolder("Shared Diagrams");
        taskFolderService.create(new TaskFolderDto(firstTask, folder));

        TaskFolder created = taskFolderService.create(new TaskFolderDto(secondTask, folder));

        assertNotNull(created.getId());
        assertEquals(secondTask.getId(), created.getTask().getId());
        assertEquals(folder.getId(), created.getFolder().getId());
    }

    @Test
    void deleteById_detachesFolderLeavingFolderAndDocumentsIntact() {
        Task task = newTask("TF-DETACH");
        Folder folder = newFolder("Detachable");
        Document document = documentService.create(
                new DocumentDto(folder, "Task sheet", "# Task 1"));
        TaskFolder attached = taskFolderService.create(new TaskFolderDto(task, folder));

        taskFolderService.deleteById(attached.getId());

        assertThrows(TaskFolderDoesNotExistException.class,
                () -> taskFolderService.findByIdNotNull(attached.getId()));
        Folder foundFolder = folderService.findByIdNotNull(folder.getId());
        assertEquals("Detachable", foundFolder.getName());
        Document foundDocument = documentService.findByIdNotNull(document.getId());
        assertEquals("Task sheet", foundDocument.getName());
        assertEquals(folder.getId(), foundDocument.getFolder().getId());
    }

    @Test
    void update_keepingOwnTaskAndFolder_succeeds() {
        Task task = newTask("TF-KEEP");
        Folder folder = newFolder("Kept");
        TaskFolder created = taskFolderService.create(new TaskFolderDto(task, folder));

        TaskFolder updated = taskFolderService.update(created.getId(),
                new TaskFolderDto(task, folder));

        assertEquals(created.getId(), updated.getId());
        assertEquals(task.getId(), updated.getTask().getId());
        assertEquals(folder.getId(), updated.getFolder().getId());
    }

    @Test
    void update_toAnotherRowsTaskAndFolder_throwsTaskFolderAlreadyExists() {
        Task task = newTask("TF-TAKEN");
        Folder takenFolder = newFolder("Taken");
        taskFolderService.create(new TaskFolderDto(task, takenFolder));
        TaskFolder other = taskFolderService.create(
                new TaskFolderDto(task, newFolder("Other")));

        assertThrows(TaskFolderAlreadyExistsException.class,
                () -> taskFolderService.update(other.getId(),
                        new TaskFolderDto(task, takenFolder)));
    }

    @Test
    void deleteTask_cascadesAttachmentsLeavingFolderAndDocumentsIntact() {
        Task task = newTask("TF-CASCADE");
        Folder folder = newFolder("Attached");
        Document document = documentService.create(
                new DocumentDto(folder, "Task sheet", "# Task 1"));
        TaskFolder attached = taskFolderService.create(new TaskFolderDto(task, folder));

        taskService.deleteById(task.getId());

        assertThrows(TaskDoesNotExistException.class,
                () -> taskService.findByIdNotNull(task.getId()));
        assertThrows(TaskFolderDoesNotExistException.class,
                () -> taskFolderService.findByIdNotNull(attached.getId()));
        Folder foundFolder = folderService.findByIdNotNull(folder.getId());
        assertEquals("Attached", foundFolder.getName());
        Document foundDocument = documentService.findByIdNotNull(document.getId());
        assertEquals("Task sheet", foundDocument.getName());
    }

    @Test
    void deleteById_thenFetch_throwsTaskFolderDoesNotExist() {
        Task task = newTask("TF-DEL");
        Folder folder = newFolder("Deleted");
        TaskFolder created = taskFolderService.create(new TaskFolderDto(task, folder));

        TaskFolder deleted = taskFolderService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertThrows(TaskFolderDoesNotExistException.class,
                () -> taskFolderService.findByIdNotNull(created.getId()));
    }
}
