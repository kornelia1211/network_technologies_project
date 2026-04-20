package edu.bi.springdemo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(@RequestParam(defaultValue = "World") String name){
        return "Hello "+name+"! :_)";
    }

    @GetMapping("/add")
    public Integer addNumbers(@RequestParam Integer a, @RequestParam Integer b){
        return a+b;
    }

    @GetMapping("/random")
    public Integer generateRan(@RequestParam Integer min, @RequestParam Integer max){
        Random random = new Random();
        return random.nextInt(max-min+1) + min;
    }
}