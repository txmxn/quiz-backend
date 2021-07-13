package de.tkayser.quiz;

public class Answer {
    public Boolean correct;
    public Integer points;
    public Integer questionPoints;

    public Answer(boolean correct, int points, int questionPoints) {
        this.correct = correct;
        this.points = points;
        this.questionPoints = questionPoints;
    }
}
