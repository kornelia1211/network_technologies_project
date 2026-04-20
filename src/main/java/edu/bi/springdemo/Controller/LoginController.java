package edu.bi.springdemo.Controller;

import com.google.gson.Gson;
import edu.bi.springdemo.DTO.LoginDTO;
import edu.bi.springdemo.exception.LoginPasswordException;
import edu.bi.springdemo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {

        String token = loginService.login(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return new ResponseEntity<>(new Gson().toJson(map), HttpStatus.OK);
    }

    @ExceptionHandler(LoginPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String resolveLoginPasswordException(LoginPasswordException e) {
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        map.put("timestamp", new Date().toString());
        return new Gson().toJson(map);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}