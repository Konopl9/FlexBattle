package com.example.flexbattle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity implements View.OnClickListener {

  DBHelper dbHelper;

  EditText et_login, et_password, et_checkPassword, et_email;
  Button createAccountButton, createFacebookAccountButton;

  private static boolean isValidEmail(String email) {
    return !TextUtils.isEmpty(email)
        && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);

    createAccountButton = findViewById(R.id.createAccountButton);
    createAccountButton.setOnClickListener(this);
    createFacebookAccountButton = findViewById(R.id.useFacebookAccount);
    createFacebookAccountButton.setOnClickListener(this);

    et_login = findViewById(R.id.loginInput);
    et_password = findViewById(R.id.passwordInput);
    et_checkPassword = findViewById(R.id.repeatPassword);
    et_email = findViewById(R.id.emailRegistration);

    dbHelper = new DBHelper(this);
  }

  @Override
  public void onClick(View v) {

    String login = et_login.getText().toString();
    String password = et_password.getText().toString();
    String passwordRepeat = et_checkPassword.getText().toString();
    String email = et_email.getText().toString();

    SQLiteDatabase database = dbHelper.getWritableDatabase();

    Cursor dbCursor = database.query(DBHelper.TABLE_GAME, null, null, null, null, null, null);
    String[] columnNames = dbCursor.getColumnNames();
    for (String column : columnNames) {
      Log.d("Column=", column);
    }


    ContentValues contentValues = new ContentValues();

    switch (v.getId()) {
      case R.id.createAccountButton:
        if (isValidLogin(login)) {
          if (isValidPassword(password, passwordRepeat)) {
            if (isValidEmail(email)) {
              if (!isLoginAlreadyExist(login, database)) {
                sqlliteAddNewUserToDataBase(login, password, email, database, contentValues);
                sqliteAttachGamesToUser(login, database, contentValues);
                clearRegistrationField();
                openSoloLoginActivity();
              } else {
                Toast.makeText(getApplicationContext(), "USER ALREADY EXITS", Toast.LENGTH_LONG)
                    .show();
              }
            } else {
              wrongEmail();
            }
          } else {
            wrongPassword();
          }
        } else {
          wrongLogin();
        }
        break;
      case R.id.useFacebookAccount:
        printAllTables(database);
        break;
    }
  }

  private void sqliteAttachGamesToUser(
      String login, SQLiteDatabase database, ContentValues contentValues) {

    contentValues.put(DBHelper.USER_HAS_GAME_KEY_USER_ID, login);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_GAME_ID, 0);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_STATE, 1);
    database.insert(DBHelper.TABLE_USER_HAS_GAME, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_USER_ID, login);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_GAME_ID, 1);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_STATE, 0);
    database.insert(DBHelper.TABLE_USER_HAS_GAME, null, contentValues);
    contentValues.clear();
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_USER_ID, login);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_GAME_ID, 2);
    contentValues.put(DBHelper.USER_HAS_GAME_KEY_STATE, 0);
    database.insert(DBHelper.TABLE_USER_HAS_GAME, null, contentValues);
    contentValues.clear();
  }

  private void sqlliteAddNewUserToDataBase(
      String login,
      String password,
      String email,
      SQLiteDatabase database,
      ContentValues contentValues) {
    contentValues.put(DBHelper.USER_KEY_LOGIN, login);
    contentValues.put(DBHelper.USER_KEY_PASSWORD, password);
    contentValues.put(DBHelper.USER_KEY_EMAIL, email);
    contentValues.put(DBHelper.USER_KEY_POINTS, 0);
    database.insert(DBHelper.TABLE_USER, null, contentValues);
    contentValues.clear();
  }

  private void wrongPassword() {
    et_password.setText("");
    et_checkPassword.setText("");
    et_password.setHintTextColor(Color.RED);
    et_checkPassword.setHintTextColor(Color.RED);
  }

  private void wrongLogin() {
    et_login.setText("");
    et_login.setHintTextColor(Color.RED);
  }

  private void wrongEmail() {
    et_email.setText("");
    et_email.setHintTextColor(Color.RED);
  }

  private boolean isValidPassword(String password, String repeatedPassword) {
    return !TextUtils.isEmpty(password) && password.equals(repeatedPassword);
  }

  private boolean isValidLogin(String login) {
    return !TextUtils.isEmpty(login);
  }

  private boolean isLoginAlreadyExist(String login, SQLiteDatabase database) {
    Cursor cursor = database.query(DBHelper.TABLE_USER, null, null, null, null, null, null);
    if (cursor.moveToFirst()) {
      int idLogin = cursor.getColumnIndex(DBHelper.USER_KEY_LOGIN);
      do {
        if(cursor.getString(idLogin).equals(login)){
          cursor.close();
          return true;
        }
      } while (cursor.moveToNext());
    }
    cursor.close();
    return false;
  }

  private void clearRegistrationField() {
    et_login.setText("");
    et_password.setText("");
    et_checkPassword.setText("");
    et_email.setText("");
  }

  private void openSoloLoginActivity() {
    Intent intent = new Intent(this, SoloLoginActivity.class);
    startActivity(intent);
  }

  private void printAllTables(SQLiteDatabase database) {
    Cursor cursor = database.query(DBHelper.TABLE_USER, null, null, null, null, null, null);
    Cursor cursor1 =
        database.query(DBHelper.TABLE_USER_HAS_GAME, null, null, null, null, null, null);
    Cursor cursor2 = database.query(DBHelper.TABLE_GAME, null, null, null, null, null, null);
    if (cursor.moveToFirst()) {
      int idIndex = cursor.getColumnIndex(DBHelper.USER_KEY_LOGIN);
      int nameIndex = cursor.getColumnIndex(DBHelper.USER_KEY_PASSWORD);
      int emailIndex = cursor.getColumnIndex(DBHelper.USER_KEY_EMAIL);
      do {
        Log.d(
            "mLog",
            "Login = "
                + cursor.getString(idIndex)
                + ", name = "
                + cursor.getString(nameIndex)
                + ", email = "
                + cursor.getString(emailIndex));
      } while (cursor.moveToNext());
    } else Log.d("mLog", "0 rows");
    cursor.close();

    if (cursor1.moveToFirst()) {
      while (!cursor1.isAfterLast()) {
        int idIndex1 = cursor1.getColumnIndex(DBHelper.USER_HAS_GAME_KEY_USER_ID);
        int nameIndex1 = cursor1.getColumnIndex(DBHelper.USER_HAS_GAME_KEY_GAME_ID);
        int emailIndex1 = cursor1.getColumnIndex(DBHelper.USER_HAS_GAME_KEY_STATE);
        do {
          Log.d(
              "mLog",
              "USER = "
                  + cursor1.getString(idIndex1)
                  + ", GAME = "
                  + cursor1.getString(nameIndex1)
                  + ", STATE = "
                  + cursor1.getString(emailIndex1));
        } while (cursor1.moveToNext());
      }
    } else Log.d("mLog", "0 rows");
    cursor1.close();

    if (cursor2.moveToFirst()) {
      while (!cursor2.isAfterLast()) {
        int idIndex1 = cursor2.getColumnIndex(DBHelper.GAME_KEY_ID);
        int nameIndex1 = cursor2.getColumnIndex(DBHelper.GAME_KEY_TITLE);
        int emailIndex1 = cursor2.getColumnIndex(DBHelper.GAME_KEY_DESCRIPTION);
        int emailIndex2 = cursor2.getColumnIndex(DBHelper.GAME_KEY_WAYTOWIN);
        int emailIndex3 = cursor2.getColumnIndex(DBHelper.GAME_KEY_PRICE);
        do {
          Log.d(
              "mLog",
              "ID = "
                  + cursor2.getString(idIndex1)
                  + ", TITLE = "
                  + cursor2.getString(nameIndex1)
                  + ", DESC = "
                  + cursor2.getString(emailIndex1)
                  + ", WAYTOWIN = "
                  + cursor2.getString(emailIndex2)
                  + ", PRICE = "
                  + cursor2.getString(emailIndex3));
        } while (cursor2.moveToNext());
      }
    } else Log.d("mLog", "0 rows");
    cursor2.close();
  }
}
