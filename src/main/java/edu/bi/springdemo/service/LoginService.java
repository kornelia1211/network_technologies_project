package edu.bi.springdemo.service;

import edu.bi.springdemo.Repositories.UserRepository;
import edu.bi.springdemo.entity.User;
import edu.bi.springdemo.exception.LoginPasswordException;
import edu.bi.springdemo.security.JwtTokenService;
import edu.bi.springdemo.security.PasswordEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class LoginService {

    private final UserRepository userRepository;

    private final PasswordEncoderConfig passwordEncoderConfig;

    private final JwtTokenService jwtTokenService;

    @Autowired
    public LoginService(UserRepository userRepository, PasswordEncoderConfig passwordEncoder,
                        JwtTokenService jwtTokenService){
        this.userRepository = userRepository;
        this.passwordEncoderConfig = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    public String login(String username, String password){
        Collection<User> list = userRepository.findUserByUsername(username);
        if(list.isEmpty()){
            throw LoginPasswordException.create("Incorrect login or password");
        } else {
            User user = list.iterator().next();
            if(passwordEncoderConfig.passwordEncoder().matches(password, user.getPassword())){
                return jwtTokenService.generateToken(username, user.getRole());
            }else {
                throw LoginPasswordException.create("Incorrect login or password");
            }
        }
    }
}
