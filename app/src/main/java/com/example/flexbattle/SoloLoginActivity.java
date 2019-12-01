package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SoloLoginActivity extends Activity {

  DBHelper dbHelper;

  EditText et_login, et_password;

  CheckBox cb_rememberMe;

  String user_login;

  String user_password;

  // Shared preferences
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor sharedPreferencesEditor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_solo_login);
    setOnTouchListenerToSoloLoginMenuButtons();

    et_login = findViewById(R.id.soloLoginInput);
    et_password = findViewById(R.id.soloPasswordInput);

    cb_rememberMe = findViewById(R.id.checkboxRememberMe);

    dbHelper = new DBHelper(this);

    // Shared preferences
    sharedPreferences =
        getSharedPreferences("flexbattle.com.mySharedPreferences", Context.MODE_PRIVATE);
    sharedPreferencesEditor = sharedPreferences.edit();
    checkSharedPreferences();
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
              if(cb_rememberMe.isChecked()){
                // Save login
                sharedPreferencesEditor.putString(getString(R.string.login), user_login);
                sharedPreferencesEditor.commit();
                // Save password
                sharedPreferencesEditor.putString(getString(R.string.password), user_password);
                sharedPreferencesEditor.commit();
                // Save checkbox
                sharedPreferencesEditor.putString(getString(R.string.checkbox), "True");
                sharedPreferencesEditor.commit();
              }else{
                // Save login
                sharedPreferencesEditor.putString(getString(R.string.login), "");
                sharedPreferencesEditor.commit();
                // Save password
                sharedPreferencesEditor.putString(getString(R.string.password), "");
                sharedPreferencesEditor.commit();
                // Save checkbox
                sharedPreferencesEditor.putString(getString(R.string.checkbox), "False");
                sharedPreferencesEditor.commit();
              }
            }
            return true;
          }
        });
  }

  private void checkSharedPreferences() {
    String checkBox = sharedPreferences.getString(getString(R.string.checkbox), "Fasle");
    String login = sharedPreferences.getString(getString(R.string.login), "");
    String password = sharedPreferences.getString(getString(R.string.password), "");

    et_login.setText(login);
    et_password.setText(password);

    if (checkBox.equals("True")) {
      cb_rememberMe.setChecked(true);
    } else {
      cb_rememberMe.setChecked(false);
    }
  }

  private boolean accountValidation(SQLiteDatabase database) {
    user_login = et_login.getText().toString().trim();
    user_password = et_password.getText().toString().trim();
    int cursorCount;
    Cursor mCursor =
        database.rawQuery(
            "SELECT * FROM "
                + DBHelper.TABLE_USER
                + " WHERE "
                + DBHelper.USER_KEY_LOGIN
                + "='"
                + user_login
                + "'"
                + " AND "
                + DBHelper.USER_KEY_PASSWORD
                + "='"
                + user_password
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
    intent.putExtra("COUNT_OF_PLAYERS", 1);
    intent.putExtra("PLAYER_LOGIN", user_login);
    startActivity(intent);
  }
}
