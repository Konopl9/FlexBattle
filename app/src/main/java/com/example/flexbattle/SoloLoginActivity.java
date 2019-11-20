package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class SoloLoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_login);
        setOnTouchListenerToSoloLoginMenuButtons();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListenerToSoloLoginMenuButtons() {
        final Button registrationButton = findViewById(R.id.registrationButton);
        final Button verifyLogin = findViewById(R.id.loginButton);
        registrationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.login_button_animation);
                    registrationButton.startAnimation(animation);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    openRegistrationActivity();
                }
                return true;
            }
        });
        verifyLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.login_button_animation);
                    verifyLogin.startAnimation(animation);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    openGamesListActivity();
                }
                return true;
            }
        });
    }

    private void openRegistrationActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void openGamesListActivity(){
        Intent intent = new Intent(this, GamesListActivity.class);
        startActivity(intent);
    }
}
