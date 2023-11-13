package hu.progmatic.battleship_torpedotigrisek.repo;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
class UserRepoTest {

    @Autowired
    private UserRepo underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }
    @Test
    void itShouldFindUserByName() {
        // given
        User user = new User(null, "Andras", "andras@andras.hu", "andras");
        underTest.save(user);

        // when
        Optional<User> foundUser = underTest.findByName(user.getName());

        // then
        assertThat(foundUser).isEqualTo(Optional.of(user));
    }

    @Test
    void itShouldNotFindUserByName() {
        // given
        User user = new User(null, "Andras", "andras@andras.hu", "andras");

        // when
        Optional<User> foundUser = underTest.findByName(user.getName());

        // then
        assertThat(foundUser).isEmpty();
    }

}