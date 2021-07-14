package de.tkayser.quiz.controller;

import de.tkayser.quiz.data.Quiz;

import java.util.Random;

public class QuizFilter {

    private static final Random RANDOMIZER = new Random();

    public void filter(Quiz quiz, int maxQuestions) {
        while(quiz.questions.size() > maxQuestions) {
            quiz.questions.remove(RANDOMIZER.nextInt(quiz.questions.size()));
        }
    }

}
