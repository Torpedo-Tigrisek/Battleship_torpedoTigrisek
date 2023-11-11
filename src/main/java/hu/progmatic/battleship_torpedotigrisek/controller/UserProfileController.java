
package hu.progmatic.battleship_torpedotigrisek.controller;


import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.model.UserProfile;
import hu.progmatic.battleship_torpedotigrisek.service.UserProfileService;
import hu.progmatic.battleship_torpedotigrisek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserService userService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService, UserService userService) {
        this.userProfileService = userProfileService;

        this.userService = userService;
    }

    @GetMapping("/userProfile")
    public String userProfile(Model model, Principal principal) {
        String userName = principal.getName();
        UserProfile userProfile = userProfileService.getUserProfileByName(userName);
        model.addAttribute("userProfile", userProfile);
        return "profile";
    }
}