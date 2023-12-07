package hu.progmatic.battleship_torpedotigrisek.controller;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.service.GameService;
import hu.progmatic.battleship_torpedotigrisek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;
    private final GameService gameService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, GameService gameService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.gameService = gameService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/update")
    public String updateUser(Model model) {
        Long userId = gameService.getCurrentUserId();
        model.addAttribute("user", userService.fingUserById(userId));
        return "update-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User updatedUser,
                             @RequestParam String confirmPassword,
                             RedirectAttributes redirectAttributes) {
        if (!confirmPassword.equals(updatedUser.getPassword())) {

            redirectAttributes.addFlashAttribute("error", "A jelszavak nem egyeznek meg.");
            return "redirect:/update"; // Vagy valamilyen másik oldalra irányítsd a felhasználót
        }


        String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
        updatedUser.setPassword(encodedPassword);
        userService.updateUser(updatedUser);

        return "redirect:/login";
    }
}
