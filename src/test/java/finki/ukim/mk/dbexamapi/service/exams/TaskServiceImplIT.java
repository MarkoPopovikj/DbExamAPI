package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskEnvironmentDto;
import finki.ukim.mk.dbexamapi.domain.enums.EnvironmentMode;
import finki.ukim.mk.dbexamapi.domain.enums.ResetPolicy;
import finki.ukim.mk.dbexamapi.domain.enums.SubmissionMode;
import finki.ukim.mk.dbexamapi.domain.enums.TaskType;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentExamMismatchException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentInUseException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentRetiredException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskPositionAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskSubmissionModeInvalidException;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.domain.models.exams.Task;
import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TaskServiceImplIT {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskEnvironmentService taskEnvironmentService;

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

    private TaskEnvironment newEnvironment(Exam exam, String name) {
        return taskEnvironmentService.create(new TaskEnvironmentDto(
                exam, name, null, EnvironmentMode.PER_STUDENT, null, ResetPolicy.NONE, null, null));
    }

    private TaskDto ddlTaskDto(TaskEnvironment environment, int position) {
        return new TaskDto(environment, position, "Create the schema",
                "Write the DDL for the model.", TaskType.SQL_DDL, SubmissionMode.DB_STATE, 10);
    }

    @Test
    void create_derivesExamFromEnvironmentWithoutClientSendingIt() {
        Exam exam = newExam("TASK-CREATE");
        TaskEnvironment environment = newEnvironment(exam, "Own schema");

        Task created = taskService.create(ddlTaskDto(environment, 1));

        assertNotNull(created.getId());
        assertEquals(exam.getId(), created.getExam().getId());
        assertEquals(environment.getId(), created.getEnvironment().getId());
        assertEquals(1, created.getPosition());
        assertEquals("Create the schema", created.getTitle());
        assertEquals(TaskType.SQL_DDL, created.getTaskType());
        assertEquals(SubmissionMode.DB_STATE, created.getSubmissionMode());
        assertEquals(10, created.getPoints());

        Task found = taskService.findByIdNotNull(created.getId());
        assertEquals(exam.getId(), found.getExam().getId());
    }

    @Test
    void create_atTakenPositionInSameExam_throwsTaskPositionAlreadyExists() {
        Exam exam = newExam("TASK-POS-DUP");
        TaskEnvironment environment = newEnvironment(exam, "Own schema");
        taskService.create(ddlTaskDto(environment, 1));

        assertThrows(TaskPositionAlreadyExistsException.class,
                () -> taskService.create(ddlTaskDto(environment, 1)));
    }

    @Test
    void create_atSamePositionInDifferentExam_succeeds() {
        Exam exam = newExam("TASK-POS-1");
        Exam otherExam = newExam("TASK-POS-2");
        taskService.create(ddlTaskDto(newEnvironment(exam, "First"), 1));

        Task created = taskService.create(ddlTaskDto(newEnvironment(otherExam, "Second"), 1));

        assertEquals(otherExam.getId(), created.getExam().getId());
        assertEquals(1, created.getPosition());
    }

    @Test
    void create_withRetiredEnvironment_throwsTaskEnvironmentRetired() {
        Exam exam = newExam("TASK-RETIRED-ENV");
        TaskEnvironment environment = newEnvironment(exam, "Retired");
        taskEnvironmentService.retireById(environment.getId());
        TaskEnvironment retired = taskEnvironmentService.findByIdNotNull(environment.getId());

        assertThrows(TaskEnvironmentRetiredException.class,
                () -> taskService.create(ddlTaskDto(retired, 1)));
    }

    @Test
    void create_sqlQueryWithDbStateSubmission_throwsSubmissionModeInvalid() {
        Exam exam = newExam("TASK-QUERY-MODE");
        TaskEnvironment environment = newEnvironment(exam, "Own schema");

        assertThrows(TaskSubmissionModeInvalidException.class,
                () -> taskService.create(new TaskDto(environment, 1, "Query the data",
                        "Select all students.", TaskType.SQL_QUERY, SubmissionMode.DB_STATE, 5)));
    }

    @Test
    void create_sqlQueryWithSqlTextSubmission_succeeds() {
        Exam exam = newExam("TASK-QUERY-OK");
        TaskEnvironment environment = newEnvironment(exam, "Own schema");

        Task created = taskService.create(new TaskDto(environment, 1, "Query the data",
                "Select all students.", TaskType.SQL_QUERY, SubmissionMode.SQL_TEXT, 5));

        assertEquals(TaskType.SQL_QUERY, created.getTaskType());
        assertEquals(SubmissionMode.SQL_TEXT, created.getSubmissionMode());
    }

    @Test
    void update_repointToEnvironmentOfSameExam_succeeds() {
        Exam exam = newExam("TASK-REPOINT");
        TaskEnvironment first = newEnvironment(exam, "First");
        TaskEnvironment second = newEnvironment(exam, "Second");
        Task created = taskService.create(ddlTaskDto(first, 1));

        Task updated = taskService.update(created.getId(), ddlTaskDto(second, 1));

        assertEquals(created.getId(), updated.getId());
        assertEquals(second.getId(), updated.getEnvironment().getId());
        assertEquals(exam.getId(), updated.getExam().getId());
    }

    @Test
    void update_repointToEnvironmentOfDifferentExam_throwsExamMismatch() {
        Exam exam = newExam("TASK-TELEPORT-1");
        Exam otherExam = newExam("TASK-TELEPORT-2");
        TaskEnvironment home = newEnvironment(exam, "Home");
        TaskEnvironment foreign = newEnvironment(otherExam, "Foreign");
        Task created = taskService.create(ddlTaskDto(home, 1));

        assertThrows(TaskEnvironmentExamMismatchException.class,
                () -> taskService.update(created.getId(), ddlTaskDto(foreign, 1)));
    }

    @Test
    void update_repointToRetiredEnvironmentOfSameExam_throwsTaskEnvironmentRetired() {
        Exam exam = newExam("TASK-REPOINT-RETIRED");
        TaskEnvironment first = newEnvironment(exam, "First");
        TaskEnvironment second = newEnvironment(exam, "Second");
        taskEnvironmentService.retireById(second.getId());
        TaskEnvironment retired = taskEnvironmentService.findByIdNotNull(second.getId());
        Task created = taskService.create(ddlTaskDto(first, 1));

        assertThrows(TaskEnvironmentRetiredException.class,
                () -> taskService.update(created.getId(), ddlTaskDto(retired, 1)));
    }

    @Test
    void update_toTakenPositionInSameExam_throwsTaskPositionAlreadyExists() {
        Exam exam = newExam("TASK-UPD-POS");
        TaskEnvironment environment = newEnvironment(exam, "Own schema");
        taskService.create(ddlTaskDto(environment, 1));
        Task second = taskService.create(ddlTaskDto(environment, 2));

        assertThrows(TaskPositionAlreadyExistsException.class,
                () -> taskService.update(second.getId(), ddlTaskDto(environment, 1)));
    }

    @Test
    void update_keepingOwnPosition_succeeds() {
        Exam exam = newExam("TASK-UPD-KEEP");
        TaskEnvironment environment = newEnvironment(exam, "Own schema");
        Task created = taskService.create(ddlTaskDto(environment, 1));

        Task updated = taskService.update(created.getId(), new TaskDto(environment, 1,
                "Create the schema v2", "Updated text.", TaskType.SQL_DDL, SubmissionMode.DB_STATE, 15));

        assertEquals(1, updated.getPosition());
        assertEquals("Create the schema v2", updated.getTitle());
        assertEquals(15, updated.getPoints());
    }

    @Test
    void update_sqlQueryWithDbStateSubmission_throwsSubmissionModeInvalid() {
        Exam exam = newExam("TASK-UPD-QUERY");
        TaskEnvironment environment = newEnvironment(exam, "Own schema");
        Task created = taskService.create(ddlTaskDto(environment, 1));

        assertThrows(TaskSubmissionModeInvalidException.class,
                () -> taskService.update(created.getId(), new TaskDto(environment, 1,
                        "Query the data", "Select all.", TaskType.SQL_QUERY, SubmissionMode.DB_STATE, 5)));
    }

    @Test
    void retireEnvironment_thatTasksPointAt_throwsTaskEnvironmentInUse() {
        Exam exam = newExam("TASK-ENV-IN-USE");
        TaskEnvironment environment = newEnvironment(exam, "Referenced");
        taskService.create(ddlTaskDto(environment, 1));

        assertThrows(TaskEnvironmentInUseException.class,
                () -> taskEnvironmentService.retireById(environment.getId()));
    }

    @Test
    void retireEnvironment_afterItsTasksAreDeleted_succeeds() {
        Exam exam = newExam("TASK-ENV-FREED");
        TaskEnvironment environment = newEnvironment(exam, "Freed");
        Task task = taskService.create(ddlTaskDto(environment, 1));
        taskService.deleteById(task.getId());

        TaskEnvironment retired = taskEnvironmentService.retireById(environment.getId());

        assertFalse(retired.isActive());
    }

    @Test
    void deleteById_removesTaskAndReturnsIt() {
        Exam exam = newExam("TASK-DEL");
        TaskEnvironment environment = newEnvironment(exam, "Own schema");
        Task created = taskService.create(ddlTaskDto(environment, 1));

        Task deleted = taskService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertThrows(TaskDoesNotExistException.class,
                () -> taskService.findByIdNotNull(created.getId()));
    }

    @Test
    void findByIdNotNull_missingId_throwsTaskDoesNotExist() {
        assertThrows(TaskDoesNotExistException.class,
                () -> taskService.findByIdNotNull("missing-task-id"));
    }
}
