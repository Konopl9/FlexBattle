package com.example.flexbattle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class GamesListActivity extends AppCompatActivity {

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
    GamesListAdapter gamesListAdapter =
        new GamesListAdapter(this, nameArray, infoArray, imageArray);
    listView = findViewById(R.id.listviewID);
    listView.setAdapter(gamesListAdapter);
    listView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
              openTicTacToeGame();
            } else if (position == 2) {
              openRockPaperScissorsLizardSpockGame();
            }
          }
        });
  }

  private void openTicTacToeGame() {
    Intent intent = new Intent(this, TicTacToeActivity.class);
    startActivity(intent);
  }

  private void openRockPaperScissorsLizardSpockGame() {
    Intent intent = new Intent(this, RockPaperScissorsLizardSpockActivity.class);
    startActivity(intent);
  }
}
