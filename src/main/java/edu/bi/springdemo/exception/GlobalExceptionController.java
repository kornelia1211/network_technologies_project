//package edu.bi.springdemo.exception;
//
//import com.google.gson.Gson;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
////this will catch exception globally
//@RestControllerAdvice
//public class GlobalExceptionController {
//
//    @ExceptionHandler(IllegalAccessException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public String handleIllegalAccessException(IllegalAccessException e){
//        Map<String, String> map = new HashMap<>();
//        map.put("message", e.getMessage());
//        map.put("timestamp", new Date().toString());
//        return new Gson().toJson(map);
//    }
//}
