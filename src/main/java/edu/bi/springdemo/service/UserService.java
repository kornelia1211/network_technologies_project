package edu.bi.springdemo.service;

import edu.bi.springdemo.entity.User;
import edu.bi.springdemo.Repositories.UserRepository;
import edu.bi.springdemo.exception.UserAlreadyExistsException;
import edu.bi.springdemo.exception.UserDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

    @Transactional
    public User updateUser(Integer id, Map<String, Object> updates) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserDoesNotExistException.create("User with id " + id + " does not exist"));

        if (updates.containsKey("username")) {
            String newUsername = (String) updates.get("username");
            if (!newUsername.equals(user.getUsername()) &&
                    !userRepository.findUserByUsername(newUsername).isEmpty()) {
                throw UserAlreadyExistsException.create("Username already taken");
            }
            user.setUsername(newUsername);
        }

        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            if (!newEmail.equals(user.getEmail()) &&
                    !userRepository.findUserByEmail(newEmail).isEmpty()) {
                throw UserAlreadyExistsException.create("Email already taken");
            }
            user.setEmail(newEmail);
        }

        if (updates.containsKey("name")) {
            user.setName((String) updates.get("name"));
        }

        if (updates.containsKey("role")) {
            user.setRole((String) updates.get("role"));
        }

        if (updates.containsKey("password")) {
            user.setPassword(passwordEncoder.encode((String) updates.get("password")));
        }

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