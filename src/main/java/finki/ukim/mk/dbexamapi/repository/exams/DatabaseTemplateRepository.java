package finki.ukim.mk.dbexamapi.repository.exams;

import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseTemplateRepository extends JpaRepository<DatabaseTemplate, String> {

    List<DatabaseTemplate> findAllByActiveTrue();

    boolean existsByTemplateDbName(String templateDbName);

    boolean existsByTemplateDbNameAndIdNot(String templateDbName, String id);
}
