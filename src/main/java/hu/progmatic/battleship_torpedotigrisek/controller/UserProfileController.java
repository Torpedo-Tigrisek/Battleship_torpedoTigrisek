
package hu.progmatic.battleship_torpedotigrisek.controller;


import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.service.UserProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserProfileController {

    private UserProfileService userProfileService;

    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userProfileService.getUserProfileByName(userName);
        model.addAttribute("user", user);
        return "profile";
    }
}