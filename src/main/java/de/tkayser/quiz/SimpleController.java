package de.tkayser.quiz;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080", methods = {RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true")
public class SimpleController {

    private static final int MAX_QUESTIONS = 10;

    private Random RANDOMIZER = new Random();
    private int highscore = 0;

    @GetMapping(path = "/", produces =  "application/json")
    public Welcome welcome() {
        Welcome welcome = new Welcome();
        welcome.message = "Willkommen beim Quiz!";
        welcome.highscore = highscore;
        return welcome;
    }

    @PostMapping(path = "/questions", produces =  "application/json", consumes = "application/json")
    public Answer question(@RequestBody Data data, HttpSession session) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.json"))) {
            QuizData quiz = loadQuizData(reader);
            for (int i = 0; i < quiz.daten.size(); i++) {
                QuizQuestion quizQuestion = quiz.daten.get(i);
                if (quizQuestion.question.equals(data.frage)) {
                    if (quizQuestion.answers[quizQuestion.right].equals(data.antwort)) {
                        Integer points = getPointsFromSession(session);
                        Integer score = getScoreFromSession(session);
                        score += points;
                        if (highscore < score) {
                            highscore = score;
                        }
                        session.setAttribute("score", score);
                        session.removeAttribute("points");
                        session.removeAttribute("checked");
                        return new Answer(true, points, 3, score, highscore);
                    } else {
                        List<String> checked = getCheckedFromSession(session);
                        Integer points = getPointsFromSession(session);
                        Integer score = getScoreFromSession(session);
                        if (points > 0 && !checked.contains(data.antwort)) {
                            points--;
                            checked.add(data.antwort);
                        }
                        session.setAttribute("points", points);
                        session.setAttribute("checked", checked);
                        return new Answer(false, 0, points, score, highscore);
                    }
                }
            }
            return new Answer(false, 0, 0, 0, highscore);
        }
    }

    private Integer getPointsFromSession(HttpSession session) {
        Integer points = (Integer) session.getAttribute("points");
        if (points == null) {
            points = Integer.valueOf(3);
        }
        return points;
    }

    private Integer getScoreFromSession(HttpSession session) {
        Integer score = (Integer) session.getAttribute("score");
        if (score == null) {
            score = Integer.valueOf(0);
        }
        return score;
    }

    private List<String> getCheckedFromSession(HttpSession session) {
        List<String> checked = (List<String>) session.getAttribute("checked");
        if (checked == null) {
            checked = new ArrayList<String>();
        }
        return checked;
    }

    @GetMapping(path = "/quiz", produces =  "application/json")
    public QuizData quiz(HttpSession session) throws IOException {
        session.removeAttribute("score");
        session.removeAttribute("points");
        session.removeAttribute("checked");
        try (InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.json"))) {
            QuizData quiz = loadQuizData(reader);
            limitMaxQuestions(quiz);
            Collections.shuffle(quiz.daten);
            for (int i = 0; i < quiz.daten.size(); i++) {
                shuffle(quiz.daten.get(i));
                quiz.daten.get(i).right = -1;
            }
            return quiz;
        }
    }

    private void limitMaxQuestions(QuizData quiz) {
        while(quiz.daten.size() > MAX_QUESTIONS) {
            quiz.daten.remove(RANDOMIZER.nextInt(quiz.daten.size()));
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

    private void shullfeData(List<QuizQuestion> questions) {

    }

    private QuizData loadQuizData(InputStreamReader reader) {
        Gson gson = new Gson();
        return gson.fromJson(reader, QuizData.class);
    }

}
