package com.ducnh.bikeshare.controller;

import com.ducnh.bikeshare.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@CrossOrigin(origins = Constant.FRONTEND_URL)
public class TestController {
    @PostMapping()
    public String test() {
        return "test";
    }
}
