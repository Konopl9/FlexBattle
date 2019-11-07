package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class MainActivity extends Activity {

    LinearLayout linearLayout;
    TextView greetingTextView;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make status bar transparent
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activiti_main);
        setOnTouchListenerToMainMenuButtons();
        setGreetingTextByTime();
    }

    @SuppressLint("NewApi")
    private void setGreetingTextByTime(){
        linearLayout = findViewById(R.id.mainLayout);
        greetingTextView = findViewById(R.id.greetingText);
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay < 12) {
            //morning
            linearLayout.setBackground(getDrawable(R.drawable.morning_bg));
            greetingTextView.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            //afternoon
            greetingTextView.setText("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            //evening
            greetingTextView.setText("Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            //night
            linearLayout.setBackground(getDrawable(R.drawable.night_bg));
            greetingTextView.setText("Good Night");
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListenerToMainMenuButtons() {
        final Button soloPlayButton = findViewById(R.id.btn_soloPlay);
        final Button pvpPlayButton = findViewById(R.id.btn_pvpPlay);
        soloPlayButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.login_button_animation);
                    soloPlayButton.startAnimation(animation);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setContentView(R.layout.activity_solo_login);
                }
                return true;
            }
        });

        pvpPlayButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.login_button_animation);
                    pvpPlayButton.startAnimation(animation);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setContentView(R.layout.activity_pvp_login);
                }
                return true;
            }
        });
    }

}
