package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.DatabaseTemplateDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskEnvironmentDto;
import finki.ukim.mk.dbexamapi.domain.enums.EnvironmentMode;
import finki.ukim.mk.dbexamapi.domain.enums.ResetPolicy;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateInUseException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateRetiredException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentCombinationInvalidException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentExamImmutableException;
import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TaskEnvironmentServiceImplIT {

    @Autowired
    private TaskEnvironmentService taskEnvironmentService;

    @Autowired
    private DatabaseTemplateService databaseTemplateService;

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

    private DatabaseTemplate newTemplate(String templateDbName) {
        return databaseTemplateService.create(new DatabaseTemplateDto(
                "Northwind", templateDbName, "CREATE TABLE students (id SERIAL PRIMARY KEY);"));
    }

    private TaskEnvironmentDto emptyPerStudentDto(Exam exam) {
        return new TaskEnvironmentDto(exam, "Own schema", null,
                EnvironmentMode.PER_STUDENT, null, ResetPolicy.NONE, null, null);
    }

    @Test
    void create_emptyPerStudentEnvironment_savesActiveEnvironment() {
        Exam exam = newExam("ENV-CREATE");

        TaskEnvironment created = taskEnvironmentService.create(new TaskEnvironmentDto(
                exam, "Own schema", "Students build their own schema.",
                EnvironmentMode.PER_STUDENT, null, ResetPolicy.NONE, null, null));

        assertNotNull(created.getId());
        assertEquals(exam.getId(), created.getExam().getId());
        assertEquals("Own schema", created.getName());
        assertEquals("Students build their own schema.", created.getDescription());
        assertEquals(EnvironmentMode.PER_STUDENT, created.getMode());
        assertNull(created.getTemplate());
        assertEquals(ResetPolicy.NONE, created.getResetPolicy());
        assertNull(created.getPoolSize());
        assertNull(created.getAutoPopulateScript());
        assertTrue(created.isActive());
    }

    @Test
    void create_sharedFromTemplateWithRecreate_succeeds() {
        Exam exam = newExam("ENV-SHARED");
        DatabaseTemplate template = newTemplate("env_tpl_shared");

        TaskEnvironment created = taskEnvironmentService.create(new TaskEnvironmentDto(
                exam, "Shared Northwind", null,
                EnvironmentMode.SHARED, template, ResetPolicy.RECREATE, null, null));

        assertEquals(EnvironmentMode.SHARED, created.getMode());
        assertEquals(template.getId(), created.getTemplate().getId());
        assertEquals(ResetPolicy.RECREATE, created.getResetPolicy());
    }

    @Test
    void create_swapFromPoolWithPoolSize_succeeds() {
        Exam exam = newExam("ENV-POOL");
        DatabaseTemplate template = newTemplate("env_tpl_pool");

        TaskEnvironment created = taskEnvironmentService.create(new TaskEnvironmentDto(
                exam, "Pooled", null,
                EnvironmentMode.PER_STUDENT, template, ResetPolicy.SWAP_FROM_POOL, 3, null));

        assertEquals(ResetPolicy.SWAP_FROM_POOL, created.getResetPolicy());
        assertEquals(3, created.getPoolSize());
    }

    @Test
    void create_sharedWithSwapFromPool_throwsCombinationInvalid() {
        Exam exam = newExam("ENV-SHARED-SWAP");
        DatabaseTemplate template = newTemplate("env_tpl_shared_swap");

        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "Broken", null,
                        EnvironmentMode.SHARED, template, ResetPolicy.SWAP_FROM_POOL, 2, null)));
    }

    @Test
    void create_sharedWithoutTemplate_throwsCombinationInvalid() {
        Exam exam = newExam("ENV-SHARED-EMPTY");

        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "Dead shared", null,
                        EnvironmentMode.SHARED, null, ResetPolicy.NONE, null, null)));
    }

    @Test
    void create_swapFromPoolWithoutPoolSize_throwsCombinationInvalid() {
        Exam exam = newExam("ENV-SWAP-NO-POOL");

        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "No pool", null,
                        EnvironmentMode.PER_STUDENT, null, ResetPolicy.SWAP_FROM_POOL, null, null)));
        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "Zero pool", null,
                        EnvironmentMode.PER_STUDENT, null, ResetPolicy.SWAP_FROM_POOL, 0, null)));
    }

    @Test
    void create_poolSizeWithoutSwapFromPool_throwsCombinationInvalid() {
        Exam exam = newExam("ENV-POOL-NO-SWAP");

        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "Pointless pool", null,
                        EnvironmentMode.PER_STUDENT, null, ResetPolicy.NONE, 2, null)));
        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "Pointless pool", null,
                        EnvironmentMode.PER_STUDENT, null, ResetPolicy.RECREATE, 2, null)));
    }

    @Test
    void create_autoPopulateScriptTogetherWithTemplate_throwsCombinationInvalid() {
        Exam exam = newExam("ENV-AUTO-TPL");
        DatabaseTemplate template = newTemplate("env_tpl_auto");

        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "Both origins", null,
                        EnvironmentMode.PER_STUDENT, template, ResetPolicy.NONE, null,
                        "INSERT INTO students VALUES (1);")));
    }

    @Test
    void create_autoPopulateScriptWithoutTemplate_succeeds() {
        Exam exam = newExam("ENV-AUTO");

        TaskEnvironment created = taskEnvironmentService.create(new TaskEnvironmentDto(
                exam, "Student DDL then data", null,
                EnvironmentMode.PER_STUDENT, null, ResetPolicy.NONE, null,
                "INSERT INTO students VALUES (1);"));

        assertEquals("INSERT INTO students VALUES (1);", created.getAutoPopulateScript());
    }

    @Test
    void create_withRetiredTemplate_throwsDatabaseTemplateRetired() {
        Exam exam = newExam("ENV-RETIRED-TPL");
        DatabaseTemplate template = newTemplate("env_tpl_retired");
        databaseTemplateService.retireById(template.getId());

        assertThrows(DatabaseTemplateRetiredException.class,
                () -> taskEnvironmentService.create(new TaskEnvironmentDto(
                        exam, "Resurrection", null,
                        EnvironmentMode.PER_STUDENT, template, ResetPolicy.NONE, null, null)));
    }

    @Test
    void update_replacesFieldsWithinSameExam() {
        Exam exam = newExam("ENV-UPD");
        DatabaseTemplate template = newTemplate("env_tpl_upd");
        TaskEnvironment created = taskEnvironmentService.create(emptyPerStudentDto(exam));

        TaskEnvironment updated = taskEnvironmentService.update(created.getId(), new TaskEnvironmentDto(
                exam, "Shared Northwind", "Now shared.",
                EnvironmentMode.SHARED, template, ResetPolicy.RECREATE, null, null));

        assertEquals(created.getId(), updated.getId());
        assertEquals(exam.getId(), updated.getExam().getId());
        assertEquals("Shared Northwind", updated.getName());
        assertEquals("Now shared.", updated.getDescription());
        assertEquals(EnvironmentMode.SHARED, updated.getMode());
        assertEquals(template.getId(), updated.getTemplate().getId());
        assertEquals(ResetPolicy.RECREATE, updated.getResetPolicy());
    }

    @Test
    void update_changingExam_throwsExamImmutable() {
        Exam exam = newExam("ENV-IMMUT-1");
        Exam otherExam = newExam("ENV-IMMUT-2");
        TaskEnvironment created = taskEnvironmentService.create(emptyPerStudentDto(exam));

        assertThrows(TaskEnvironmentExamImmutableException.class,
                () -> taskEnvironmentService.update(created.getId(), emptyPerStudentDto(otherExam)));
    }

    @Test
    void update_violatingCombinationMatrix_throwsCombinationInvalid() {
        Exam exam = newExam("ENV-UPD-MATRIX");
        TaskEnvironment created = taskEnvironmentService.create(emptyPerStudentDto(exam));

        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.update(created.getId(), new TaskEnvironmentDto(
                        exam, "Dead shared", null,
                        EnvironmentMode.SHARED, null, ResetPolicy.NONE, null, null)));
        assertThrows(TaskEnvironmentCombinationInvalidException.class,
                () -> taskEnvironmentService.update(created.getId(), new TaskEnvironmentDto(
                        exam, "No pool", null,
                        EnvironmentMode.PER_STUDENT, null, ResetPolicy.SWAP_FROM_POOL, null, null)));
    }

    @Test
    void update_toRetiredTemplate_throwsDatabaseTemplateRetired() {
        Exam exam = newExam("ENV-UPD-RETIRED-TPL");
        DatabaseTemplate template = newTemplate("env_tpl_upd_retired");
        databaseTemplateService.retireById(template.getId());
        TaskEnvironment created = taskEnvironmentService.create(emptyPerStudentDto(exam));

        assertThrows(DatabaseTemplateRetiredException.class,
                () -> taskEnvironmentService.update(created.getId(), new TaskEnvironmentDto(
                        exam, "Resurrection", null,
                        EnvironmentMode.PER_STUDENT, template, ResetPolicy.NONE, null, null)));
    }

    @Test
    void retireById_excludesFromListWhileIdLookupStillResolves() {
        Exam exam = newExam("ENV-RETIRE");
        TaskEnvironment created = taskEnvironmentService.create(emptyPerStudentDto(exam));

        TaskEnvironment retired = taskEnvironmentService.retireById(created.getId());

        assertEquals(created.getId(), retired.getId());
        assertFalse(retired.isActive());
        assertTrue(taskEnvironmentService.findAll().stream()
                .noneMatch(environment -> environment.getId().equals(created.getId())));
        TaskEnvironment found = taskEnvironmentService.findByIdNotNull(created.getId());
        assertFalse(found.isActive());
    }

    @Test
    void activateById_makesRetiredEnvironmentActiveAgain() {
        Exam exam = newExam("ENV-REACTIVATE");
        TaskEnvironment created = taskEnvironmentService.create(emptyPerStudentDto(exam));
        taskEnvironmentService.retireById(created.getId());

        TaskEnvironment activated = taskEnvironmentService.activateById(created.getId());

        assertTrue(activated.isActive());
        assertTrue(taskEnvironmentService.findAll().stream()
                .anyMatch(environment -> environment.getId().equals(created.getId())));
    }

    @Test
    void retireTemplate_referencedByActiveEnvironment_throwsDatabaseTemplateInUse() {
        Exam exam = newExam("ENV-TPL-IN-USE");
        DatabaseTemplate template = newTemplate("env_tpl_in_use");
        taskEnvironmentService.create(new TaskEnvironmentDto(
                exam, "Shared Northwind", null,
                EnvironmentMode.SHARED, template, ResetPolicy.NONE, null, null));

        assertThrows(DatabaseTemplateInUseException.class,
                () -> databaseTemplateService.retireById(template.getId()));
    }

    @Test
    void retireTemplate_referencedOnlyByRetiredEnvironments_succeeds() {
        Exam exam = newExam("ENV-TPL-FREED");
        DatabaseTemplate template = newTemplate("env_tpl_freed");
        TaskEnvironment environment = taskEnvironmentService.create(new TaskEnvironmentDto(
                exam, "Shared Northwind", null,
                EnvironmentMode.SHARED, template, ResetPolicy.NONE, null, null));
        taskEnvironmentService.retireById(environment.getId());

        DatabaseTemplate retired = databaseTemplateService.retireById(template.getId());

        assertFalse(retired.isActive());
    }

    @Test
    void findByIdNotNull_missingId_throwsTaskEnvironmentDoesNotExist() {
        assertThrows(TaskEnvironmentDoesNotExistException.class,
                () -> taskEnvironmentService.findByIdNotNull("missing-environment-id"));
    }
}
