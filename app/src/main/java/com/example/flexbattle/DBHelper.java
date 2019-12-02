package com.example.flexbattle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 2;
  public static final String DATABASE_NAME = "FlexBattleDb";

  public static final String TABLE_USER = "user";
  public static final String TABLE_USER_HAS_GAME = "user_has_game";
  public static final String TABLE_GAME = "game";

  public static final String USER_KEY_LOGIN = "login";
  public static final String USER_KEY_PASSWORD = "password";
  public static final String USER_KEY_EMAIL = "email";
  public static final String USER_KEY_NAME = "name";
  public static final String USER_KEY_SURNAME = "surname";
  public static final String USER_KEY_AVATAR_IMAGE = "avatarImage";
  public static final String USER_KEY_POINTS = "points";

  public static final String USER_HAS_GAME_KEY_USER_ID = "_user_Login";
  public static final String USER_HAS_GAME_KEY_GAME_ID = "_game_Title";
  public static final String USER_HAS_GAME_KEY_STATE = "state";

  public static final String GAME_KEY_ID = "_game_id";
  public static final String GAME_KEY_TITLE = "title";
  public static final String GAME_KEY_DESCRIPTION = "description";
  public static final String GAME_KEY_WAYTOWIN = "wayToWin";
  public static final String GAME_KEY_PRICE = "price";

  private static final String CREATE_TABLE_USER =
      "CREATE TABLE "
          + TABLE_USER
          + "("
          + USER_KEY_LOGIN
          + " VARCHAR(30) NOT NULL,"
          + USER_KEY_PASSWORD
          + " VARCHAR(30) NOT NULL,"
          + USER_KEY_EMAIL
          + " VARCHAR(30) NOT NULL,"
          + USER_KEY_NAME
          + " VARCHAR(30),"
          + USER_KEY_SURNAME
          + " VARCHAR(30),"
          + USER_KEY_AVATAR_IMAGE
          + " BLOB,"
          + USER_KEY_POINTS
          + " INTEGER NOT NULL,"
          + "PRIMARY KEY"
          + "("
          + USER_KEY_LOGIN
          + ")"
          + ")";

  private static final String CREATE_TABLE_USER_HAS_GAME =
      "CREATE TABLE "
          + TABLE_USER_HAS_GAME
          + "("
          + USER_HAS_GAME_KEY_USER_ID
          + " VARCHAR(30) NOT NULL,"
          + USER_HAS_GAME_KEY_GAME_ID
          + " VARCHAR(50) NOT NULL,"
          + USER_HAS_GAME_KEY_STATE
          + " INTEGER NOT NULL,"
          + "PRIMARY KEY"
          + "("
          + USER_HAS_GAME_KEY_USER_ID
          + ","
          + USER_HAS_GAME_KEY_GAME_ID
          + "),"
          + "FOREIGN KEY(_user_Login) REFERENCES user(login),"
          + "FOREIGN KEY(_game_Title) REFERENCES game(_game_id)"
          + ")";

  private static final String CREATE_TABLE_GAME =
      "CREATE TABLE "
          + TABLE_GAME
          + "("
          + GAME_KEY_ID
          + " INTEGER NOT NULL,"
          + GAME_KEY_TITLE
          + " VARCHAR(50),"
          + GAME_KEY_DESCRIPTION
          + " VARCHAR(300),"
          + GAME_KEY_WAYTOWIN
          + " VARCHAR(300),"
          + GAME_KEY_PRICE
          + " INTEGER NOT NULL,"
          + "PRIMARY KEY"
          + "("
          + GAME_KEY_ID
          + ")"
          + ")";

  public DBHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Check if user have enough money and subtract  money
  public static int buyGame(String id, String user_login, SQLiteDatabase database) {
    Cursor c = null;
    int game_id;
    int user_has_money;
    int game_has_price;
    int current_balnce;
    String user_money =
        "SELECT "
            + USER_KEY_POINTS
            + " FROM "
            + TABLE_USER
            + " WHERE "
            + USER_KEY_LOGIN
            + " = '"
            + user_login
            + "'";

    String game_price =
        "SELECT "
            + GAME_KEY_PRICE
            + " FROM "
            + TABLE_GAME
            + " WHERE "
            + GAME_KEY_TITLE
            + " = '"
            + id
            + "'";
    c = database.rawQuery(user_money, null);
    c.moveToFirst();
    user_has_money = c.getInt(c.getColumnIndex(USER_KEY_POINTS));
    c.close();
    c = database.rawQuery(game_price, null);
    c.moveToFirst();
    game_has_price = c.getInt(c.getColumnIndex(GAME_KEY_PRICE));
    c.close();

    String query =
        "SELECT "
            + GAME_KEY_ID
            + " From "
            + TABLE_GAME
            + " WHERE "
            + GAME_KEY_TITLE
            + "='"
            + id
            + "'";
    c = database.rawQuery(query, null);
    c.moveToFirst();
    game_id = c.getInt(c.getColumnIndex(GAME_KEY_ID));
    c.close();
    current_balnce = user_has_money - game_has_price;

    if (user_has_money >= game_has_price) {
      String query1 =
          " UPDATE "
              + TABLE_USER_HAS_GAME
              + " SET "
              + USER_HAS_GAME_KEY_STATE
              + " = "
              + 1
              + " WHERE "
              + USER_HAS_GAME_KEY_USER_ID
              + " = '"
              + user_login
              + "'"
              + " AND "
              + USER_HAS_GAME_KEY_GAME_ID
              + " = '"
              + game_id
              + "'";
      database.execSQL(query1);
      query1 =
          " UPDATE "
              + TABLE_USER
              + " SET "
              + USER_KEY_POINTS
              + " = "
              + current_balnce
              + " WHERE "
              + USER_KEY_LOGIN
              + " = '"
              + user_login
              + "'";
      database.execSQL(query1);
      return 1;
    } else {
      return -1;
    }
  }

  // check if user don't have this game already
  public static int checkIfGameIsAlreadyBought(
      String game_title, String user_login, SQLiteDatabase database) {
    Cursor c = null;
    Cursor c1 = null;
    int game_state;
    int game_id;

    String query =
        "SELECT "
            + GAME_KEY_ID
            + " From "
            + TABLE_GAME
            + " WHERE "
            + GAME_KEY_TITLE
            + "='"
            + game_title
            + "'";
    c = database.rawQuery(query, null);
    c.moveToFirst();
    game_id = c.getInt(c.getColumnIndex(GAME_KEY_ID));
    c.close();

    String query1 =
        " SELECT "
            + USER_HAS_GAME_KEY_STATE
            + " FROM "
            + TABLE_USER_HAS_GAME
            + " WHERE "
            + USER_HAS_GAME_KEY_USER_ID
            + " = '"
            + user_login
            + "'"
            + " AND "
            + USER_HAS_GAME_KEY_GAME_ID
            + " = '"
            + game_id
            + "'";
    c1 = database.rawQuery(query1, null);
    c1.moveToFirst();
    game_state = c1.getInt(c1.getColumnIndex(USER_HAS_GAME_KEY_STATE));
    c1.close();
    return game_state;
  }

  // Load data for settings
  public static Pair<List<String>, byte[]> preloadUserData(SQLiteDatabase database, String user_login) {
    byte[] img = new byte[0];
    List<String> userData = new ArrayList<>();
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
      int user_login_index = cursor.getColumnIndex(DBHelper.USER_KEY_LOGIN);
      int user_password_index = cursor.getColumnIndex(DBHelper.USER_KEY_PASSWORD);
      int user_email_index = cursor.getColumnIndex(DBHelper.USER_KEY_EMAIL);
      int user_name_index = cursor.getColumnIndex(DBHelper.USER_KEY_NAME);
      int user_surname_index = cursor.getColumnIndex(DBHelper.USER_KEY_SURNAME);
      int user_avatar_index = cursor.getColumnIndex(DBHelper.USER_KEY_AVATAR_IMAGE);
      do {
        userData.add(cursor.getString(user_login_index));
        userData.add(cursor.getString(user_password_index));
        userData.add(cursor.getString(user_email_index));
        userData.add(cursor.getString(user_name_index));
        userData.add(cursor.getString(user_surname_index));
        img = cursor.getBlob(user_avatar_index);
      } while (cursor.moveToNext());
    } else Log.d("mLog", "0 rows");
    cursor.close();
    return new Pair<>(userData, img);
  }

  // Insert updated user data
  public static void applyDataChanges(
          SQLiteDatabase database, String email, String name, String surname, byte[] bytes, String login) {
    ContentValues contentValues = new ContentValues();
    if (!email.trim().equals("")) {
      contentValues.put(USER_KEY_EMAIL, email);
      database.update(TABLE_USER, contentValues, USER_KEY_LOGIN + "='" + login + "'", null);
      contentValues.clear();
    }
    if (!name.trim().equals("")) {
      contentValues.put(USER_KEY_NAME, name);
      database.update(TABLE_USER, contentValues, USER_KEY_LOGIN + "='" + login + "'", null);
      contentValues.clear();
    }
    if (!surname.trim().equals("")) {
      contentValues.put(USER_KEY_SURNAME, surname);
      database.update(TABLE_USER, contentValues, USER_KEY_LOGIN + "='" + login + "'", null);
      contentValues.clear();
    }
    if(!(bytes.length == 0)){
      contentValues.put(USER_KEY_AVATAR_IMAGE, bytes);
      database.update(TABLE_USER, contentValues, USER_KEY_LOGIN + "='" + login + "'", null);
      contentValues.clear();
    }
  }

  //Insert new google account user
  public static void addNewGoogleUser(SQLiteDatabase database, String login, String password, String name, String surname, String email, byte[] avatarImage){
    ContentValues contentValues = new ContentValues();
    contentValues.put(DBHelper.USER_KEY_LOGIN, login);
    contentValues.put(DBHelper.USER_KEY_PASSWORD, password);
    contentValues.put(DBHelper.USER_KEY_EMAIL, email);
    contentValues.put(DBHelper.USER_KEY_NAME, name);
    contentValues.put(DBHelper.USER_KEY_SURNAME, surname);
    contentValues.put(DBHelper.USER_KEY_AVATAR_IMAGE, avatarImage);
    contentValues.put(DBHelper.USER_KEY_POINTS, 0);
    database.insert(DBHelper.TABLE_USER, null, contentValues);
    contentValues.clear();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_USER);
    db.execSQL(CREATE_TABLE_USER_HAS_GAME);
    db.execSQL(CREATE_TABLE_GAME);
    insertGameIntoTable(db);
    insertSuperUser(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table if exists " + TABLE_USER);
    db.execSQL("drop table if exists " + TABLE_USER_HAS_GAME);
    db.execSQL("drop table if exists " + TABLE_GAME);
  }

  // insert games
  public void insertGameIntoTable(SQLiteDatabase database) {

    ContentValues contentValues = new ContentValues();

    contentValues.put(DBHelper.GAME_KEY_ID, 0);
    contentValues.put(DBHelper.GAME_KEY_TITLE, "Rock Paper Scissors Lizard Spock");
    contentValues.put(DBHelper.GAME_KEY_DESCRIPTION, "Play");
    contentValues.put(DBHelper.GAME_KEY_WAYTOWIN, "Win");
    contentValues.put(DBHelper.GAME_KEY_PRICE, 0);
    database.insert(DBHelper.TABLE_GAME, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.GAME_KEY_ID, 1);
    contentValues.put(DBHelper.GAME_KEY_TITLE, "Mexico Dice");
    contentValues.put(DBHelper.GAME_KEY_DESCRIPTION, "Play");
    contentValues.put(DBHelper.GAME_KEY_WAYTOWIN, "Win");
    contentValues.put(DBHelper.GAME_KEY_PRICE, 10);
    database.insert(DBHelper.TABLE_GAME, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.GAME_KEY_ID, 2);
    contentValues.put(DBHelper.GAME_KEY_TITLE, "Tic Tac Toe");
    contentValues.put(DBHelper.GAME_KEY_DESCRIPTION, "Play");
    contentValues.put(DBHelper.GAME_KEY_WAYTOWIN, "Win");
    contentValues.put(DBHelper.GAME_KEY_PRICE, 20);
    database.insert(DBHelper.TABLE_GAME, null, contentValues);
    contentValues.clear();
  }

  // insert test user
  private void insertSuperUser(SQLiteDatabase database) {

    ContentValues contentValues = new ContentValues();
    contentValues.put(DBHelper.USER_KEY_LOGIN, "test");
    contentValues.put(DBHelper.USER_KEY_PASSWORD, "test");
    contentValues.put(DBHelper.USER_KEY_EMAIL, "test@gmail.com");
    contentValues.put(DBHelper.USER_KEY_POINTS, 100);
    database.insert(DBHelper.TABLE_USER, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_USER_ID, "test");
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_GAME_ID, 0);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_STATE, 1);
    database.insert(DBHelper.TABLE_USER_HAS_GAME, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_USER_ID, "test");
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_GAME_ID, 1);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_STATE, 1);
    database.insert(DBHelper.TABLE_USER_HAS_GAME, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_USER_ID, "test");
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_GAME_ID, 2);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_STATE, 0);
    database.insert(DBHelper.TABLE_USER_HAS_GAME, null, contentValues);
    contentValues.clear();
  }
}
