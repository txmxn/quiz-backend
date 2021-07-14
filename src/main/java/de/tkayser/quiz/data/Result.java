package de.tkayser.quiz.data;

public class Result {

    public static Result emptyResult(int highScore, String username) {
        return new Result(false, 0, highScore, username, 0);
    }

    public static Result correctResult(int currentScore, int highScore, String username) {
        return new Result(true, currentScore, highScore, username, 3);
    }

    public static Result wrongResult( int questionPoints, int currentScore, int highScore, String username) {
        return new Result(false, currentScore, highScore, username, questionPoints);
    }

    public Boolean correct;

    public Integer currentScore;
    public Integer highScore;
    public String highScoreUser;

    public Integer questionPoints;

    private Result(boolean correct, int currentScore, int highScore, String highScoreUser, int questionPoints) {
        this.correct = correct;
        this.questionPoints = questionPoints;
        this.currentScore = currentScore;
        this.highScore = highScore;
        this.highScoreUser = highScoreUser;
    }

}
