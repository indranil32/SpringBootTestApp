package com.example.SpringBootTestApp.controller;

import com.example.SpringBootTestApp.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {
    @Autowired
    ITestService testService;

    @GetMapping("/test")
    public String test(@RequestParam Map<String, String> queryMap) {
        testService.test();
        return "OK";
    }
}
