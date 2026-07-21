package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.DatabaseTemplateDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateDbNameAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateDbNameInvalidException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateSourceScriptRequiredException;
import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DatabaseTemplateServiceImplIT {

    private static final String SOURCE_SCRIPT = "CREATE TABLE students (id SERIAL PRIMARY KEY);";

    @Autowired
    private DatabaseTemplateService databaseTemplateService;

    private DatabaseTemplate newTemplate(String templateDbName) {
        return databaseTemplateService.create(
                new DatabaseTemplateDto("Northwind", templateDbName, SOURCE_SCRIPT));
    }

    @Test
    void create_savesActiveTemplate() {
        DatabaseTemplate created = newTemplate("tpl_create");

        assertNotNull(created.getId());
        assertEquals("Northwind", created.getName());
        assertEquals("tpl_create", created.getTemplateDbName());
        assertEquals(SOURCE_SCRIPT, created.getSourceScript());
        assertTrue(created.isActive());

        DatabaseTemplate found = databaseTemplateService.findByIdNotNull(created.getId());
        assertEquals("tpl_create", found.getTemplateDbName());
        assertTrue(found.isActive());
    }

    @Test
    void create_withoutSourceScript_throwsSourceScriptRequired() {
        assertThrows(DatabaseTemplateSourceScriptRequiredException.class,
                () -> databaseTemplateService.create(
                        new DatabaseTemplateDto("Northwind", "tpl_no_script", null)));
        assertThrows(DatabaseTemplateSourceScriptRequiredException.class,
                () -> databaseTemplateService.create(
                        new DatabaseTemplateDto("Northwind", "tpl_blank_script", "   ")));
    }

    @Test
    void create_withUnsafeTemplateDbName_throwsDbNameInvalid() {
        assertThrows(DatabaseTemplateDbNameInvalidException.class,
                () -> databaseTemplateService.create(
                        new DatabaseTemplateDto("Northwind", "Tpl_Upper", SOURCE_SCRIPT)));
        assertThrows(DatabaseTemplateDbNameInvalidException.class,
                () -> databaseTemplateService.create(
                        new DatabaseTemplateDto("Northwind", "tpl-hyphen", SOURCE_SCRIPT)));
        assertThrows(DatabaseTemplateDbNameInvalidException.class,
                () -> databaseTemplateService.create(
                        new DatabaseTemplateDto("Northwind", "1tpl_leading_digit", SOURCE_SCRIPT)));
        assertThrows(DatabaseTemplateDbNameInvalidException.class,
                () -> databaseTemplateService.create(
                        new DatabaseTemplateDto("Northwind", "tpl; drop database dbexam", SOURCE_SCRIPT)));
        assertThrows(DatabaseTemplateDbNameInvalidException.class,
                () -> databaseTemplateService.create(
                        new DatabaseTemplateDto("Northwind", "a".repeat(64), SOURCE_SCRIPT)));
    }

    @Test
    void create_templateDbNameAtMaxLength_succeeds() {
        DatabaseTemplate created = newTemplate("t" + "a".repeat(62));

        assertEquals(63, created.getTemplateDbName().length());
    }

    @Test
    void create_duplicateTemplateDbName_throwsDbNameAlreadyExists() {
        newTemplate("tpl_dup");

        assertThrows(DatabaseTemplateDbNameAlreadyExistsException.class,
                () -> newTemplate("tpl_dup"));
    }

    @Test
    void update_replacesNameDbNameAndSourceScript() {
        DatabaseTemplate created = newTemplate("tpl_upd_before");

        DatabaseTemplate updated = databaseTemplateService.update(created.getId(),
                new DatabaseTemplateDto("Sakila", "tpl_upd_after", "CREATE TABLE films (id SERIAL);"));

        assertEquals(created.getId(), updated.getId());
        assertEquals("Sakila", updated.getName());
        assertEquals("tpl_upd_after", updated.getTemplateDbName());
        assertEquals("CREATE TABLE films (id SERIAL);", updated.getSourceScript());
        assertTrue(updated.isActive());
    }

    @Test
    void update_withoutSourceScript_throwsSourceScriptRequired() {
        DatabaseTemplate created = newTemplate("tpl_upd_no_script");

        assertThrows(DatabaseTemplateSourceScriptRequiredException.class,
                () -> databaseTemplateService.update(created.getId(),
                        new DatabaseTemplateDto("Northwind", "tpl_upd_no_script", " ")));
    }

    @Test
    void update_withUnsafeTemplateDbName_throwsDbNameInvalid() {
        DatabaseTemplate created = newTemplate("tpl_upd_unsafe");

        assertThrows(DatabaseTemplateDbNameInvalidException.class,
                () -> databaseTemplateService.update(created.getId(),
                        new DatabaseTemplateDto("Northwind", "Tpl Upd Unsafe", SOURCE_SCRIPT)));
    }

    @Test
    void update_toAnotherRowsTemplateDbName_throwsDbNameAlreadyExists() {
        newTemplate("tpl_taken");
        DatabaseTemplate other = newTemplate("tpl_other");

        assertThrows(DatabaseTemplateDbNameAlreadyExistsException.class,
                () -> databaseTemplateService.update(other.getId(),
                        new DatabaseTemplateDto("Northwind", "tpl_taken", SOURCE_SCRIPT)));
    }

    @Test
    void update_keepingOwnTemplateDbName_succeeds() {
        DatabaseTemplate created = newTemplate("tpl_keep");

        DatabaseTemplate updated = databaseTemplateService.update(created.getId(),
                new DatabaseTemplateDto("Northwind v2", "tpl_keep", SOURCE_SCRIPT));

        assertEquals("Northwind v2", updated.getName());
        assertEquals("tpl_keep", updated.getTemplateDbName());
    }

    @Test
    void retireById_excludesFromListWhileIdLookupStillResolves() {
        DatabaseTemplate created = newTemplate("tpl_retire");

        DatabaseTemplate retired = databaseTemplateService.retireById(created.getId());

        assertEquals(created.getId(), retired.getId());
        assertFalse(retired.isActive());
        assertTrue(databaseTemplateService.findAll().stream()
                .noneMatch(template -> template.getId().equals(created.getId())));
        DatabaseTemplate found = databaseTemplateService.findByIdNotNull(created.getId());
        assertFalse(found.isActive());
    }

    @Test
    void activateById_makesRetiredTemplateActiveAgain() {
        DatabaseTemplate created = newTemplate("tpl_reactivate");
        databaseTemplateService.retireById(created.getId());

        DatabaseTemplate activated = databaseTemplateService.activateById(created.getId());

        assertTrue(activated.isActive());
        assertTrue(databaseTemplateService.findAll().stream()
                .anyMatch(template -> template.getId().equals(created.getId())));
    }

    @Test
    void findAll_returnsActiveTemplatesOnly() {
        DatabaseTemplate active = newTemplate("tpl_list_active");
        DatabaseTemplate retired = newTemplate("tpl_list_retired");
        databaseTemplateService.retireById(retired.getId());

        assertTrue(databaseTemplateService.findAll().stream()
                .anyMatch(template -> template.getId().equals(active.getId())));
        assertTrue(databaseTemplateService.findAll().stream()
                .noneMatch(template -> template.getId().equals(retired.getId())));
    }

    @Test
    void findByIdNotNull_missingId_throwsDatabaseTemplateDoesNotExist() {
        assertThrows(DatabaseTemplateDoesNotExistException.class,
                () -> databaseTemplateService.findByIdNotNull("missing-template-id"));
    }
}
