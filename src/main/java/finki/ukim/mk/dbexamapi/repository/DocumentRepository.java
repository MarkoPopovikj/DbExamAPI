package finki.ukim.mk.dbexamapi.repository;

import finki.ukim.mk.dbexamapi.domain.models.content.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {
}
