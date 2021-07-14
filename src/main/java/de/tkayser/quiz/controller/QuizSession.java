package de.tkayser.quiz.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizSession implements Serializable {

    public static final int MAX_POINTS = 5;

    public int score = 0;
    public int points = MAX_POINTS;
    public List<String> checked = new ArrayList<>();

    public void handleWrongAnswer(String answer) {
        if (points > 0 && !checked.contains(answer)) {
            points--;
            checked.add(answer);
        }
    }

    public void handleCorrectAnswer() {
        score += points;
        points = MAX_POINTS;
        checked.clear();
    }

}
