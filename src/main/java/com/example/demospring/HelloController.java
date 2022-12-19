package com.example.demospring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        var a = 3;
        return "Greetings from Spring Boot!  " + a;
    }

}