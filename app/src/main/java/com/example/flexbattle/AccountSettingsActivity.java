package com.example.flexbattle;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AccountSettingsActivity extends AppCompatActivity {

  Button bt_takePhoto, bt_selectFromGallery, bt_applyChanges, bt_back;

  ImageView iv_avatar;

  EditText et_login, et_password, et_email, et_name, et_surname;

  DBHelper dbHelper;

  List<String> userData;

  String user_login;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_settings);

    bt_takePhoto = findViewById(R.id.btnTakePhoto);
    bt_selectFromGallery = findViewById(R.id.btnOpenGallery);
    bt_applyChanges = findViewById(R.id.applyChanges);
    bt_back = findViewById(R.id.cancelChanges);

    iv_avatar = findViewById(R.id.profileImage);

    et_login = findViewById(R.id.editLogin);
    et_password = findViewById(R.id.editPassword);
    et_email = findViewById(R.id.editEmail);
    et_name = findViewById(R.id.editName);
    et_surname = findViewById(R.id.editSurname);

    // Db options
    dbHelper = new DBHelper(this);
    final SQLiteDatabase database = dbHelper.getReadableDatabase();

    // Intend data
    user_login = getIntent().getExtras().getString("USER_LOGIN");

    // Fill user data to text edit
    putLoadedDataToTextEdit(DBHelper.preloadUserData(database, user_login));

    //apply changes listener
      bt_applyChanges.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              DBHelper.applyDataChanges(database, et_email.getText().toString(), et_name.getText().toString(), et_surname.getText().toString(), user_login);
              finish();
          }
      });

      // close settings
      bt_back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });
  }

    private void putLoadedDataToTextEdit(List<String> userData) {

    String login = userData.get(0);
    String password = userData.get(1);
    String email = userData.get(2);
    String name = userData.get(3);
    String surname = userData.get(4);

    if (login != null) et_login.setText(login);
    if (password != null) et_password.setText(password);
    if (email != null) et_email.setText(email);
    if (name != null) et_name.setText(name);
    if (surname != null) et_surname.setText(surname);

    et_login.setEnabled(false);
    et_password.setEnabled(false);
  }

}
