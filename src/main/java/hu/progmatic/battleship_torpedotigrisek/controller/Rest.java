package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.service.UserProfileService;
import hu.progmatic.battleship_torpedotigrisek.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Rest {
    private UserService userService;
    private UserProfileService userProfileService;

    public Rest(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/users")
    public List<User> getUsersPage(){
        return userService.getAllUsers();
    }
}
