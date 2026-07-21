package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.models.exams.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Optional<Course> findById(String id);

    Course findByIdNotNull(String id);

    List<Course> findAll();

    Course create(CourseDto courseDto);

    Course update(String id, CourseDto courseDto);

    Course deleteById(String id);
}
