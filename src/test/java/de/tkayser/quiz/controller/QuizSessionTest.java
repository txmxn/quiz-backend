package de.tkayser.quiz.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuizSessionTest {

    @Test
    public void testInitialState() {
        QuizSession session = new QuizSession();

        assertEquals(QuizSession.MAX_POINTS, session.points);
        assertEquals(0, session.score);
    }

    @Test
    public void testWrongAnswerFirstTime() {
        QuizSession session = new QuizSession();
        session.handleWrongAnswer("foo");

        assertEquals(QuizSession.MAX_POINTS -1, session.points);
    }

    @Test
    public void testWrongAnswerSecondTime() {
        QuizSession session = new QuizSession();
        session.handleWrongAnswer("foo");
        session.handleWrongAnswer("foo");

        assertEquals(QuizSession.MAX_POINTS -1, session.points);
    }

    @Test
    public void testWrongAnswerTooOften() {
        QuizSession session = new QuizSession();
        for (int i = 0; i < QuizSession.MAX_POINTS; i++) {
            session.handleWrongAnswer("foo" + i);
        }

        assertEquals(0, session.points);
    }

    @Test
    public void testCorrectAnswer() {
        QuizSession session = new QuizSession();
        session.handleCorrectAnswer();

        assertEquals(QuizSession.MAX_POINTS, session.score);
        assertEquals(QuizSession.MAX_POINTS, session.points);
    }

    @Test
    public void testCorrectAnswerAfterWrong() {
        QuizSession session = new QuizSession();
        session.handleWrongAnswer("foo");
        session.handleCorrectAnswer();

        assertEquals(QuizSession.MAX_POINTS -1, session.score);
        assertEquals(QuizSession.MAX_POINTS, session.points);
    }

}