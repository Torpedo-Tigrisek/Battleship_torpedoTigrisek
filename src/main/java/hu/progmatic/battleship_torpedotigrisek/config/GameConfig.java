package hu.progmatic.battleship_torpedotigrisek.config;

import hu.progmatic.battleship_torpedotigrisek.model.Game;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class GameConfig {
    @Bean
    public Game gameBean(){
        return new Game();
    }

}