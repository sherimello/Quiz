package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiz.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChooseQuiz extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_quiz_id;
    private Button button_attend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz);
        edit_quiz_id = findViewById(R.id.edit_quizID);
        button_attend = findViewById(R.id.button_attend);

        button_attend.setOnClickListener(this);

        attatchListenerToEdittext();

    }

    private void attatchListenerToEdittext() {

    }

    @Override
    public void onClick(View v) {
        if (v == button_attend) {
            if (edit_quiz_id.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "field cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            checkForAvailability();
        }
    }

    private void checkForAvailability() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("QuizID");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(edit_quiz_id.getText().toString())) {
                        proceedToQuiz();
                        return;
                    }
                }
                Snackbar.make(button_attend, "not found...", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(button_attend, error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void proceedToQuiz() {
        Toast.makeText(getApplicationContext(), "in", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("quizID", edit_quiz_id.getText().toString().trim()));
        finish();
    }
}