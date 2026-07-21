package finki.ukim.mk.dbexamapi.repository.exams;

import finki.ukim.mk.dbexamapi.domain.models.exams.ManagedDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedDatabaseRepository extends JpaRepository<ManagedDatabase, String> {
}
