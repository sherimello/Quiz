package com.example.quiz.classes;

import java.util.ArrayList;

public class ScoreClass {

    private final ArrayList<String> answers;
    private int score_percentage = 0;

    public ScoreClass(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getScorePercentage() {
        QuizData quizData = new QuizData();
        quizData.assignData();
        ArrayList<String> correctAnswers = quizData.Answers;
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).equals(correctAnswers.get(i))) {

                score_percentage += 4;

            }
        }
        return score_percentage;
    }
}
