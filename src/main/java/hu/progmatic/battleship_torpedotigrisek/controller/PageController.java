package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class PageController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
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
        userService.save(user);
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

    @GetMapping({"","/","/welcome"})
    public String getWelcomePage(){
        return "welcome";
    }
}
