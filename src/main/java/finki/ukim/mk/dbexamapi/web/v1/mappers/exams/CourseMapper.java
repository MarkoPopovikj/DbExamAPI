package finki.ukim.mk.dbexamapi.web.v1.mappers.exams;

import finki.ukim.mk.dbexamapi.service.exams.CourseService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.exams.CourseExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.CourseRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.CourseResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseMapper {

    private final CourseService courseService;

    public CourseMapper(CourseService courseService) {
        this.courseService = courseService;
    }

    public List<CourseResponse> findAll() {
        return CourseExtensions.toResponse(courseService.findAll());
    }

    public CourseResponse findById(String id) {
        return CourseExtensions.toResponse(courseService.findByIdNotNull(id));
    }

    public CourseResponse create(CourseRequest request) {
        return CourseExtensions.toResponse(courseService.create(CourseExtensions.toDto(request)));
    }

    public CourseResponse update(String id, CourseRequest request) {
        return CourseExtensions.toResponse(courseService.update(id, CourseExtensions.toDto(request)));
    }

    public CourseResponse deleteById(String id) {
        return CourseExtensions.toResponse(courseService.deleteById(id));
    }
}
