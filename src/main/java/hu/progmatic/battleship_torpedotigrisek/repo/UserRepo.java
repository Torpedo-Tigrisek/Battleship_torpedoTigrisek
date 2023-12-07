package hu.progmatic.battleship_torpedotigrisek.repo;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    User findUserByName(String name);
}