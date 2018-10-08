package murraco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import murraco.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);

}
