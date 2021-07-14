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
    private static final String SESSION_ATTRIBUTE = "quiz";
    private static final String UNKNOWN_USERNAME = "unknown";

    @Value("${maxQuestions}")
    private int maxQuestions = 10;

    private int highScore = 0;
    private String username = UNKNOWN_USERNAME;

    @GetMapping(path = "/", produces =  "application/json")
    public Welcome welcome() {
        Welcome welcome = new Welcome();
        welcome.message = "Willkommen beim Quiz!";
        welcome.highScore = highScore;
        return welcome;
    }

    @GetMapping(path = "/quiz", produces =  "application/json")
    public Quiz quiz(HttpSession session) throws IOException {
        resetSession(session);
        return prepareQuiz();
    }

    private void resetSession(HttpSession session) {
        session.removeAttribute(SESSION_ATTRIBUTE);
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
        QuizSession sessionValue = getFromSession(session);
        sessionValue.handleCorrectAnswer();
        handleHighScore(selectedAnswer.username, sessionValue.score);
        session.setAttribute(SESSION_ATTRIBUTE, sessionValue);
        return Result.correctResult(sessionValue.score, highScore, username);
    }

    private void handleHighScore(String username, int score) {
        if (highScore < score) {
            highScore = score;
            if (username == null || "".equals(username)) {
                this.username = UNKNOWN_USERNAME;
            } else {
                this.username = username ;
            }
        }
    }

    private Result handleWrongAnswer(SelectedAnswer selectedAnswer, HttpSession session) {
        QuizSession sessionValue = getFromSession(session);
        sessionValue.handleWrongAnswer(selectedAnswer.answer);
        session.setAttribute(SESSION_ATTRIBUTE, sessionValue);
        return Result.wrongResult(sessionValue.points, sessionValue.score, highScore, username);
    }

    private QuizSession getFromSession(HttpSession session) {
        QuizSession quiz = (QuizSession) session.getAttribute(SESSION_ATTRIBUTE);
        if (quiz == null) {
            quiz = new QuizSession();
        }
        return quiz;
    }

}
