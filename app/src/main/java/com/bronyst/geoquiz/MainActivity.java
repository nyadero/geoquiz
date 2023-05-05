package com.bronyst.geoquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
   private Button mTrueButton, mFalseButton, mNextButton, mPreviousBtn, mCheatButton;
   private TextView mQuestionTextView;

   // logging
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    public static final int REQUEST_CODE_CHEAT = 0;

//   array of questions
    private Question[] questionsList = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, true),
    };

    // current question index
    private int mCurrentIndex = 0;
    private int correctAnswers = 0;
    private boolean mIsCheater;
    private int noOfCheats = 0;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: onCreate(Bundle) called ");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mTrueButton = (Button) findViewById(R.id.btn_true);
        mFalseButton = (Button) findViewById(R.id.btn_false);
        mNextButton = (Button) findViewById(R.id.btn_next);
        mCheatButton = (Button) findViewById(R.id.btn_cheat);
        mQuestionTextView = (TextView) findViewById(R.id.tv_question);
        // update question method
        updateQuestion();

//        click event listeners for the buttons
        mTrueButton.setOnClickListener(v -> {
            checkAnswer(true);
        });

        mFalseButton.setOnClickListener(v -> {
            checkAnswer(false);
        });

        mNextButton.setOnClickListener(v -> {
            mCurrentIndex = (mCurrentIndex + 1) % questionsList.length;
            mIsCheater = false;
            if(mCurrentIndex == questionsList.length - 1){
                checkTotalScore();
                mNextButton.setEnabled(false);
            }
            updateQuestion();
        });

        mQuestionTextView.setOnClickListener(v -> {
            mCurrentIndex = (mCurrentIndex + 1) % questionsList.length;
            updateQuestion();
        });

        mCheatButton.setOnClickListener(v-> {
            noOfCheats++;
            if(noOfCheats >= 3) mCheatButton.setEnabled(false);
            boolean answerIsTrue = questionsList[mCurrentIndex].isAnswerTrue();
            Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
            startActivityForResult(intent, REQUEST_CODE_CHEAT);
        });

    }

    private void checkTotalScore() {
        int totalScore = (correctAnswers / questionsList.length - 1) * 100;
        Snackbar.make(findViewById(R.id.content), "Your total score is " + totalScore, Snackbar.LENGTH_SHORT).show();
    }

    private void updateQuestion() {
        mQuestionTextView.setText(questionsList[mCurrentIndex].getmTextResId());
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = questionsList[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(mIsCheater){
            messageResId = R.string.judgement_toast;
        }else{
            if(userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
                correctAnswers++;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }
//        mTrueButton.setEnabled(false);
//        mFalseButton.setEnabled(false);
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
}