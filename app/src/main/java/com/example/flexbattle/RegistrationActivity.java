package com.example.flexbattle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class RegistrationActivity extends Activity {

  DBHelper dbHelper;

  EditText et_login, et_password, et_checkPassword, et_email;
  Button createAccountButton;

  SignInButton useFacebookAccount;

  GoogleSignInClient mGoogleSignInClient;

  int RC_SIGN_IN = 0;

  String user_login;

  byte[] userAvatarImage;

  private static boolean isValidEmail(String email) {
    return !TextUtils.isEmpty(email)
        && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);

    createAccountButton = findViewById(R.id.createAccountButton);
    useFacebookAccount = findViewById(R.id.sign_in_button);
    createAccountButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            addNewUserButton();
          }
        });
    useFacebookAccount.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            signIn();
          }
        });

    et_login = findViewById(R.id.loginInput);
    et_password = findViewById(R.id.passwordInput);
    et_checkPassword = findViewById(R.id.repeatPassword);
    et_email = findViewById(R.id.emailRegistration);

    dbHelper = new DBHelper(this);

    // Google
    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
  }

  private void signIn() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);

      // Signed in successfully, show authenticated UI.
      insertUserGoogleData();
      // updateUI(account);
    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
      // updateUI(null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      // The Task returned from this call is always completed, no need to attach
      // a listener.
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      handleSignInResult(task);
    }
  }

  // Google get user data
  void insertUserGoogleData() throws IOException {
    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
    if (acct != null) {
      String personName = acct.getGivenName();
      String personFamilyName = acct.getFamilyName();
      String personEmail = acct.getEmail();
      Uri personPhoto = acct.getPhotoUrl();
      // add global login
      user_login = personName;
      // convert picture

      // database
      SQLiteDatabase database = dbHelper.getWritableDatabase();

      if (!isLoginAlreadyExist(personName, database)) {
        ContentValues contentValues = new ContentValues();
        DBHelper.addNewGoogleUser(
            database,
            personName,
            personFamilyName,
            personName,
            personFamilyName,
            personEmail,
            userAvatarImage);
        sqliteAttachGamesToUser(personName, database, contentValues);
        contentValues.clear();
        openGameListActivity();
      } else {
        openGameListActivity();
      }
    }
  }

  private void signOut() {
    mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                // ...
              }
            });
  }

  private void addNewUserButton() {
    String login = et_login.getText().toString();
    String password = et_password.getText().toString();
    String passwordRepeat = et_checkPassword.getText().toString();
    String email = et_email.getText().toString();
    SQLiteDatabase database = dbHelper.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    if (isValidLogin(login)) {
      if (isValidPassword(password, passwordRepeat)) {
        if (isValidEmail(email)) {
          if (!isLoginAlreadyExist(login, database)) {
            sqlliteAddNewUserToDataBase(login, password, email, database, contentValues);
            sqliteAttachGamesToUser(login, database, contentValues);
            clearRegistrationField();
            openSoloLoginActivity();
          } else {
            Toast.makeText(getApplicationContext(), "USER ALREADY EXITS", Toast.LENGTH_LONG).show();
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
        if (cursor.getString(idLogin).equals(login)) {
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

  private void openGameListActivity() {
    Intent intent = new Intent(this, GamesListActivity.class);
    intent.putExtra("COUNT_OF_PLAYERS", 1);
    intent.putExtra("PLAYER_LOGIN", user_login);
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
