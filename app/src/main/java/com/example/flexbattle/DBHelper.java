package com.example.flexbattle;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
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
          + " VARCHAR(30),"
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

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_USER);
    db.execSQL(CREATE_TABLE_USER_HAS_GAME);
    db.execSQL(CREATE_TABLE_GAME);
    insertGameIntoTable(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table if exists " + TABLE_USER);
    db.execSQL("drop table if exists " + TABLE_USER_HAS_GAME);
    db.execSQL("drop table if exists " + TABLE_GAME);
  }

  public void insertGameIntoTable(SQLiteDatabase database) {

    ContentValues contentValues = new ContentValues();

    contentValues.put(DBHelper.GAME_KEY_ID, 0);
    contentValues.put(DBHelper.GAME_KEY_TITLE, "Tic Tac Toe");
    contentValues.put(DBHelper.GAME_KEY_DESCRIPTION, "Play");
    contentValues.put(DBHelper.GAME_KEY_WAYTOWIN, "Win");
    contentValues.put(DBHelper.GAME_KEY_PRICE, 0);
    database.insert(DBHelper.TABLE_GAME, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.GAME_KEY_ID, 1);
    contentValues.put(DBHelper.GAME_KEY_TITLE, "Mexico dice");
    contentValues.put(DBHelper.GAME_KEY_DESCRIPTION, "Play");
    contentValues.put(DBHelper.GAME_KEY_WAYTOWIN, "Win");
    contentValues.put(DBHelper.GAME_KEY_PRICE, 10);
    database.insert(DBHelper.TABLE_GAME, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.GAME_KEY_ID, 2);
    contentValues.put(DBHelper.GAME_KEY_TITLE, "Rock Paper Scissors Lizard Spock");
    contentValues.put(DBHelper.GAME_KEY_DESCRIPTION, "Play");
    contentValues.put(DBHelper.GAME_KEY_WAYTOWIN, "Win");
    contentValues.put(DBHelper.GAME_KEY_PRICE, 20);
    database.insert(DBHelper.TABLE_GAME, null, contentValues);
    contentValues.clear();
  }
}
