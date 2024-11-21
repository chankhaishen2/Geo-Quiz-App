package com.example.geoquiz;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.geoquiz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String EXTRA_ANSWER = "com.example.geoquiz.answer";
    private static final String EXTRA_ANSWER_SHOW = "com.example.geoquiz.answer_shown";

    private ActivityMainBinding binding;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;
    private Boolean mIsCheater;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LifeCycle", "On Start");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LifeCycle", "On Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle", "On Stop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("LifeCycle", "On Restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LifeCycle", "On Resume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle", "On Destroy");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        updateQuestion();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        binding.btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        binding.btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK){
                            mIsCheater = result.getData().getBooleanExtra(EXTRA_ANSWER_SHOW, false);
                        }
                    }
                }
        );

        binding.btnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CheatActivity.class);
                i.putExtra(EXTRA_ANSWER, mQuestionBank[mCurrentIndex].isAnswerTrue());
                startActivity(i);
                resultLauncher.launch(i);
            }
        });
    }

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        binding.questionTextView.setText(question);

        mIsCheater = false;

        if (mCurrentIndex == mQuestionBank.length - 1){
            binding.btnNext.setEnabled(false);
        }
        else{
            binding.btnNext.setEnabled(true);
        }

        if (mCurrentIndex == 0){
            binding.btnPrevious.setEnabled(false);
        }
        else{
            binding.btnPrevious.setEnabled(true);
        }
    };

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        Log.d("checkAnswer", "Click");
        if (mIsCheater){
            messageResId = R.string.judgement_toast;
        }
        else if (userPressedTrue == answerIsTrue){
            messageResId = R.string.correctToast;
        }
        else{
            messageResId = R.string.incorrectToast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }
}