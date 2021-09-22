package com.example.quiz.activities;

import static java.lang.System.exit;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.quiz.R;
import com.example.quiz.classes.ProgressClass;
import com.example.quiz.classes.ScoreClass;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

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
    private ArrayList<String> answers, questions, options, correctAnswers;
    private DatabaseReference databaseReference;
    private String quizID = "", chosen = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        quizID = intent.getStringExtra("quizID");

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

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("fetching quiz data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        initDatabase();

        answers = new ArrayList<>();

    }

    private void initDatabase() {
        questions = new ArrayList<>();
        options = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Questions").child(quizID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    questions.add(Objects.requireNonNull(dataSnapshot.child("que").getValue()).toString());

                    options.add(Objects.requireNonNull(dataSnapshot.child("_1").getValue()).toString());
                    options.add(Objects.requireNonNull(dataSnapshot.child("_2").getValue()).toString());
                    options.add(Objects.requireNonNull(dataSnapshot.child("_3").getValue()).toString());
                    options.add(Objects.requireNonNull(dataSnapshot.child("_4").getValue()).toString());
                }
                pc = new ProgressClass(relative_progress, view_progress, text_question_count, text_option1, text_option2, text_option3, text_option4, text_question, image_done, progressBar, text_counter, options, questions);
                pc.initQuestions();
                progressDialog.cancel();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Snackbar.make(text_counter, error.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == button_quit) {
            exit(1);
        }

        if (v == button_play_again) {
            card_result.animate().scaleY(.1f).scaleX(.2f).alpha(0).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
            new Handler().postDelayed(() -> {
//                pc.initQuestions();
                view_progress.animate().scaleX(0).setDuration(1000);
                card_result.setVisibility(View.GONE);
                card_query.animate().scaleY(1).scaleX(1).alpha(1).setDuration(500).setInterpolator(new OvershootInterpolator());
                scroll_query.setVisibility(View.VISIBLE);
            }, 300);
        }

        if (v == card_answer) {
            if (scroll_query.getVisibility() == View.VISIBLE && card_query.getScaleY() == 1 && !chosen.isEmpty()) {
                answers.add(chosen);
                chosen = "";
                if (answers.size() == questions.size()) {
                    correctAnswers = new ArrayList<>();
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Answers").child(quizID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        correctAnswers.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                                    }
                                    pc.updateProgress();
                                    setScoreProgress();
                                    scroll_query.setVisibility(View.GONE);
                                    card_query.animate().scaleY(.1f).scaleX(.2f).alpha(0).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
                                    new Handler().postDelayed(() -> {
                                        card_result.setAlpha(0);
                                        card_result.setVisibility(View.VISIBLE);
                                        card_result.animate().scaleY(1).scaleX(1).setDuration(500).alpha(1).setInterpolator(new OvershootInterpolator());
                                        scroll_query.setVisibility(View.VISIBLE);

                                    }, 500);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

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
            chosen = text_option1.getText().toString().substring(3);
        }
        if (v == text_option2) {
            assert text_option1 != null;
            text_option1.setBackgroundTintList(null);
            text_option2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.BG));
            text_option3.setBackgroundTintList(null);
            text_option4.setBackgroundTintList(null);
            chosen = text_option2.getText().toString().substring(3);
        }
        if (v == text_option3) {
            assert text_option1 != null;
            text_option1.setBackgroundTintList(null);
            text_option2.setBackgroundTintList(null);
            text_option3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.BG));
            text_option4.setBackgroundTintList(null);
            chosen = text_option3.getText().toString().substring(3);
        }
        if (v == text_option4) {
            assert text_option1 != null;
            text_option1.setBackgroundTintList(null);
            text_option2.setBackgroundTintList(null);
            text_option3.setBackgroundTintList(null);
            text_option4.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.BG));
            chosen = text_option4.getText().toString().substring(3);
        }
    }

    private void setScoreProgress() {
        int score = new ScoreClass(answers, correctAnswers).getScorePercentage();
        text_score.setText(String.valueOf(score));
        ObjectAnimator progressAnimator;
        progressAnimator = ObjectAnimator.ofInt(progress_score, "progress", 0, score);
        progressAnimator.setDuration(1000);
        progressAnimator.setInterpolator(new OvershootInterpolator());
        progressAnimator.start();
        answers.clear();
    }
}
