package com.percyval.pokiapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class APIController {

    @GetMapping
    public String get(){
        return "OK";
    }
}
