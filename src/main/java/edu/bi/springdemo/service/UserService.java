package edu.bi.springdemo.service;

import edu.bi.springdemo.entity.User;
import edu.bi.springdemo.Repositories.UserRepository;
import edu.bi.springdemo.exception.UserAlreadyExistsException;
import edu.bi.springdemo.exception.UserDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {

        if(!userRepository.findUserByUsername(user.getUsername()).isEmpty()){
            throw UserAlreadyExistsException.create("Username already taken");
        }

        if(!userRepository.findUserByEmail(user.getEmail()).isEmpty()){
            throw UserAlreadyExistsException.create("Email already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Integer id){
        if(!userRepository.existsById(id)){
            throw UserDoesNotExistException.create("User with id " + id + " does not exist");
        }
        userRepository.deleteById(id);
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }
}