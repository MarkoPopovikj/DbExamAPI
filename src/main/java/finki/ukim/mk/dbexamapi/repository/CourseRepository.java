package finki.ukim.mk.dbexamapi.repository;

import finki.ukim.mk.dbexamapi.domain.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, String id);
}
