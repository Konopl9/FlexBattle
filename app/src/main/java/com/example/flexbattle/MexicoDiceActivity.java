package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MexicoDiceActivity extends AppCompatActivity {

  ImageView iv_dice_p1, iv_dice_p2, iv_lives_p1, iv_lives_p2;
  TextView tv_player1, tv_player2;
  int livesP1, livesP2;
  int rolledP1, rolledP2;

  Animation animation;

  Random r;

  // DB options
  DBHelper dbHelper;
  String user_login, first_user_login, second_user_login;
  int count_of_player;

  // Sound
  SoundPlayer soundPlayer;

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mexico_dice);

    // Db options
    count_of_player = getIntent().getExtras().getInt("COUNT_OF_PLAYERS");
    if (count_of_player == 1) {
      user_login = getIntent().getExtras().getString("MexicoDice_USER_LOGIN");
    } else {
      first_user_login = getIntent().getExtras().getString("MexicoDice_FIRST_USER_LOGIN");
      second_user_login = getIntent().getExtras().getString("MexicoDice_SECOND_USER_LOGIN");
    }

    dbHelper = new DBHelper(this);

    // Sound
    soundPlayer = new SoundPlayer(this);

    r = new Random();

    animation = AnimationUtils.loadAnimation(this, R.anim.rotate_dice);

    iv_dice_p1 = findViewById(R.id.iv_dice_p1);
    iv_dice_p2 = findViewById(R.id.iv_dice_p2);

    iv_lives_p1 = findViewById(R.id.iv_lives_p1);
    iv_lives_p2 = findViewById(R.id.iv_lives_p2);

    tv_player1 = findViewById(R.id.tv_player1);
    tv_player2 = findViewById(R.id.tv_player2);

    tv_player1.setText("PLAYER 1 ROLL!");
    tv_player2.setText("PLAYER 2 ROLL!");

    // lives left
    livesP1 = 6;
    livesP2 = 6;

    setDiceImage(livesP1, iv_dice_p1);
    setDiceImage(livesP2, iv_dice_p2);

    iv_dice_p1.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            soundPlayer.playDiceSound(getApplicationContext());
            // roll the dice
            rolledP1 = r.nextInt(6) + 1;
            setDiceImage(rolledP1, iv_dice_p1);
            iv_dice_p1.startAnimation(animation);

            // check if other player rolled his dice
            if (rolledP2 != 0) {
              tv_player1.setText("PLAYER 1 ROLL!");
              tv_player2.setText("PLAYER 2 ROLL!");

              // calculate the winner
              if (rolledP1 > rolledP2) {
                livesP2--;
                setDiceImage(livesP2, iv_lives_p2);
                Toast.makeText(getApplicationContext(), "Player 1 WIN!", Toast.LENGTH_SHORT).show();
              }
              if (rolledP2 > rolledP1) {
                livesP1--;
                setDiceImage(livesP1, iv_lives_p1);
                Toast.makeText(getApplicationContext(), "Player 2 WIN!", Toast.LENGTH_SHORT).show();
              }

              if (rolledP1 == rolledP2) {
                Toast.makeText(getApplicationContext(), "DRAW!", Toast.LENGTH_SHORT).show();
              }

              // init the values
              rolledP1 = 0;
              rolledP2 = 0;

              iv_dice_p1.setEnabled(true);
              iv_dice_p2.setEnabled(true);

              // check player witch one have 0 lives left
              checkEndGame();

            } else {
              tv_player1.setText("PLAYER 1 ROLLED!");
              iv_dice_p1.setEnabled(false);
            }
          }
        });

    iv_dice_p2.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            soundPlayer.playDiceSound(getApplicationContext());
            // roll the dice
            rolledP2 = r.nextInt(6) + 1;
            setDiceImage(rolledP2, iv_dice_p2);
            iv_dice_p2.startAnimation(animation);

            if (rolledP1 != 0) {
              tv_player1.setText("PLAYER 1 ROLL!");
              tv_player2.setText("PLAYER 2 ROLL!");
              if (rolledP1 > rolledP2) {
                livesP2--;
                setDiceImage(livesP2, iv_lives_p2);
                Toast.makeText(getApplicationContext(), "Player 1 WIN!", Toast.LENGTH_SHORT).show();
              }
              if (rolledP2 > rolledP1) {
                livesP1--;
                setDiceImage(livesP1, iv_lives_p1);
                Toast.makeText(getApplicationContext(), "Player 2 WIN!", Toast.LENGTH_SHORT).show();
              }

              if (rolledP1 == rolledP2) {
                Toast.makeText(getApplicationContext(), "DRAW!", Toast.LENGTH_SHORT).show();
              }

              rolledP1 = 0;
              rolledP2 = 0;

              iv_dice_p1.setEnabled(true);
              iv_dice_p2.setEnabled(true);

              checkEndGame();

            } else {
              tv_player2.setText("PLAYER 2 ROLLED!");
              iv_dice_p2.setEnabled(false);
            }
          }
        });
  }

  // show dice image according the number
  private void setDiceImage(int dice, ImageView image) {
    switch (dice) {
      case 1:
        image.setImageResource(R.drawable.dice1);
        break;
      case 2:
        image.setImageResource(R.drawable.dice2);
        break;
      case 3:
        image.setImageResource(R.drawable.dice3);
        break;
      case 4:
        image.setImageResource(R.drawable.dice4);
        break;
      case 5:
        image.setImageResource(R.drawable.dice5);
        break;
      case 6:
        image.setImageResource(R.drawable.dice6);
        break;
      default:
        image.setImageResource(R.drawable.dice0);
    }
  }

  // show end game dialog
  private void checkEndGame() {
    SQLiteDatabase database = dbHelper.getWritableDatabase();
    ContentValues contentValues = new ContentValues();

    if (livesP1 == 0 || livesP2 == 0) {
      iv_dice_p1.setEnabled(false);
      iv_dice_p1.setEnabled(false);

      String text = "";
      if (livesP1 != 0) {
        text = "Game Over! Winner Player 1";
        if (count_of_player == 1) {
          int currentMoneyValue = getCurrentMoneyValue(database, user_login);
          putMoneyValue(database, user_login, contentValues, currentMoneyValue + 1);
          contentValues.clear();

        } else {
          int currentMoneyValue = getCurrentMoneyValue(database, first_user_login);
          putMoneyValue(database, first_user_login, contentValues, currentMoneyValue + 1);
          contentValues.clear();
        }
      }
      if (livesP2 != 0) {
        text = "Game Over! Winner Player 2";
        if (count_of_player == 2) {
          int currentMoneyValue = getCurrentMoneyValue(database, second_user_login);
          putMoneyValue(database, second_user_login, contentValues, currentMoneyValue + 1);
          contentValues.clear();
        }
      }
      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
      alertDialogBuilder.setCancelable(false);
      alertDialogBuilder.setMessage(text);
      alertDialogBuilder.setPositiveButton(
          "OK",
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              finish();
            }
          });
      AlertDialog alertDialog = alertDialogBuilder.create();
      alertDialog.show();
      soundPlayer.playWinSound(this);
    }
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
