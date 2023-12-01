package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.User;


import hu.progmatic.battleship_torpedotigrisek.model.UserProfile;
import hu.progmatic.battleship_torpedotigrisek.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public void addUserProfile(UserProfile userProfile){
        userProfileRepository.save(userProfile);
    }

}
