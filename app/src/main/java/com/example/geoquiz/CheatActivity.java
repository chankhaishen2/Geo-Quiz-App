package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.geoquiz.databinding.ActivityCheatBinding;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER = "com.example.geoquiz.answer";
    private static final String EXTRA_ANSWER_SHOW = "com.example.geoquiz.answer_shown";
    private Boolean mAnswer;

    ActivityCheatBinding cheatBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cheatBinding = ActivityCheatBinding.inflate(getLayoutInflater());
        View v = cheatBinding.getRoot();
        setContentView(v);

        mAnswer = getIntent().getBooleanExtra(EXTRA_ANSWER, false);

        cheatBinding.btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswer)
                    cheatBinding.AnswerTextView.setText(R.string.true_button);
                else
                    cheatBinding.AnswerTextView.setText(R.string.false_button);
                setAnswerShownResult(true);
            }
        });
    }

    private void setAnswerShownResult(Boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOW, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}