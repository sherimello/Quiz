package com.example.quiz.classes;

import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;

public class ProgressClass {

    private final RelativeLayout relative_progress;
    private final View view_progress;
    private final TextView text_question_count, text_option1, text_option2, text_option3, text_option4, text_question;
    private static final int QUESTIONS = 25;
    private int question_count = 0;
    private final QuizData quizData;
    private final ImageView image_done;
    private final ProgressBar progressBar;
    private final TextView text_counter;

    public ProgressClass(RelativeLayout relative_progress, View view_progress, TextView text_question_count, TextView text_option1, TextView text_option2, TextView text_option3, TextView text_option4, TextView text_question, ImageView image_done, ProgressBar progressBar, TextView text_counter) {
        this.relative_progress = relative_progress;
        this.view_progress = view_progress;
        this.text_question_count = text_question_count;
        this.text_option1 = text_option1;
        this.text_option2 = text_option2;
        this.text_option3 = text_option3;
        this.text_option4 = text_option4;
        this.text_question = text_question;
        this.image_done = image_done;
        this.progressBar = progressBar;
        this.text_counter = text_counter;

        int PROGRESS_WIDTH;

        PROGRESS_WIDTH = view_progress.getResources().getDisplayMetrics().widthPixels;

        quizData = new QuizData();
        quizData.assignData();
        text_counter.setText(String.valueOf(question_count + 1));
        text_question.setText(quizData.Questions.get(0));
        text_option1.setText(MessageFormat.format("a. {0}", quizData.OptionA.get(0)));
        text_option2.setText(MessageFormat.format("b. {0}", quizData.OptionB.get(0)));
        text_option3.setText(MessageFormat.format("c. {0}", quizData.OptionC.get(0)));
        text_option4.setText(MessageFormat.format("d. {0}", quizData.OptionD.get(0)));

        Toast.makeText(relative_progress.getContext(), String.valueOf(PROGRESS_WIDTH), Toast.LENGTH_SHORT).show();

    }

    public void setQuestionCountToNegativeOne() {
        question_count = -1;
    }

    public void updateProgress() {
        question_count++;
        view_progress.animate().scaleX(0.04f * question_count).setDuration(500).setInterpolator(new OvershootInterpolator());
        text_question_count.setText(MessageFormat.format("{0}/{1}", question_count, QUESTIONS));
        if (question_count >= 25) {
            Toast.makeText(relative_progress.getContext(), "done!", Toast.LENGTH_SHORT).show();
            image_done.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            question_count = 0;
            text_question_count.setText(MessageFormat.format("{0}/{1}", question_count, QUESTIONS));
            view_progress.animate().scaleX(0.04f * question_count).setDuration(500).setInterpolator(new OvershootInterpolator());
            return;
        }


        text_option1.setBackgroundTintList(null);
        text_option2.setBackgroundTintList(null);
        text_option3.setBackgroundTintList(null);
        text_option4.setBackgroundTintList(null);

        text_counter.setText(String.valueOf(question_count + 1));
        quizData.assignData();
        text_question.setText(quizData.Questions.get(question_count));
        text_option1.setText(MessageFormat.format("a. {0}", quizData.OptionA.get(question_count)));
        text_option2.setText(MessageFormat.format("b. {0}", quizData.OptionB.get(question_count)));
        text_option3.setText(MessageFormat.format("c. {0}", quizData.OptionC.get(question_count)));
        text_option4.setText(MessageFormat.format("d. {0}", quizData.OptionD.get(question_count)));


    }

}
