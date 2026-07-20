package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.CourseCodeAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.CourseDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CourseServiceImplIT {

    @Autowired
    private CourseService courseService;

    @Test
    void create_savesCourseAndReturnsIt() {
        Course created = courseService.create(new CourseDto("DB-CREATE", "Databases"));

        assertNotNull(created.getId());
        assertEquals("DB-CREATE", created.getCode());
        assertEquals("Databases", created.getName());

        Course found = courseService.findByIdNotNull(created.getId());
        assertEquals("DB-CREATE", found.getCode());
        assertEquals("Databases", found.getName());
    }

    @Test
    void create_duplicateCode_throwsCourseCodeAlreadyExists() {
        courseService.create(new CourseDto("DB-DUP", "Databases"));

        assertThrows(CourseCodeAlreadyExistsException.class,
                () -> courseService.create(new CourseDto("DB-DUP", "Advanced Databases")));
    }

    @Test
    void findByIdNotNull_missingId_throwsCourseDoesNotExist() {
        assertThrows(CourseDoesNotExistException.class,
                () -> courseService.findByIdNotNull("missing-course-id"));
    }

    @Test
    void update_replacesCodeAndName() {
        Course created = courseService.create(new CourseDto("DB-UPD", "Databases"));

        Course updated = courseService.update(created.getId(), new CourseDto("DB-UPD-2", "Advanced Databases"));

        assertEquals(created.getId(), updated.getId());
        assertEquals("DB-UPD-2", updated.getCode());
        assertEquals("Advanced Databases", updated.getName());

        Course found = courseService.findByIdNotNull(created.getId());
        assertEquals("DB-UPD-2", found.getCode());
        assertEquals("Advanced Databases", found.getName());
    }

    @Test
    void update_toAnotherRowsCode_throwsCourseCodeAlreadyExists() {
        courseService.create(new CourseDto("DB-TAKEN", "Databases"));
        Course other = courseService.create(new CourseDto("DB-OTHER", "Advanced Databases"));

        assertThrows(CourseCodeAlreadyExistsException.class,
                () -> courseService.update(other.getId(), new CourseDto("DB-TAKEN", "Advanced Databases")));
    }

    @Test
    void update_keepingOwnCode_succeeds() {
        Course created = courseService.create(new CourseDto("DB-KEEP", "Databases"));

        Course updated = courseService.update(created.getId(), new CourseDto("DB-KEEP", "Databases Renamed"));

        assertEquals("DB-KEEP", updated.getCode());
        assertEquals("Databases Renamed", updated.getName());
    }

    @Test
    void deleteById_removesCourseAndReturnsIt() {
        Course created = courseService.create(new CourseDto("DB-DEL", "Databases"));

        Course deleted = courseService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertEquals("DB-DEL", deleted.getCode());
        assertThrows(CourseDoesNotExistException.class,
                () -> courseService.findByIdNotNull(created.getId()));
    }
}
