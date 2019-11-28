package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SoloLoginActivity extends Activity {

  DBHelper dbHelper;

  EditText et_login, et_password;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_solo_login);
    setOnTouchListenerToSoloLoginMenuButtons();

    et_login = findViewById(R.id.soloLoginInput);
    et_password = findViewById(R.id.soloPasswordInput);

    dbHelper = new DBHelper(this);
  }

  @SuppressLint("ClickableViewAccessibility")
  private void setOnTouchListenerToSoloLoginMenuButtons() {
    final Button registrationButton = findViewById(R.id.registrationButton);
    final Button verifyLogin = findViewById(R.id.loginButton);
    registrationButton.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
              Animation animation =
                  AnimationUtils.loadAnimation(
                      getApplicationContext(), R.anim.login_button_animation);
              registrationButton.startAnimation(animation);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
              openRegistrationActivity();
            }
            return true;
          }
        });
    verifyLogin.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
              Animation animation =
                  AnimationUtils.loadAnimation(
                      getApplicationContext(), R.anim.login_button_animation);
              verifyLogin.startAnimation(animation);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
              if (accountValidation(database)) {
                openGamesListActivity();
              } else {
                Toast.makeText(
                        getApplicationContext(), "WRONG LOGIN OR PASSWORD", Toast.LENGTH_LONG)
                    .show();
              }
            }
            return true;
          }
        });
  }

  private boolean accountValidation(SQLiteDatabase database) {
    String login = et_login.getText().toString().trim();
    String password = et_password.getText().toString().trim();
    int cursorCount;
    Cursor mCursor =
        database.rawQuery(
            "SELECT * FROM "
                + DBHelper.TABLE_USER
                + " WHERE "
                + DBHelper.USER_KEY_LOGIN
                + "='"
                + login
                + "'"
                + " AND "
                + DBHelper.USER_KEY_PASSWORD
                + "='"
                + password
                + "'",
            null);
    cursorCount = mCursor.getCount();
    mCursor.close();
    return cursorCount == 1;
  }

  private void openRegistrationActivity() {
    Intent intent = new Intent(this, RegistrationActivity.class);
    startActivity(intent);
  }

  private void openGamesListActivity() {
    Intent intent = new Intent(this, GamesListActivity.class);
    startActivity(intent);
  }
}
