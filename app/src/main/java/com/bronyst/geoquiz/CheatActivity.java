package com.bronyst.geoquiz;


import static com.bronyst.geoquiz.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private TextView mTextViewShowAnswer, mTextViewAPILevel;
    private Button mButtonShowAnswer;

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bronyst.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "com.bronyst.geoquiz.aswer_shown";

    private boolean mAnswerTrue;


    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_cheat);

        mAnswerTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mTextViewShowAnswer = (TextView) findViewById(id.answer_textview);
        mTextViewAPILevel = (TextView) findViewById(R.id.tv_api_level);
        mTextViewAPILevel.setText(Build.VERSION.SDK);
        mButtonShowAnswer = (Button) findViewById(id.btn_show_answer);
        mButtonShowAnswer.setOnClickListener(v->{
            if(mAnswerTrue){
                mTextViewShowAnswer.setText(string.btn_true);
            }else{
               mTextViewShowAnswer.setText(string.btn_false);
            }
            setAnswerShown(true);

//            animation
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int cx = mButtonShowAnswer.getWidth() / 2;
                int cy = mButtonShowAnswer.getHeight() / 2;
                float radius = mButtonShowAnswer.getWidth();
                Animator animator = ViewAnimationUtils.createCircularReveal(mButtonShowAnswer, cx, cy, radius, 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mButtonShowAnswer.setVisibility(View.INVISIBLE);
                    }
                });
                animator.start();
            }else{
                mButtonShowAnswer.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setAnswerShown(boolean answerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, answerShown);
        setResult(RESULT_OK, data);
    }
}