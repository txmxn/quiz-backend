package de.tkayser.quiz.data;

public class Result {

    public static Result emptyResult(int highScore, String username) {
        return new Result(false, 0, 0, highScore, username);
    }

    public static Result correctResult(int currentScore, int highScore, String username) {
        return new Result(true, 3, currentScore, highScore, username);
    }

    public static Result wrongResult( int questionPoints, int currentScore, int highScore, String username) {
        return new Result(false, questionPoints, currentScore, highScore, username);
    }

    public Boolean correct;

    public Integer currentScore;
    public Integer highScore;
    public String highScoreUser;

    public Integer questionPoints;

    private Result(boolean correct, int questionPoints, int currentScore, int highScore, String highScoreUser) {
        this.correct = correct;
        this.questionPoints = questionPoints;
        this.currentScore = currentScore;
        this.highScore = highScore;
        this.highScoreUser = highScoreUser;
    }

}
