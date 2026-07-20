package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamDto;
import finki.ukim.mk.dbexamapi.domain.enums.ExamStatus;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.ExamDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.exams.Course;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ExamServiceImplIT {

    @Autowired
    private ExamService examService;

    @Autowired
    private CourseService courseService;

    private Course newCourse(String code) {
        return courseService.create(new CourseDto(code, "Databases"));
    }

    @Test
    void create_savesExamWithScheduledStatus() {
        Course course = newCourse("EX-CREATE");
        Instant startsAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Instant endsAt = startsAt.plus(2, ChronoUnit.HOURS);

        Exam created = examService.create(new ExamDto(course, "Midterm", startsAt, endsAt));

        assertNotNull(created.getId());
        assertEquals(course.getId(), created.getCourse().getId());
        assertEquals("Midterm", created.getName());
        assertEquals(startsAt, created.getStartsAt());
        assertEquals(endsAt, created.getEndsAt());
        assertEquals(ExamStatus.SCHEDULED, created.getStatus());

        Exam found = examService.findByIdNotNull(created.getId());
        assertEquals(ExamStatus.SCHEDULED, found.getStatus());
    }

    @Test
    void update_replacesFieldsButLeavesStatusUntouched() {
        Course course = newCourse("EX-UPD-1");
        Course otherCourse = newCourse("EX-UPD-2");
        Instant startsAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Exam created = examService.create(
                new ExamDto(course, "Midterm", startsAt, startsAt.plus(2, ChronoUnit.HOURS)));

        Instant newStartsAt = startsAt.plus(1, ChronoUnit.DAYS);
        Instant newEndsAt = newStartsAt.plus(3, ChronoUnit.HOURS);
        Exam updated = examService.update(created.getId(),
                new ExamDto(otherCourse, "Final", newStartsAt, newEndsAt));

        assertEquals(created.getId(), updated.getId());
        assertEquals(otherCourse.getId(), updated.getCourse().getId());
        assertEquals("Final", updated.getName());
        assertEquals(newStartsAt, updated.getStartsAt());
        assertEquals(newEndsAt, updated.getEndsAt());
        assertEquals(ExamStatus.SCHEDULED, updated.getStatus());
    }

    @Test
    void findByIdNotNull_missingId_throwsExamDoesNotExist() {
        assertThrows(ExamDoesNotExistException.class,
                () -> examService.findByIdNotNull("missing-exam-id"));
    }

    @Test
    void deleteById_removesExamAndReturnsIt() {
        Course course = newCourse("EX-DEL");
        Instant startsAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        Exam created = examService.create(
                new ExamDto(course, "Midterm", startsAt, startsAt.plus(2, ChronoUnit.HOURS)));

        Exam deleted = examService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertThrows(ExamDoesNotExistException.class,
                () -> examService.findByIdNotNull(created.getId()));
    }
}
