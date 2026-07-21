package finki.ukim.mk.dbexamapi.repository.identity;

import finki.ukim.mk.dbexamapi.domain.models.identity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, String> {

    Optional<UserDetail> findByEmail(String email);

    boolean existsByUser_Id(String userId);

    boolean existsByUser_IdAndIdNot(String userId, String id);

    boolean existsByIndex(String index);

    boolean existsByIndexAndIdNot(String index, String id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, String id);
}
