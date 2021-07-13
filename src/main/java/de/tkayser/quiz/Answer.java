package de.tkayser.quiz;

public class Answer {
    public Boolean correct;
    public Integer points;
    public Integer questionPoints;
    public Integer score;

    public Answer(boolean correct, int points, int questionPoints, int score) {
        this.correct = correct;
        this.points = points;
        this.questionPoints = questionPoints;
        this.score = score;
    }
}
