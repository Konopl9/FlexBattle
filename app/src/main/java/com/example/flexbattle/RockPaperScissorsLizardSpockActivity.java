package com.example.flexbattle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RockPaperScissorsLizardSpockActivity extends AppCompatActivity {

  private static final int ROCK = 0;
  private static final int SPOCK = 1;
  private static final int PAPER = 2;
  private static final int LIZARD = 3;
  private static final int SCISSORS = 4;

  private ImageView iv_cpu, iv_player;
  private Button rock, spock, paper, lizard, scissors;

  private TextView tv_info;

  private Random r;

  private int playedCPU, playedPLAYER;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rock_paper_scissors_lizard_spock);

    iv_cpu = findViewById(R.id.iv_cpu);
    iv_player = findViewById(R.id.iv_player);

    rock = findViewById(R.id.rock);
    spock = findViewById(R.id.spock);
    paper = findViewById(R.id.paper);
    scissors = findViewById(R.id.scissors);
    lizard = findViewById(R.id.lizard);

    tv_info = findViewById(R.id.tv_info);

    r = new Random();

    rock.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              playedPLAYER = ROCK;
              playedCPU = r.nextInt(5);
              setImages(playedPLAYER, playedCPU);
              tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
          }
        });
    spock.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              playedPLAYER = SPOCK;
              playedCPU = r.nextInt(5);
              setImages(playedPLAYER, playedCPU);
              tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
          }
        });
    paper.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              playedPLAYER = PAPER;
              playedCPU = r.nextInt(5);
              setImages(playedPLAYER, playedCPU);
              tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
          }
        });
    scissors.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              playedPLAYER = SCISSORS;
              playedCPU = r.nextInt(5);
              setImages(playedPLAYER, playedCPU);
              tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
          }
        });
    lizard.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              playedPLAYER = LIZARD;
              playedCPU = r.nextInt(5);
              setImages(playedPLAYER, playedCPU);
              tv_info.setText(getWinnerText(getWinner(playedPLAYER, playedCPU)));
          }
        });
  }

  // this decides the winner
  private int getWinner(int A, int B) {
    return A == B ? 0 : (A - B + 5) % 5 <= 2 ? 1 : -1;
    // returns 1 if A is the winner, return -1 if B is the winner, returns 0 if it is a draw
  }

  // Converts result from the getWinner() function to text
  private String getWinnerText(int result) {
    switch (result) {
      case 1:
        return "Winner Player";
      case -1:
        return "Winner CPU";
      case 0:
        return "Draw";
      default:
        return "";
    }
  }

  //set images of the picks
    private void setImages(int A, int B){
      switch (A){
          case ROCK:
              iv_player.setImageResource(R.drawable.rock);
              break;
          case SPOCK:
              iv_player.setImageResource(R.drawable.spock);
              break;
          case PAPER:
              iv_player.setImageResource(R.drawable.paper);
              break;
          case LIZARD:
              iv_player.setImageResource(R.drawable.lizard);
              break;
          case SCISSORS:
              iv_player.setImageResource(R.drawable.scissors);
              break;

      }
        switch (B){
            case ROCK:
                iv_cpu.setImageResource(R.drawable.rock);
                break;
            case SPOCK:
                iv_cpu.setImageResource(R.drawable.spock);
                break;
            case PAPER:
                iv_cpu.setImageResource(R.drawable.paper);
                break;
            case LIZARD:
                iv_cpu.setImageResource(R.drawable.lizard);
                break;
            case SCISSORS:
                iv_cpu.setImageResource(R.drawable.scissors);
                break;

        }
    }
}
