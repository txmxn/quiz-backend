package de.tkayser.quiz;

public class Answer {
    public Boolean correct;
    public Integer points;
    public Integer questionPoints;
    public Integer score;
    public Integer highscore;

    public Answer(boolean correct, int points, int questionPoints, int score, int highscore) {
        this.correct = correct;
        this.points = points;
        this.questionPoints = questionPoints;
        this.score = score;
        this.highscore = highscore;
    }
}
