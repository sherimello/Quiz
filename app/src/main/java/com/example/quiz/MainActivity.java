package com.example.quiz;

import static java.lang.System.exit;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.quiz.classes.ProgressClass;
import com.example.quiz.classes.ScoreClass;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relative_progress;
    private View view_progress;
    private TextView text_question_count, text_option1, text_option2, text_option3, text_option4, text_question, text_counter, text_score;
    private CardView card_answer, card_query, card_result;
    private Button button_play_again, button_quit;
    private ScrollView scroll_query;
    private ProgressClass pc;
    private ImageView image_done;
    private ProgressBar progressBar, progress_score;
    private int chosen = 0;
    private ObjectAnimator animator;
    private ArrayList<String> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relative_progress = findViewById(R.id.relative_progress);
        view_progress = findViewById(R.id.view_progress);
        text_question_count = findViewById(R.id.text_question_count);
        card_answer = findViewById(R.id.card_answer);
        card_query = findViewById(R.id.card_query);
        card_result = findViewById(R.id.card_result);
        button_play_again = findViewById(R.id.button_play_again);
        button_quit = findViewById(R.id.button_quit);
        scroll_query = findViewById(R.id.scroll_query);
        text_option1 = findViewById(R.id.text_option1);
        text_option2 = findViewById(R.id.text_option2);
        text_option3 = findViewById(R.id.text_option3);
        text_option4 = findViewById(R.id.text_option4);
        text_question = findViewById(R.id.text_question);
        text_score = findViewById(R.id.text_score);
        image_done = findViewById(R.id.image_done);
        progressBar = findViewById(R.id.progress_circular);
        progress_score = findViewById(R.id.progress_score);
        text_counter = findViewById(R.id.text_counter);

        card_answer.setOnClickListener(this);
        text_option1.setOnClickListener(this);
        text_option2.setOnClickListener(this);
        text_option3.setOnClickListener(this);
        text_option4.setOnClickListener(this);
        button_quit.setOnClickListener(this);
        button_play_again.setOnClickListener(this);

        answers = new ArrayList<>();

        pc = new ProgressClass(relative_progress, view_progress, text_question_count, text_option1, text_option2, text_option3, text_option4, text_question, image_done, progressBar, text_counter);

    }

    @Override
    public void onClick(View v) {

        if (v == button_quit) {
            exit(1);
        }

        if (v == button_play_again) {
            card_result.animate().scaleY(.1f).scaleX(.2f).alpha(0).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
            new Handler().postDelayed(() -> {
                pc.setQuestionCountToNegativeOne();
                pc.updateProgress();
                card_result.setVisibility(View.GONE);
                card_query.animate().scaleY(1).scaleX(1).alpha(1).setDuration(500).setInterpolator(new OvershootInterpolator());
                scroll_query.setVisibility(View.VISIBLE);
            }, 300);
        }

        if (v == card_answer) {
            if (scroll_query.getVisibility() == View.VISIBLE && card_query.getScaleY() == 1 && chosen != 0) {
                answers.add(String.valueOf(chosen));
                chosen = 0;
                if (answers.size() == 25) {
                    setScoreProgress();
                    scroll_query.setVisibility(View.GONE);
                    card_query.animate().scaleY(.1f).scaleX(.2f).alpha(0).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
                    new Handler().postDelayed(() -> {
                        card_result.setAlpha(0);
                        card_result.setVisibility(View.VISIBLE);
                        card_result.animate().scaleY(1).scaleX(1).setDuration(500).alpha(1).setInterpolator(new OvershootInterpolator());
                        scroll_query.setVisibility(View.VISIBLE);
                        pc.updateProgress();
                    }, 701);
                    return;
                }
                scroll_query.setVisibility(View.GONE);
                card_query.animate().scaleY(.1f).scaleX(.2f).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
                new Handler().postDelayed(() -> {
                    card_query.animate().scaleY(1).scaleX(1).setDuration(500).setInterpolator(new OvershootInterpolator());
                    scroll_query.setVisibility(View.VISIBLE);
                    pc.updateProgress();
                }, 701);
                return;
            }
            Snackbar.make(card_answer, "choose an answer first...", Snackbar.LENGTH_SHORT).show();
        }
        if (v == text_option1) {
            assert text_option1 != null;
            text_option1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.BG));
            text_option2.setBackgroundTintList(null);
            text_option3.setBackgroundTintList(null);
            text_option4.setBackgroundTintList(null);
            chosen = 1;
        }
        if (v == text_option2) {
            assert text_option1 != null;
            text_option1.setBackgroundTintList(null);
            text_option2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.BG));
            text_option3.setBackgroundTintList(null);
            text_option4.setBackgroundTintList(null);
            chosen = 2;
        }
        if (v == text_option3) {
            assert text_option1 != null;
            text_option1.setBackgroundTintList(null);
            text_option2.setBackgroundTintList(null);
            text_option3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.BG));
            text_option4.setBackgroundTintList(null);
            chosen = 3;
        }
        if (v == text_option4) {
            assert text_option1 != null;
            text_option1.setBackgroundTintList(null);
            text_option2.setBackgroundTintList(null);
            text_option3.setBackgroundTintList(null);
            text_option4.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.BG));
            chosen = 4;
        }
    }

    private void setScoreProgress() {
        int score = new ScoreClass(answers).getScorePercentage();
        text_score.setText(String.valueOf(score));
        ObjectAnimator progressAnimator;
        progressAnimator = ObjectAnimator.ofInt(progress_score, "progress", 0, score);
        progressAnimator.setDuration(1000);
        progressAnimator.setInterpolator(new OvershootInterpolator());
        progressAnimator.start();
    }
}
