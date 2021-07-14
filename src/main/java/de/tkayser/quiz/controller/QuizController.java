package de.tkayser.quiz.controller;

import com.google.gson.Gson;
import de.tkayser.quiz.data.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080", methods = {RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true")
public class QuizController {

    private static final Random RANDOMIZER = new Random();

    @Value("${maxQuestions}")
    private int maxQuestions = 10;

    private int highScore = 0;
    private String username = "unknown";

    @GetMapping(path = "/", produces =  "application/json")
    public Welcome welcome() {
        Welcome welcome = new Welcome();
        welcome.message = "Willkommen beim Quiz!";
        welcome.highscore = highScore;
        return welcome;
    }

    @GetMapping(path = "/quiz", produces =  "application/json")
    public Quiz quiz(HttpSession session) throws IOException {
        resetSession(session);
        return prepareQuiz();
    }

    private void resetSession(HttpSession session) {
        session.removeAttribute("score");
        session.removeAttribute("points");
        session.removeAttribute("checked");
    }

    private Quiz prepareQuiz() throws IOException {
        Quiz quiz = loadQuiz();
        limitMaxQuestions(quiz);
        shuffle(quiz);
        return quiz;
    }

    private Quiz loadQuiz() throws IOException {
        try (InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("database.json"))) {
            return new Gson().fromJson(reader, Quiz.class);
        }
    }

    private void limitMaxQuestions(Quiz quiz) {
        while(quiz.questions.size() > maxQuestions) {
            quiz.questions.remove(RANDOMIZER.nextInt(quiz.questions.size()));
        }
    }

    private void shuffle(Quiz quiz) {
        Collections.shuffle(quiz.questions);
        for (int i = 0; i < quiz.questions.size(); i++) {
            shuffleAnswers(quiz.questions.get(i));
            quiz.questions.get(i).right = -1;
        }
    }

    private void shuffleAnswers(Question element) {
        for (int i = element.answers.length -1 ; i > 0; i--) {
            int j = RANDOMIZER.nextInt(i + 1);
            switchAnswers(element, i, j);
            switchRightAnswer(element, i, j);
        }
    }

    private void switchAnswers(Question element, int i, int j) {
        String tmp = element.answers[i];
        element.answers[i] = element.answers[j];
        element.answers[j] = tmp;
    }

    private void switchRightAnswer(Question element, int i, int j) {
        if (element.right == j) {
            element.right = i;
        }
        else if (element.right == i) {
            element.right = j;
        }
    }

    @PostMapping(path = "/solve", produces =  "application/json", consumes = "application/json")
    public Result solve(@RequestBody SelectedAnswer selectedAnswer, HttpSession session) throws IOException {
        Quiz quiz = loadQuiz();
        Optional<Question> question = searchQuestion(quiz, selectedAnswer.question);
        if (question.isPresent()) {
            return handleQuestion(question.get(), selectedAnswer, session);
        } else {
            return Result.emptyResult(highScore, username);
        }
    }

    private Optional<Question> searchQuestion(Quiz quiz, String questionText) {
        for (int i = 0; i < quiz.questions.size(); i++) {
            Question question = quiz.questions.get(i);
            if (question.question.equals(questionText)) {
                return Optional.of(question);
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private Result handleQuestion(Question question, SelectedAnswer selectedAnswer, HttpSession session) {
        if (isCorrect(question, selectedAnswer)) {
            return handleCorrectAnswer(selectedAnswer, session);
        }
        else {
            return handleWrongAnswer(selectedAnswer, session);
        }
    }

    private boolean isCorrect(Question question, SelectedAnswer selectedAnswer) {
        return question.answers[question.right].equals(selectedAnswer.answer);
    }

    private Result handleCorrectAnswer(SelectedAnswer selectedAnswer, HttpSession session) {
        Integer points = getPointsFromSession(session);
        Integer score = getScoreFromSession(session);
        score += points;
        if (highScore < score) {
            highScore = score;
            if (selectedAnswer.username == null || "".equals(selectedAnswer.username)) {
                username = "unknown";
            } else {
                username = selectedAnswer.username ;
            }
        }
        session.setAttribute("score", score);
        session.removeAttribute("points");
        session.removeAttribute("checked");
        return Result.correctResult(score, highScore, username);
    }

    private Result handleWrongAnswer(SelectedAnswer selectedAnswer, HttpSession session) {
        List<String> checked = getCheckedFromSession(session);
        Integer points = getPointsFromSession(session);
        Integer score = getScoreFromSession(session);
        if (points > 0 && !checked.contains(selectedAnswer.answer)) {
            points--;
            checked.add(selectedAnswer.answer);
        }
        session.setAttribute("points", points);
        session.setAttribute("checked", checked);
        return Result.wrongResult(points, score, highScore, username);
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

}
