package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GamesListActivity extends AppCompatActivity {

  DBHelper dbHelper;

  ListView listView;

  String[] nameArray = {"Tic Tac Toe", "Mexico Dice", "Rock Paper Scissors Lizard Spock"};

  String[] infoArray = {
    "The player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row is the winner.",
    "The game ends when enough rounds have been played that only one player with any money remains, at which point the pot is his.",
    "The winner is the one who defeats the others."
  };

  Integer[] imageArray = {
    R.drawable.tic_tac_toe_icon, R.drawable.mexico_dice_icon, R.drawable.rpsls_icon
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_games_list);

    dbHelper = new DBHelper(this);
    SQLiteDatabase database = dbHelper.getReadableDatabase();
    int count_of_players = getIntent().getExtras().getInt("COUNT_OF_PLAYERS");
    String user_login = getIntent().getExtras().getString("PLAYER_LOGIN");
    String second_user_login = getIntent().getExtras().getString("SECOND_USER_LOGIN");
    String main_in_game_login;

    // separate for solo and multilayer
    if (count_of_players == 1) {
      main_in_game_login = user_login;
    } else {
      main_in_game_login = second_user_login;
    }

    GamesListAdapter gamesListAdapter =
        new GamesListAdapter(
            this,
            nameArray,
            infoArray,
            imageArray,
            loadGamesStateFromDatabase(database, main_in_game_login));
    // push parameters to list view (4 parameter is array list with bought games )
    listView = findViewById(R.id.listviewID);
    listView.setAdapter(gamesListAdapter);
    listView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
              openTicTacToeGame();
            }
            if (position == 1) {
              openMexicoDiceGame();

            } else if (position == 2) {
              openRockPaperScissorsLizardSpockGame();
            }
          }
        });
  }

  // Return list with game state for current login
  @SuppressLint("Assert")
  private List<Integer> loadGamesStateFromDatabase(SQLiteDatabase database, String user_login) {
    List<Integer> gamesState = new ArrayList<>();
    Cursor cursor =
        database.rawQuery(
            "SELECT * FROM "
                + DBHelper.TABLE_USER_HAS_GAME
                + " WHERE "
                + DBHelper.USER_HAS_GAME_KEY_USER_ID
                + "='"
                + user_login
                + "'",
            null);
    if (cursor.moveToFirst()) {
      int game_state = cursor.getColumnIndex(DBHelper.USER_HAS_GAME_KEY_STATE);
      do {
        gamesState.add(cursor.getInt(game_state));
      } while (cursor.moveToNext());
    } else Log.d("mLog", "0 rows");
    cursor.close();

    return gamesState;
  }

  // open tic tac toe game
  private void openTicTacToeGame() {
    Intent intent = new Intent(this, TicTacToeActivity.class);
    startActivity(intent);
  }

  // open mexico dice
  private void openMexicoDiceGame() {
    Intent intent = new Intent(this, MexicoDiceActivity.class);
    startActivity(intent);
  }

  // open rock paper scissors lizard spock game
  private void openRockPaperScissorsLizardSpockGame() {
    Intent intent = new Intent(this, RockPaperScissorsLizardSpockActivity.class);
    startActivity(intent);
  }
}
