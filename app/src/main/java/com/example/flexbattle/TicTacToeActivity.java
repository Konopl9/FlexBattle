package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class TicTacToeActivity extends Activity implements View.OnClickListener {

  private Button[][] buttons = new Button[3][3];

  private boolean player1Turn = true;

  private int roundCount;

  private int player1Points;

  private int player2Points;

  private TextView textViewPlayer1;

  private TextView textViewPlayer2;

  // DB options
  DBHelper dbHelper;
  String user_login, first_user_login, second_user_login;
  int count_of_player;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tic_tac_toe);

    // Db options
    count_of_player = getIntent().getExtras().getInt("COUNT_OF_PLAYERS");
    if (count_of_player == 1) {
      user_login = getIntent().getExtras().getString("TicTacToe_USER_LOGIN");
    } else {
      first_user_login = getIntent().getExtras().getString("TicTacToe_FIRST_USER_LOGIN");
      second_user_login = getIntent().getExtras().getString("TicTacToe_SECOND_USER_LOGIN");
    }
    dbHelper = new DBHelper(this);

    textViewPlayer1 = findViewById(R.id.text_view_p1);
    textViewPlayer2 = findViewById(R.id.text_view_p2);

    // Buttons preparation
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        String buttonID = "button_" + i + j;
        // Dynamically generation recourse id for buttons
        int resId = getResources().getIdentifier(buttonID, "id", getPackageName());
        buttons[i][j] = findViewById(resId);
        buttons[i][j].setOnClickListener(this);
      }
    }
    Button buttonReset = findViewById(R.id.button_reset);
    buttonReset.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            resetGame();
          }
        });
  }

  @Override
  public void onClick(View v) {
    if (!((Button) v).getText().toString().equals("")) {
      return;
    }
    if (player1Turn) {
      ((Button) v).setText("X");
    } else {
      ((Button) v).setText("O");
    }
    roundCount++;

    if (checkForWin()) {
      if (player1Turn) {
        player1Wins();
      } else {
        player2Wins();
      }
    } else if (roundCount == 9) {
      draw();
    } else {
      player1Turn = !player1Turn;
    }
  }

  private boolean checkForWin() {
    String[][] field = new String[3][3];

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        field[i][j] = buttons[i][j].getText().toString();
      }
    }

    for (int i = 0; i < 3; i++) {
      // Check rows have 3 same symbols except empty fieldL
      if (field[i][0].equals(field[i][1])
          && field[i][0].equals(field[i][2])
          && !field[i][0].equals("")) {
        return true;
      }
      // Check column have 3 same symbols except empty field
      if (field[0][i].equals(field[1][i])
          && field[0][i].equals(field[2][i])
          && !field[0][i].equals("")) {
        return true;
      }
      // Check left up to right down diagonal has 3 same symbols except empty field
      if (field[0][0].equals(field[1][1])
          && field[0][2].equals(field[2][0])
          && !field[0][2].equals("")) {
        return true;
      }
      // Check right up to left bottom diagonal has 3 same symbols except empty field
      if (field[0][2].equals(field[1][1])
          && field[0][2].equals(field[2][0])
          && !field[0][2].equals("")) {
        return true;
      }
    }
    return false;
  }

  private void player1Wins() {
    SQLiteDatabase database = dbHelper.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    player1Points++;
    Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
    if (count_of_player == 1) {
      int currentMoneyValue = getCurrentMoneyValue(database, user_login);
      putMoneyValue(database, user_login, contentValues, currentMoneyValue + 1);
      contentValues.clear();

    } else {
      int currentMoneyValue = getCurrentMoneyValue(database, first_user_login);
      putMoneyValue(database, first_user_login, contentValues, currentMoneyValue + 1);
      contentValues.clear();
    }
    updatePointsText();
    resetBoard();
  }

  private void player2Wins() {
    player2Points++;
    Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
    updatePointsText();
    SQLiteDatabase database = dbHelper.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    if (count_of_player == 2) {
      int currentMoneyValue = getCurrentMoneyValue(database, second_user_login);
      putMoneyValue(database, second_user_login, contentValues, currentMoneyValue + 1);
      contentValues.clear();
    }
    resetBoard();
  }

  private void draw() {
    Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
    resetBoard();
  }

  @SuppressLint("SetTextI18n")
  private void updatePointsText() {
    textViewPlayer1.setText("Player 1: " + player1Points);
    textViewPlayer2.setText("Player 2: " + player2Points);
  }

  private void resetBoard() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        buttons[i][j].setText("");
      }
    }
    roundCount = 0;
    player1Turn = true;
  }

  private void resetGame() {
    player1Points = 0;
    player2Points = 0;
    updatePointsText();
    resetBoard();
  }


  // save state when rotating the phone
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

  private int getCurrentMoneyValue(SQLiteDatabase database, String login) {
    int money_value = 0;
    Cursor cursor =
            database.rawQuery(
                    "SELECT * FROM "
                            + DBHelper.TABLE_USER
                            + " WHERE "
                            + DBHelper.USER_KEY_LOGIN
                            + "='"
                            + login
                            + "'",
                    null);
    if (cursor.moveToFirst()) {
      int points = cursor.getColumnIndex(DBHelper.USER_KEY_POINTS);
      do {
        money_value = cursor.getInt(points);
      } while (cursor.moveToNext());
    } else Log.d("mLog", "0 rows");
    cursor.close();
    return money_value;
  }

  private void putMoneyValue(
          SQLiteDatabase database, String login, ContentValues contentValues, int currentMoneyValue) {
    String strFilter = DBHelper.USER_KEY_LOGIN + " = " + "'" + login + "'";
    contentValues.put(DBHelper.USER_KEY_POINTS, currentMoneyValue);
    database.update(DBHelper.TABLE_USER, contentValues, strFilter, null);
    contentValues.clear();
  }
}
