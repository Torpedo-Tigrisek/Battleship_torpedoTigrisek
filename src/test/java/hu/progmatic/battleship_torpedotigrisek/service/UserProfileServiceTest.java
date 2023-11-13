package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.model.UserProfile;
import hu.progmatic.battleship_torpedotigrisek.repository.UserProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class UserProfileServiceTest {
    @Mock
    private UserProfileRepository userProfileRepository;

    private AutoCloseable autoCloseable;
    private UserProfileService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserProfileService(userProfileRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canAddUserProfile() {
        // given
        User user = new User(null, "Andras", "andras@andras.hu", "andras");
        UserProfile userProfile = new UserProfile(null, user, 0, 0, 0, 0.0 );

        // when
        underTest.addUserProfile(userProfile);

        // then
        ArgumentCaptor<UserProfile> userProfileArgumentCaptor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(userProfileArgumentCaptor.capture());
        UserProfile capturedUserProfile = userProfileArgumentCaptor.getValue();
        assertThat(capturedUserProfile).isEqualTo(userProfile);
    }
}