package de.tkayser.quiz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping(path = "/", produces =  "application/json")
    public Welcome homePage() {
        Welcome welcome = new Welcome();
        welcome.message = "Hello World";
        return welcome;
    }

}
