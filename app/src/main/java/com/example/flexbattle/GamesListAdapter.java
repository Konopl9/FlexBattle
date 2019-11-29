package com.example.flexbattle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class GamesListAdapter extends ArrayAdapter {

  // to reference the Activity
  private final Activity context;

  // to store the animal images
  private final Integer[] imageIDarray;

  // to store the list of countries
  private final String[] nameArray;

  // to store the list of countries
  private final String[] infoArray;

  //to store the list of game states
  private final List<Integer> gameStatesArray;

  GamesListAdapter(
          Activity context,
          String[] nameArrayParam,
          String[] infoArrayParam,
          Integer[] imageIDArrayParam,
          List<Integer> gameState) {

    super(context, R.layout.games_list_row, nameArrayParam);

    this.context = context;
    this.imageIDarray = imageIDArrayParam;
    this.nameArray = nameArrayParam;
    this.infoArray = infoArrayParam;
    this.gameStatesArray = gameState;
  }

  public View getView(int position, View view, ViewGroup parent) {
    LayoutInflater inflater = context.getLayoutInflater();
    @SuppressLint({"ViewHolder", "InflateParams"})
    View rowView = inflater.inflate(R.layout.games_list_row, null, true);
    Typeface boltTilteFont = ResourcesCompat.getFont(getContext(), R.font.gothambold);
    Typeface bookDescFont = ResourcesCompat.getFont(getContext(), R.font.gothammedium);

    // this code gets references to objects in the listview_row.xml file
    TextView nameTextField = rowView.findViewById(R.id.nameGame);
    TextView infoTextField = rowView.findViewById(R.id.descGame);
    ImageView imageView = rowView.findViewById(R.id.imgGame);
    nameTextField.setTextSize(19);
    nameTextField.setTypeface(boltTilteFont);
    infoTextField.setTypeface(bookDescFont);
    infoTextField.setTextSize(15);

    // this code sets the values of the objects to values from the arrays
    nameTextField.setText(nameArray[position]);
    infoTextField.setText(infoArray[position]);
    imageView.setImageResource(imageIDarray[position]);

    //check if user bought this game
    if(gameStatesArray.get(position) == 1){
      rowView.setEnabled(true);
    }
    else {
      rowView.setEnabled(false);
      rowView.setOnClickListener(null);
    }

    return rowView;
  }
}
