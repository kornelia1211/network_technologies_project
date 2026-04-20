package edu.bi.springdemo.Controller;

import edu.bi.springdemo.entity.User;
import edu.bi.springdemo.exception.InvalidDataException;
import edu.bi.springdemo.exception.UserAlreadyExistsException;
import edu.bi.springdemo.exception.UserDoesNotExistException;
import edu.bi.springdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/add")
    @ResponseStatus(code = HttpStatus.CREATED)
    public @ResponseBody User addUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public User updateUser(@PathVariable Integer id, @RequestBody Map<String, Object> updates){
        return userService.updateUser(id, updates);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public void deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
    }

    @GetMapping("/getAll")
    public @ResponseBody Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserException(UserDoesNotExistException e){
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return map;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleUserExists(UserAlreadyExistsException e){
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return map;
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidData(InvalidDataException e){
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return map;
    }
}