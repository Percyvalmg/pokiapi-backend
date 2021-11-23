package com.percyval.pokiapi.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class APIController {

    @CrossOrigin(origins = "http://pokiapi-frontend.s3-website.us-east-2.amazonaws.com")
    @GetMapping
    public String get(){
        return "OK";
    }
}
