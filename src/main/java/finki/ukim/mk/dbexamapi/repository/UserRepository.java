package finki.ukim.mk.dbexamapi.repository;

import finki.ukim.mk.dbexamapi.domain.models.identity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
