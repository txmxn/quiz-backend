package de.tkayser.quiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class SimpleController {

    @GetMapping(path = "/", produces =  "application/json")
    public Welcome welcome() {
        Welcome welcome = new Welcome();
        welcome.message = "Willkommen beim Quiz!";
        return welcome;
    }

    @GetMapping(path = "/quiz", produces =  "application/json")
    public QuizData quiz() throws IOException {
        try (InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.json"))) {
            Gson gson = new Gson();
            QuizData quiz = gson.fromJson(reader, QuizData.class);
            return quiz;
        }
    }

}
