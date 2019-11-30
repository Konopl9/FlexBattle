package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

  // Game Titles
  String[] nameArray = {"Rock Paper Scissors Lizard Spock", "Mexico Dice", "Tic Tac Toe"};

  // Description for solo game
  String[] infoSoloPlayArray = {
    "Win 3 times in a row to gain 1 coin",
    "Win once to gain coin \nTo gain coins play as Player 1",
    "Win once to gain coin \nTo gain coins use CROSS mark"
  };

  // Description for multilayer
  String[] infoPvPPlayArray = {
    "Win 3 times in a row to gain 1 coin \nFirst logged player will gain coins",
    "Win once to gain coin \nFirst logged player will be Player 1",
    "Win once to gain coin \nFirst logged player will be CROSS mark"
  };

  // Array to clone
  String[] infoSelectedPlayArray;

  // Image for game icons
  Integer[] imageArray = {
    R.drawable.rpsls_icon, R.drawable.mexico_dice_icon, R.drawable.tic_tac_toe_icon
  };

  // String with solo intent user login
  String user_login;

  // String with first intent user login
  String first_user_login;

  // String with second intent user login
  String second_user_login;

  Integer count_of_players;

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_games_list);
    // Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    tw_money = findViewById(R.id.FlexMoney);
    tw_login = findViewById(R.id.toolbarLogin);

    // Db options
    dbHelper = new DBHelper(this);
    SQLiteDatabase database = dbHelper.getReadableDatabase();

    // Get data from intends
    count_of_players = getIntent().getExtras().getInt("COUNT_OF_PLAYERS");
    user_login = getIntent().getExtras().getString("PLAYER_LOGIN");
    first_user_login = getIntent().getExtras().getString("FIRST_USER_LOGIN");
    second_user_login = getIntent().getExtras().getString("SECOND_USER_LOGIN");
    // Buffer string to store logged user login and put into listView
    String main_in_game_login;

    // separate for solo and multilayer
    if (count_of_players == 1) {
      main_in_game_login = user_login;
      infoSelectedPlayArray = infoSoloPlayArray.clone();
      // Tool bar insert data
      tw_login.setText(user_login);
      int user_money = getUserMoney(database, user_login);
      tw_money.setText(String.valueOf(user_money));
    } else {
      main_in_game_login = second_user_login;
      infoSelectedPlayArray = infoPvPPlayArray.clone();
      // Tool bar insert data
      tw_login.setText(first_user_login + " VS " + second_user_login);
      int first_user_money = getUserMoney(database, first_user_login);
      int second_user_money = getUserMoney(database, second_user_login);
      tw_money.setText(first_user_money + " VS " + second_user_money);
    }

    // Crating list view
    GamesListAdapter gamesListAdapter =
        new GamesListAdapter(
            this,
            nameArray,
            infoSelectedPlayArray,
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
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.account_setting_item:
        // account setting item
        return true;
      case R.id.shop_item:
        Intent intent = new Intent(this, GamesShopActivity.class);
        intent.putExtra("USER_LOGIN", user_login);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    // Refresh data after back button
    SQLiteDatabase database = dbHelper.getReadableDatabase();
    if (count_of_players == 1) {
      int user_money = getUserMoney(database, user_login);
      tw_money.setText(String.valueOf(user_money));
    } else {
      int first_user_money = getUserMoney(database, first_user_login);
      int second_user_money = getUserMoney(database, second_user_login);
      tw_money.setText(first_user_money + " VS " + second_user_money);
    }
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

  // Select value of player points
  private int getUserMoney(SQLiteDatabase database, String login) {
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
    if (count_of_players == 1) {
      Intent intent = new Intent(this, TicTacToeActivity.class);
      intent.putExtra("COUNT_OF_PLAYERS", count_of_players);
      intent.putExtra("TicTacToe_USER_LOGIN", user_login);
      startActivity(intent);
    } else {
      Intent intent = new Intent(this, TicTacToeActivity.class);
      intent.putExtra("COUNT_OF_PLAYERS", count_of_players);
      intent.putExtra("TicTacToe_FIRST_USER_LOGIN", first_user_login);
      intent.putExtra("TicTacToe_SECOND_USER_LOGIN", second_user_login);
      startActivity(intent);
    }
  }

  // open mexico dice
  private void openMexicoDiceGame() {
    if (count_of_players == 1) {
      Intent intent = new Intent(this, MexicoDiceActivity.class);
      intent.putExtra("COUNT_OF_PLAYERS", count_of_players);
      intent.putExtra("MexicoDice_USER_LOGIN", user_login);
      startActivity(intent);
    } else {
      Intent intent = new Intent(this, MexicoDiceActivity.class);
      intent.putExtra("COUNT_OF_PLAYERS", count_of_players);
      intent.putExtra("MexicoDice_FIRST_USER_LOGIN", first_user_login);
      intent.putExtra("MexicoDice_SECOND_USER_LOGIN", second_user_login);
      startActivity(intent);
    }
  }

  // open rock paper scissors lizard spock game
  private void openRockPaperScissorsLizardSpockGame() {
    if (count_of_players == 1) {
      Intent intent = new Intent(this, RockPaperScissorsLizardSpockActivity.class);
      intent.putExtra("COUNT_OF_PLAYERS", count_of_players);
      intent.putExtra("RockPaperScissorsLizardSpockGame_USER_LOGIN", user_login);
      startActivity(intent);
    } else {
      Intent intent = new Intent(this, RockPaperScissorsLizardSpockActivity.class);
      intent.putExtra("COUNT_OF_PLAYERS", count_of_players);
      intent.putExtra("RockPaperScissorsLizardSpockGame_FIRST_USER_LOGIN", first_user_login);
      intent.putExtra("RockPaperScissorsLizardSpockGame_SECOND_USER_LOGIN", second_user_login);
      startActivity(intent);
    }
  }
}
