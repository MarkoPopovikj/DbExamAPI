package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamDto;
import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamParticipantDto;
import finki.ukim.mk.dbexamapi.domain.dtos.identity.UserDto;
import finki.ukim.mk.dbexamapi.domain.enums.UserRole;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.ExamParticipantAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.ExamParticipantDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.domain.models.exams.ExamParticipant;
import finki.ukim.mk.dbexamapi.domain.models.identity.User;
import finki.ukim.mk.dbexamapi.service.identity.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ExamParticipantServiceImplIT {

    @Autowired
    private ExamParticipantService examParticipantService;

    @Autowired
    private ExamService examService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    private Exam newExam(String courseCode) {
        Instant startsAt = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        return examService.create(new ExamDto(
                courseService.create(new CourseDto(courseCode, "Databases")),
                "Midterm", startsAt, startsAt.plus(2, ChronoUnit.HOURS)));
    }

    private User newUser() {
        return userService.create(new UserDto(UserRole.STUDENT));
    }

    @Test
    void create_savesExamParticipantAndReturnsIt() {
        Exam exam = newExam("EP-CREATE");
        User user = newUser();

        ExamParticipant created = examParticipantService.create(
                new ExamParticipantDto(exam, user, true));

        assertNotNull(created.getId());
        assertEquals(exam.getId(), created.getExam().getId());
        assertEquals(user.getId(), created.getUser().getId());
        assertTrue(created.isAdmin());

        ExamParticipant found = examParticipantService.findByIdNotNull(created.getId());
        assertEquals(exam.getId(), found.getExam().getId());
        assertEquals(user.getId(), found.getUser().getId());
        assertTrue(found.isAdmin());
    }

    @Test
    void create_sameExamAndUserTwice_throwsExamParticipantAlreadyExists() {
        Exam exam = newExam("EP-DUP");
        User user = newUser();
        examParticipantService.create(new ExamParticipantDto(exam, user, false));

        assertThrows(ExamParticipantAlreadyExistsException.class,
                () -> examParticipantService.create(new ExamParticipantDto(exam, user, true)));
    }

    @Test
    void update_keepingOwnExamAndUser_succeeds() {
        Exam exam = newExam("EP-KEEP");
        User user = newUser();
        ExamParticipant created = examParticipantService.create(
                new ExamParticipantDto(exam, user, false));

        ExamParticipant updated = examParticipantService.update(created.getId(),
                new ExamParticipantDto(exam, user, true));

        assertEquals(created.getId(), updated.getId());
        assertEquals(exam.getId(), updated.getExam().getId());
        assertEquals(user.getId(), updated.getUser().getId());
        assertTrue(updated.isAdmin());
    }

    @Test
    void update_toAnotherRowsExamAndUser_throwsExamParticipantAlreadyExists() {
        Exam exam = newExam("EP-TAKEN");
        User takenUser = newUser();
        examParticipantService.create(new ExamParticipantDto(exam, takenUser, false));
        ExamParticipant other = examParticipantService.create(
                new ExamParticipantDto(exam, newUser(), false));

        assertThrows(ExamParticipantAlreadyExistsException.class,
                () -> examParticipantService.update(other.getId(),
                        new ExamParticipantDto(exam, takenUser, false)));
    }

    @Test
    void deleteById_removesExamParticipantAndReturnsIt() {
        Exam exam = newExam("EP-DEL");
        User user = newUser();
        ExamParticipant created = examParticipantService.create(
                new ExamParticipantDto(exam, user, false));

        ExamParticipant deleted = examParticipantService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertFalse(deleted.isAdmin());
        assertThrows(ExamParticipantDoesNotExistException.class,
                () -> examParticipantService.findByIdNotNull(created.getId()));
    }
}
