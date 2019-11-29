package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class GamesListActivity extends AppCompatActivity {

  DBHelper dbHelper;

  ListView listView;

  TextView tw_money, tw_login;

  String[] nameArray = {"Rock Paper Scissors Lizard Spock", "Mexico Dice", "Tic Tac Toe"};

  String[] infoArray = {
    "Win 3 times in a row to gain 1 coin", "Win once to gain coin", "Win once to gain coin"
  };

  Integer[] imageArray = {
    R.drawable.rpsls_icon, R.drawable.mexico_dice_icon, R.drawable.tic_tac_toe_icon
  };

  String user_login;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_games_list);
    // Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    tw_money = findViewById(R.id.FlexMoney);
    tw_login = findViewById(R.id.toolbarLogin);

    dbHelper = new DBHelper(this);
    SQLiteDatabase database = dbHelper.getReadableDatabase();

    int count_of_players = getIntent().getExtras().getInt("COUNT_OF_PLAYERS");
    user_login = getIntent().getExtras().getString("PLAYER_LOGIN");
    String second_user_login = getIntent().getExtras().getString("SECOND_USER_LOGIN");
    String main_in_game_login;

    // Tool bar insert data
    tw_login.setText(user_login);
    int user_money = getUserMoney(database, user_login);
    tw_money.setText(String.valueOf(user_money));

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

              openRockPaperScissorsLizardSpockGame();
            }
            if (position == 1) {
              openMexicoDiceGame();

            } else if (position == 2) {
              openTicTacToeGame();
            }
          }
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_game_list, menu);
    return true;
  }

  @Override
  public void onResume() { // After a pause OR at startup
    super.onResume();
    // Refresh your stuff here
    SQLiteDatabase database = dbHelper.getReadableDatabase();
    int user_money = getUserMoney(database, user_login);
    tw_money.setText(String.valueOf(user_money));
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

  private int getUserMoney(SQLiteDatabase database, String user_login) {
    int money_value = 0;
    Cursor cursor =
        database.rawQuery(
            "SELECT * FROM "
                + DBHelper.TABLE_USER
                + " WHERE "
                + DBHelper.USER_KEY_LOGIN
                + "='"
                + user_login
                + "'",
            null);
    if (cursor.moveToFirst()) {
      int game_state = cursor.getColumnIndex(DBHelper.USER_KEY_POINTS);
      do {
        money_value = cursor.getInt(game_state);
      } while (cursor.moveToNext());
    } else Log.d("mLog", "0 rows");
    cursor.close();
    return money_value;
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
    intent.putExtra("RockPaperScissorsLizardSpockGame_USER_LOGIN", user_login);
    startActivity(intent);
  }
}
