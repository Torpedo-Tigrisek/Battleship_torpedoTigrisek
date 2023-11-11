package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.model.UserProfile;
import hu.progmatic.battleship_torpedotigrisek.service.UserProfileService;
import hu.progmatic.battleship_torpedotigrisek.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class PageController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private final UserProfileService userProfileService;

    @GetMapping({"","/","/home"})
    public String getHome(){
        return "home";
    }

    @GetMapping("/play")
    public String getPlay(){
        return "play";
    }
    @GetMapping("/reg")
    public String getReg(Model model){
        model.addAttribute("newUser", new User());
        return "registration";
    }

    @PostMapping("/reg")
    public String saveUser(
            @ModelAttribute("newUser")
            User user
    ) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.save(user);
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(savedUser);
        userProfile.setScore(0);
        userProfile.setWins(0);
        userProfile.setLosses(0);
        userProfile.setWinLossRatio(0.0);
        userProfileService.saveUserProfile(userProfile);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }



    @GetMapping("/leaderboard")
    public String getLeaderBoard(){
        return "leaderboard";
    }




}
