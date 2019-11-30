package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PvPLoginActivity extends Activity {

  DBHelper dbHelper;

  EditText et_firstPlayerLogin,
      et_firstPlayerPassword,
      et_secondPlayerLogin,
      et_secondPlayerPassword;

  boolean firstPlayerIsReady = false, secondPlayerIsReady = false;

  String second_user_login;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pvp_login);
    dbHelper = new DBHelper(this);
    setOnTouchListenerToPvPLoginMenuButtons();
    Button bothPlayerVerify = findViewById(R.id.verifyBothPlayerButton);
    bothPlayerVerify.setEnabled(false);
    bothPlayerVerify.setTextColor(Color.GRAY);

    et_firstPlayerLogin = findViewById(R.id.firstPlayerLoginInput);
    et_firstPlayerPassword = findViewById(R.id.firstPlayerPasswordInput);
    et_secondPlayerLogin = findViewById(R.id.secondPlayerLoginInput);
    et_secondPlayerPassword = findViewById(R.id.secondPlayerPasswordInput);
  }

  @SuppressLint("ClickableViewAccessibility")
  private void setOnTouchListenerToPvPLoginMenuButtons() {
    final Button firstPlayerVerify = findViewById(R.id.verifyPlayer1Button);
    final Button secondPlayerVerify = findViewById(R.id.verifyPlayer2Button);
    final Button bothPlayerVerify = findViewById(R.id.verifyBothPlayerButton);
    final SQLiteDatabase database = dbHelper.getReadableDatabase();

    firstPlayerVerify.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {}

            if (event.getAction() == MotionEvent.ACTION_UP) {
              if (!et_firstPlayerLogin.getText().toString().equals("")) {
                if (isDifferentPlayers()) {
                  if (accountValidation(database, et_firstPlayerLogin, et_firstPlayerPassword)) {
                    firstPlayerVerify.setTextColor(Color.GREEN);
                    et_firstPlayerLogin.setEnabled(false);
                    et_firstPlayerPassword.setEnabled(false);
                    firstPlayerIsReady = true;
                    if (checkBothPlayersReadyToPlay()) {
                      firstPlayerVerify.setEnabled(false);
                      secondPlayerVerify.setEnabled(false);
                      bothPlayerVerify.setEnabled(true);
                      bothPlayerVerify.setTextColor(Color.GREEN);
                    }
                  } else {
                    Toast.makeText(
                            getApplicationContext(), "WRONG LOGIN OR PASSWORD", Toast.LENGTH_LONG)
                        .show();
                  }
                } else {
                  Toast.makeText(
                          getApplicationContext(),
                          "THIS USER IS ALREADY PLAYING",
                          Toast.LENGTH_LONG)
                      .show();
                }
              } else {
                Toast.makeText(
                        getApplicationContext(), "FIRST PLAYER FIELD IS EMPTY", Toast.LENGTH_LONG)
                    .show();
              }
            }
            return true;
          }
        });
    secondPlayerVerify.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {}

            if (event.getAction() == MotionEvent.ACTION_UP) {
              if (!et_firstPlayerLogin.getText().toString().equals("")) {
                if (isDifferentPlayers()) {
                  if (accountValidation(database, et_secondPlayerLogin, et_secondPlayerPassword)) {
                    secondPlayerVerify.setTextColor(Color.GREEN);
                    et_secondPlayerLogin.setEnabled(false);
                    et_secondPlayerPassword.setEnabled(false);
                    secondPlayerIsReady = true;
                    if (checkBothPlayersReadyToPlay()) {
                      firstPlayerVerify.setEnabled(false);
                      secondPlayerVerify.setEnabled(false);
                      bothPlayerVerify.setEnabled(true);
                      bothPlayerVerify.setTextColor(Color.GREEN);
                    }
                  } else {
                    Toast.makeText(
                            getApplicationContext(), "WRONG LOGIN OR PASSWORD", Toast.LENGTH_LONG)
                        .show();
                  }
                } else {
                  Toast.makeText(
                          getApplicationContext(),
                          "THIS USER IS ALREADY PLAYING",
                          Toast.LENGTH_LONG)
                      .show();
                }
              } else {
                Toast.makeText(
                        getApplicationContext(), "SECOND PLAYER FIELD IS EMPTY", Toast.LENGTH_LONG)
                    .show();
              }
            }
            return true;
          }
        });
    bothPlayerVerify.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            openGamesListActivity();
            return true;
          }
        });
  }

  private boolean checkBothPlayersReadyToPlay() {
    return firstPlayerIsReady && secondPlayerIsReady;
  }

  private boolean isDifferentPlayers() {
    return !et_firstPlayerLogin
        .getText()
        .toString()
        .trim()
        .equals(et_secondPlayerLogin.getText().toString().trim());
  }

  private boolean accountValidation(
          SQLiteDatabase database, EditText playerLogin, EditText playerPassword) {
    String login = playerLogin.getText().toString().trim();
    String password = playerPassword.getText().toString().trim();
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

    if (mCursor.moveToFirst()) {
      int userLoginIndex = mCursor.getColumnIndex(DBHelper.USER_KEY_LOGIN);
      do {
        second_user_login = mCursor.getString(userLoginIndex);
      } while (mCursor.moveToNext());
    } else Log.d("mLog", "0 rows");

    cursorCount = mCursor.getCount();
    mCursor.close();
    return cursorCount == 1;
  }

  private void openGamesListActivity() {
    Intent intent = new Intent(this, GamesListActivity.class);
    intent.putExtra("COUNT_OF_PLAYERS", 2);
    intent.putExtra("FIRST_USER_LOGIN", et_firstPlayerLogin.getText().toString().trim());
    intent.putExtra("SECOND_USER_LOGIN", et_secondPlayerLogin.getText().toString().trim());
    startActivity(intent);
  }
}
