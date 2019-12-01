package com.example.flexbattle;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class AccountSettingsActivity extends AppCompatActivity {

  private static final int OPEN_CAMERA_CODE = 1;

  private static final int OPEN_GALLERY_CODE = 2;

  Button bt_takePhoto, bt_selectFromGallery, bt_applyChanges, bt_back;

  ImageView iv_avatar;

  EditText et_login, et_password, et_email, et_name, et_surname;

  DBHelper dbHelper;

  String user_login;

  Bitmap selectedPicture;

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

    // Apply changes listener
    bt_applyChanges.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            DBHelper.applyDataChanges(
                database,
                et_email.getText().toString(),
                et_name.getText().toString(),
                et_surname.getText().toString(),
                convertBitmapToByteArray(selectedPicture),
                user_login);
            finish();
          }
        });

    // Close settings
    bt_back.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            finish();
          }
        });

    // Take picture using camera
    bt_takePhoto.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, OPEN_CAMERA_CODE);
          }
        });

    // Select picture from gallery
    bt_selectFromGallery.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, OPEN_GALLERY_CODE);
          }
        });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Photo result
    if (requestCode == OPEN_CAMERA_CODE && resultCode == RESULT_OK) {
      if (data != null) {
        // this is the image selected by the user
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        iv_avatar.setImageBitmap(bitmap);
        selectedPicture = bitmap;
      }
    }

    // Gallery pick result
    if (requestCode == OPEN_GALLERY_CODE && resultCode == RESULT_OK) {
      if (data != null) {
        Uri imageUri = data.getData();
        Bitmap bitmap = null;
        try {
          bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
          e.printStackTrace();
        }
        iv_avatar.setImageBitmap(bitmap);
        selectedPicture = bitmap;
      }
    }
  }

  private void putLoadedDataToTextEdit(Pair<List<String>,byte[]> a) {

    List<String> userData = a.first;
    byte[] b = a.second;

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
    if (b != null) {
      Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
      iv_avatar.setImageBitmap(bmp);
    }

    et_login.setEnabled(false);
    et_password.setEnabled(false);
  }

  private byte[] convertBitmapToByteArray(Bitmap bitmap) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
    return bos.toByteArray();
  }
}
