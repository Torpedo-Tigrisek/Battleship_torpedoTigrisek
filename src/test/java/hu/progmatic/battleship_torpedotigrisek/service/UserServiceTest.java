package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.repo.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    // mockoljuk ebben a classban szerepló repo-t,
    // mert azt már elszigetelve unit tesztelük, így elég lemockolni
    // így gyors lesz a tesztelésünk
    private AutoCloseable autoCloseable;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        // ez mindenegyes teszt előtt megnyitja a lemockolt database-t
        // ez egy autoClosable értéket az vissza, ezért rendeltük ki a teszt fieljei közé változóként
        underTest = new UserService(userRepo);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
        // bezárja a forrást a mindenegyes tesztelés után
    }

    @Test
    void canLoadUserByUsername() {
        // given
        String username = "Andras";
        User user = new User(null, username, "andras@andras.hu", "andras");
        when(userRepo.findByName(username)).thenReturn(Optional.of(user));

        // when
        UserDetails foundUser = underTest.loadUserByUsername(username);

        // then
        assertEquals(username, foundUser.getUsername());
    }
    @Test
    void cannotLoadUserByUsername() {
        // given
        String username = "Andras";
        User user = new User(null, username, "andras@andras.hu", "andras");

        // when and then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername(username));
        assertEquals("USER_NOT_FOUND: " + username, exception.getMessage());
    }


    @Test
    void canAddAUser() {
        // given
        User user = new User(null, "Andras", "andras@andras.hu", "andras");

        // when
        underTest.addUser(user);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userArgumentCaptor.capture());
        // azt akarjuk ellenőrizni, hogy a userRepo a save metódussal lett meghívva,
        // amiben egy olyan user-t mentünk el, amit elkell csípni a capture metódussal
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void canGetAllUsers() {
        // when
        underTest.getAllUsers();

        // then
        verify(userRepo).findAll();
        // a getAllUsers metódusban a .findAll() parancsot hívjuk meg,
        // összeveti a verify és az eredeti metódus működését ez a tesztrész

    }
}