package de.tkayser.quiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class SimpleController {

    private Random RANDOMIZER = new Random();

    @GetMapping(path = "/", produces =  "application/json")
    public Welcome welcome() {
        Welcome welcome = new Welcome();
        welcome.message = "Willkommen beim Quiz!";
        return welcome;
    }

    @GetMapping(path = "/quiz", produces =  "application/json")
    public QuizData quiz() throws IOException {
        try (InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.json"))) {
            QuizData quiz = loadQuizData(reader);
            quiz.daten = shullfeData(quiz.daten);
            for (int i = 0; i < quiz.daten.length; i++) {
                shuffle(quiz.daten[i]);
            }
            return quiz;
        }
    }
    private void shuffle(QuizQuestion element) {
        for (int i = element.answers.length -1 ; i > 0; i--) {
            int j = RANDOMIZER.nextInt(i + 1);
            String tmp = element.answers[i];
            element.answers[i] = element.answers[j];
            element.answers[j] = tmp;
            if (element.right == j) {
                element.right = i;
            }
            else if (element.right == i) {
                element.right = j;
            }
       }
    }

    private QuizQuestion[] shullfeData(QuizQuestion[] questions) {
        List<QuizQuestion> quizQuestions = Arrays.asList(questions);
        Collections.shuffle(quizQuestions);
       return quizQuestions.toArray(questions);
    }

    private QuizData loadQuizData(InputStreamReader reader) {
        Gson gson = new Gson();
        return gson.fromJson(reader, QuizData.class);
    }

}
