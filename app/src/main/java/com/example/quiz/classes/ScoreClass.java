package com.example.quiz.classes;

import java.util.ArrayList;

public class ScoreClass {

    private final ArrayList<String> answers, correctAnswers;
    private int score_percentage = 0;

    public ScoreClass(ArrayList<String> answers, ArrayList<String> correctAnswers) {
        this.answers = answers;
        this.correctAnswers = correctAnswers;
    }

    public int getScorePercentage() {

        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).equals(correctAnswers.get(i))) {

                score_percentage += (100/ correctAnswers.size());

            }
        }
        return score_percentage;
    }
}
