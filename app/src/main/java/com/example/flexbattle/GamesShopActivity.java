package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GamesShopActivity extends AppCompatActivity {

  DBHelper dbHelper;

  ListView listView;

  String user_login;

  // Game Titles
  String[] nameArray = {"Rock Paper Scissors Lizard Spock", "Mexico Dice", "Tic Tac Toe"};

  // Image for game icons
  Integer[] imageArray = {
    R.drawable.rpsls_icon, R.drawable.mexico_dice_icon, R.drawable.tic_tac_toe_icon
  };

  private TableLayout tableLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_games_shop);
    user_login = getIntent().getExtras().getString("USER_LOGIN");
    initView();
    loadData();

    // Db options
    dbHelper = new DBHelper(this);
  }

  private void initView() {
    tableLayout = findViewById(R.id.tableLayoutProduct);
  }

  private void loadData() {

    List<ShopGameItem> products = new ArrayList<ShopGameItem>();
    products.add(new ShopGameItem("Rock Paper Scissors Lizard Spock", 0, "Game"));
    products.add(new ShopGameItem("Mexico Dice", 10, "Game1"));
    products.add(new ShopGameItem("Tic Tac Toe", 20, "Game2"));
    // products.add(new ShopGameItem(user_login, getUserMoney(database, user_login)));

    createColumns();

    fillData(products);
  }

  private void createColumns() {
    TableRow tableRow = new TableRow(this);
    tableRow.setLayoutParams(
        new TableRow.LayoutParams(
            TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

    // Name Title
    TextView textViewTitle = new TextView(this);
    textViewTitle.setText("Title");
    textViewTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewTitle.setPadding(5, 5, 5, 0);
    tableRow.addView(textViewTitle);

    // Price Description
    TextView textViewDescription = new TextView(this);
    textViewDescription.setText("Description");
    textViewDescription.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewDescription.setPadding(5, 5, 5, 0);
    tableRow.addView(textViewDescription);

    // PRICE
    TextView textViewPrice = new TextView(this);
    textViewPrice.setText("Price");
    textViewPrice.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewPrice.setPadding(5, 5, 5, 0);
    tableRow.addView(textViewPrice);

    tableLayout.addView(
        tableRow,
        new TableLayout.LayoutParams(
            TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

    // Add Divider
    tableRow = new TableRow(this);
    tableRow.setLayoutParams(
        new TableRow.LayoutParams(
            TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

    // Title
    textViewTitle = new TextView(this);
    textViewTitle.setText("--------------------");
    textViewTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewTitle.setPadding(5, 5, 5, 0);
    tableRow.addView(textViewTitle);

    // Description
    textViewDescription = new TextView(this);
    textViewDescription.setText("---------------");
    textViewDescription.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewDescription.setPadding(5, 5, 5, 0);
    tableRow.addView(textViewDescription);

    // Price
    textViewPrice = new TextView(this);
    textViewPrice.setText("---");
    textViewPrice.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    textViewPrice.setPadding(5, 5, 5, 0);
    tableRow.addView(textViewPrice);

    tableLayout.addView(
        tableRow,
        new TableLayout.LayoutParams(
            TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
  }

  private void fillData(List<ShopGameItem> shopGameItems) {
    for (ShopGameItem shopGameItem : shopGameItems) {
      final TableRow tableRow = new TableRow(this);
      tableRow.setLayoutParams(
          new TableRow.LayoutParams(
              TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

      tableRow.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              TableRow currentRow = (TableRow) view;
              TextView textViewId = (TextView) currentRow.getChildAt(0);
              final String id = textViewId.getText().toString();

              // Check if game is not already bought
              final SQLiteDatabase database = dbHelper.getWritableDatabase();
              if (DBHelper.checkIfGameIsAlreadyBought(id, user_login, database) == 1) {
                Toast.makeText(
                        getApplicationContext(), "You already have this game", Toast.LENGTH_LONG)
                    .show();
              } else {
                createAndShowAlertDialog(id, database);
              }
            }
          });

      // Title
      TextView textViewTitle = new TextView(this);
      textViewTitle.setText(shopGameItem.getGame_title());
      textViewTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
      textViewTitle.setPadding(5, 5, 5, 0);
      tableRow.addView(textViewTitle);

      // Description
      TextView textViewDescription = new TextView(this);
      textViewDescription.setText(shopGameItem.getDescription());
      textViewDescription.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
      textViewDescription.setPadding(5, 5, 5, 0);
      tableRow.addView(textViewDescription);

      // Price
      TextView textViewPrice = new TextView(this);
      textViewPrice.setText(String.valueOf(shopGameItem.getGame_price()));
      textViewPrice.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
      textViewPrice.setPadding(5, 5, 5, 0);
      tableRow.addView(textViewPrice);

      tableLayout.addView(
          tableRow,
          new TableLayout.LayoutParams(
              TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }
  }

  private void createAndShowAlertDialog(final String user_id, final SQLiteDatabase database) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Are you sure that you what to buy this game?");
    builder.setPositiveButton(
        android.R.string.yes,
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            if (DBHelper.buyGame(user_id, user_login, database) == 1) {
              // buy game
              Toast.makeText(
                      getApplicationContext(), "Game was added to you game list", Toast.LENGTH_LONG)
                  .show();
            } else {
              Toast.makeText(getApplicationContext(), "Not enough money", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
          }
        });
    builder.setNegativeButton(
        android.R.string.cancel,
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // TODO
            dialog.dismiss();
          }
        });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

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
}
