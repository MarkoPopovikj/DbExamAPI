package finki.ukim.mk.dbexamapi.repository.content;

import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, String> {
}
