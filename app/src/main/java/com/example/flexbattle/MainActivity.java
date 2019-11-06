package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_main);
        setOnTouchListenerToMainMenuButtons();
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
