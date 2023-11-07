package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByName(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                String.format("USER_NOT_FOUND: " + username)
                        )
                );
    }


    public void save(User user) {
        userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}