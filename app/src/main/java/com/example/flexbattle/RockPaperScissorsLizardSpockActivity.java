package com.example.flexbattle;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RockPaperScissorsLizardSpockActivity extends AppCompatActivity {

  private static final int ROCK = 0;
  private static final int SPOCK = 1;
  private static final int PAPER = 2;
  private static final int LIZARD = 3;
  private static final int SCISSORS = 4;
  DBHelper dbHelper;
  String user_login, first_user_login, second_user_login;
  int count_of_player;
  private ImageView iv_cpu, iv_player;
  private Button rock, spock, paper, lizard, scissors;
  private TextView tv_info;
  private Random r;
  private int playedCPU, playedPLAYER;
  private int winCounter = 0, winCounter_first_player = 0, winCounter_second_player = 0;
  // Sound
  SoundPlayer soundPlayer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rock_paper_scissors_lizard_spock);

    count_of_player = getIntent().getExtras().getInt("COUNT_OF_PLAYERS");
    if (count_of_player == 1) {
      user_login = getIntent().getExtras().getString("RockPaperScissorsLizardSpockGame_USER_LOGIN");
    } else {
      first_user_login =
          getIntent().getExtras().getString("RockPaperScissorsLizardSpockGame_FIRST_USER_LOGIN");
      second_user_login =
          getIntent().getExtras().getString("RockPaperScissorsLizardSpockGame_SECOND_USER_LOGIN");
    }

    dbHelper = new DBHelper(this);

    // Sound
    soundPlayer = new SoundPlayer(this);

    iv_cpu = findViewById(R.id.iv_cpu);
    iv_player = findViewById(R.id.iv_player);

    rock = findViewById(R.id.rock);
    spock = findViewById(R.id.spock);
    paper = findViewById(R.id.paper);
    scissors = findViewById(R.id.scissors);
    lizard = findViewById(R.id.lizard);

    tv_info = findViewById(R.id.tv_info);

    r = new Random();

    rock.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            playedPLAYER = ROCK;
            playedCPU = r.nextInt(5);
            setImages(playedPLAYER, playedCPU);
            tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
            soundPlayer.playRockSound(getApplicationContext());
          }
        });
    spock.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            playedPLAYER = SPOCK;
            playedCPU = r.nextInt(5);
            setImages(playedPLAYER, playedCPU);
            tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
            soundPlayer.playSpockSound(getApplicationContext());
          }
        });
    paper.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            playedPLAYER = PAPER;
            playedCPU = r.nextInt(5);
            setImages(playedPLAYER, playedCPU);
            tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
            soundPlayer.playPaperSound(getApplicationContext());
          }
        });
    scissors.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            playedPLAYER = SCISSORS;
            playedCPU = r.nextInt(5);
            setImages(playedPLAYER, playedCPU);
            tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
            soundPlayer.playScissorsSound(getApplicationContext());
          }
        });
    lizard.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            playedPLAYER = LIZARD;
            playedCPU = r.nextInt(5);
            setImages(playedPLAYER, playedCPU);
            tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
            soundPlayer.playLizardSound(getApplicationContext());
          }
        });
  }

  // this decides the winner
  private int getWinner(int A, int B) {
    return A == B ? 0 : (A - B + 5) % 5 <= 2 ? 1 : -1;
    // returns 1 if A is the winner, return -1 if B is the winner, returns 0 if it is a draw
  }

  // Converts result from the getWinner() function to text
  private String getWinnerText(int result) {
    SQLiteDatabase database = dbHelper.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    switch (result) {
      case 1:
        if (count_of_player == 1) {
          winCounter++;
          if (winCounter == 3) {
            int currentMoneyValue = getCurrentMoneyValue(database, user_login);
            putMoneyValue(database, user_login, contentValues, currentMoneyValue + 1);
            contentValues.clear();
            winCounter = 0;
          }
        } else {
          winCounter_first_player++;
          if (winCounter_first_player == 3) {
            int currentMoneyValue = getCurrentMoneyValue(database, first_user_login);
            putMoneyValue(database, first_user_login, contentValues, currentMoneyValue + 1);
            contentValues.clear();
            winCounter_first_player = 0;
          }
        }
        return "Winner Player";
      case -1:
        if (count_of_player == 1) {
          winCounter--;
        } else {
          winCounter_first_player--;
        }
        return "Winner CPU";
      case 0:
        return "Draw";
      default:
        return "";
    }
  }

  // set images of the picks
  private void setImages(int A, int B) {
    switch (A) {
      case ROCK:
        iv_player.setImageResource(R.drawable.rock);
        break;
      case SPOCK:
        iv_player.setImageResource(R.drawable.spock);
        break;
      case PAPER:
        iv_player.setImageResource(R.drawable.paper);
        break;
      case LIZARD:
        iv_player.setImageResource(R.drawable.lizard);
        break;
      case SCISSORS:
        iv_player.setImageResource(R.drawable.scissors);
        break;
    }
    switch (B) {
      case ROCK:
        iv_cpu.setImageResource(R.drawable.rock);
        break;
      case SPOCK:
        iv_cpu.setImageResource(R.drawable.spock);
        break;
      case PAPER:
        iv_cpu.setImageResource(R.drawable.paper);
        break;
      case LIZARD:
        iv_cpu.setImageResource(R.drawable.lizard);
        break;
      case SCISSORS:
        iv_cpu.setImageResource(R.drawable.scissors);
        break;
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
