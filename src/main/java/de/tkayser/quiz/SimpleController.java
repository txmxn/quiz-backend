package de.tkayser.quiz;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080", methods = {RequestMethod.GET, RequestMethod.POST})
public class SimpleController {

    private Random RANDOMIZER = new Random();

    @GetMapping(path = "/", produces =  "application/json")
    public Welcome welcome() {
        Welcome welcome = new Welcome();
        welcome.message = "Willkommen beim Quiz!";
        return welcome;
    }

    @PostMapping(path = "/questions", produces =  "application/json", consumes = "application/json")
    public Answer question(@RequestBody Data data ) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.json"))) {
            QuizData quiz = loadQuizData(reader);
            for (int i = 0; i < quiz.daten.length; i++) {
                QuizQuestion quizQuestion = quiz.daten[i];
                if (quizQuestion.question.equals(data.frage)) {
                    if (quizQuestion.answers[quizQuestion.right].equals(data.antwort)) {
                        return new Answer(true);
                    } else {
                        return new Answer(false);
                    }
                }
            }
            return new Answer(false);
        }
    }

    @GetMapping(path = "/quiz", produces =  "application/json")
    public QuizData quiz() throws IOException {
        try (InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.json"))) {
            QuizData quiz = loadQuizData(reader);
            quiz.daten = shullfeData(quiz.daten);
            for (int i = 0; i < quiz.daten.length; i++) {
                shuffle(quiz.daten[i]);
                quiz.daten[i].right = -1;
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
