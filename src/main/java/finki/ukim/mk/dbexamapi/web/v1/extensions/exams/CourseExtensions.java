package finki.ukim.mk.dbexamapi.web.v1.extensions.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.CourseDto;
import finki.ukim.mk.dbexamapi.domain.models.Course;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.CourseRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.CourseResponse;

import java.util.List;

public final class CourseExtensions {

    private CourseExtensions() {
    }

    public static CourseDto toDto(CourseRequest request) {
        return new CourseDto(request.code(), request.name());
    }

    public static CourseResponse toResponse(Course course) {
        return new CourseResponse(course.getId(), course.getCode(), course.getName());
    }

    public static List<CourseResponse> toResponse(List<Course> courses) {
        return courses.stream()
                .map(CourseExtensions::toResponse)
                .toList();
    }
}
