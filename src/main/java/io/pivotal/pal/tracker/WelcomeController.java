package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {




    private String controllerMsg = "hello";

    public WelcomeController(@Value("${welcome.message}") final String msg) {
        this.controllerMsg = msg;
    }

    @GetMapping("/")
    public String sayHello() {
        return this.controllerMsg;
    }
}
