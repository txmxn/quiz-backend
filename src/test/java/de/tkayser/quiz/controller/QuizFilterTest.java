package de.tkayser.quiz.controller;

import de.tkayser.quiz.data.Question;
import de.tkayser.quiz.data.QuestionWithRight;
import de.tkayser.quiz.data.Quiz;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizFilterTest {


    @Test
    public void testFilterEmptyQuiz() {
        Quiz quiz = new Quiz();

        QuizFilter filter = new QuizFilter();
        filter.filter(quiz, 2);

        assertEquals(0, quiz.questions.size());
    }

    @Test
    public void testFilterQuiz() {
        Quiz quiz = new Quiz();
        quiz.questions.add(new QuestionWithRight());
        quiz.questions.add(new QuestionWithRight());
        quiz.questions.add(new QuestionWithRight());
        quiz.questions.add(new QuestionWithRight());
        quiz.questions.add(new QuestionWithRight());
        quiz.questions.add(new QuestionWithRight());
        quiz.questions.add(new QuestionWithRight());
        quiz.questions.add(new QuestionWithRight());

        QuizFilter filter = new QuizFilter();
        filter.filter(quiz, 2);

        assertEquals(2, quiz.questions.size());
    }

}
