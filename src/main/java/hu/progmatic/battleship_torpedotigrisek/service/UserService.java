package hu.progmatic.battleship_torpedotigrisek.service;

import hu.progmatic.battleship_torpedotigrisek.model.User;
import hu.progmatic.battleship_torpedotigrisek.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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


    public void addUser(User user) {
        userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public List<User> getAllUserSortedByScore() {
        List<User> users = getAllUsers();
        users.sort(Comparator.comparing(u -> u.getUserProfile().getScore()));
        Collections.reverse(users);
        return users;
    }

    public Optional<User> getUserById(Long id){
        return userRepo.findById(id);
    }

    public User fingUserById(Long id){
        return userRepo.findById(id).orElse(null);
    }

    public User updateUser(User user){
        return userRepo.save(user);
    }

}