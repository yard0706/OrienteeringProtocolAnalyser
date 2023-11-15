package ru.vrn.shor18.ProtocolAnalyzer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("test")
    public String test() {
        return "Test: Hello, World!";
    }
}
