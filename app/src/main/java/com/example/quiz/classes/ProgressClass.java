package com.example.quiz.classes;

import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ProgressClass {

    private final RelativeLayout relative_progress;
    private final View view_progress;
    private final TextView text_question_count, text_option1, text_option2, text_option3, text_option4, text_question;
    private static final int QUESTIONS = 25;
    private int question_count = 0, option_count = -1;
    private final ImageView image_done;
    private final ProgressBar progressBar;
    private final TextView text_counter;
    private ArrayList<String> questions, options;

    public ProgressClass(RelativeLayout relative_progress, View view_progress, TextView text_question_count, TextView text_option1, TextView text_option2, TextView text_option3, TextView text_option4, TextView text_question, ImageView image_done, ProgressBar progressBar, TextView text_counter, ArrayList<String> options, ArrayList<String> questions) {
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
        this.questions = questions;
        this.options = options;
    }

    public void initQuestions() {

        text_question.setText(questions.get(question_count));
        text_option1.setText(MessageFormat.format("a. {0}", options.get(++option_count)));
        text_option2.setText(MessageFormat.format("b. {0}", options.get(++option_count)));
        text_option3.setText(MessageFormat.format("c. {0}", options.get(++option_count)));
        text_option4.setText(MessageFormat.format("d. {0}", options.get(++option_count)));
        text_question_count.setText(MessageFormat.format("{0}/{1}", question_count, questions.size()));
        text_counter.setText(String.valueOf(question_count + 1));

        resetOptionsUI();
    }

    private void resetOptionsUI() {
        text_option1.setBackgroundTintList(null);
        text_option2.setBackgroundTintList(null);
        text_option3.setBackgroundTintList(null);
        text_option4.setBackgroundTintList(null);
    }

    public void updateProgress() {
        question_count++;
        if (question_count == questions.size()) {
            text_question_count.setText(MessageFormat.format("{0}/{1}", question_count, questions.size()));

            view_progress.animate().scaleX(1.0f / questions.size() * question_count).setDuration(700).setInterpolator(new OvershootInterpolator());

            Toast.makeText(relative_progress.getContext(), "done!", Toast.LENGTH_SHORT).show();
            image_done.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            new Handler().postDelayed(() -> {

                question_count = 0;
                option_count = -1;
                initQuestions();
            }, 700);
            return;
        }
        view_progress.animate().scaleX((1.0f / questions.size()) * question_count).setDuration(700).setInterpolator(new OvershootInterpolator());

        initQuestions();
    }

}
