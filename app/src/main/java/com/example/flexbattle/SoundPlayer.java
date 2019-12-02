package com.example.flexbattle;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

  private static SoundPool soundPool;
  private static int rockSound;
  private static int paperSound;
  private static int scissorsSound;
  private static int lizardSound;
  private static int spockSound;
  private static int crossSound;
  private static int circleSound;
  private static int diceSound;
  private static int winSound;
  private static int backgroundSound;
  final int SOUND_POOL_MAX = 10;
  Context context;
  private AudioAttributes audioAttributes;

  public SoundPlayer(Context context) {

    this.context = context;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      AudioAttributes audioAttributes =
          new AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_GAME)
              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
              .build();

      soundPool =
          new SoundPool.Builder()
              .setAudioAttributes(audioAttributes)
              .setMaxStreams(SOUND_POOL_MAX)
              .build();
    } else {
      soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
    }

    rockSound = soundPool.load(context, R.raw.rock, 1);
    paperSound = soundPool.load(context, R.raw.paper, 1);
    scissorsSound = soundPool.load(context, R.raw.scissors, 1);
    lizardSound = soundPool.load(context, R.raw.lizard, 1);
    spockSound = soundPool.load(context, R.raw.spock, 1);
    crossSound = soundPool.load(context, R.raw.cross, 1);
    circleSound = soundPool.load(context, R.raw.circle, 1);
    diceSound = soundPool.load(context, R.raw.dice, 1);
    winSound = soundPool.load(context, R.raw.win, 1);
    backgroundSound = soundPool.load(context, R.raw.background, 1);
  }

  public void playRockSound(Context context) {
    soundPool.play(rockSound, 1, 1, 1, 0, 1.0f);
  }

  public void playPaperSound(Context context) {
    soundPool.play(paperSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void playScissorsSound(Context context) {
    soundPool.play(scissorsSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void playLizardSound(Context context) {
    soundPool.play(lizardSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void playSpockSound(Context context) {
    soundPool.play(spockSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void playCrossSound(Context context) {
    soundPool.play(crossSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void playCircleSound(Context context) {
    soundPool.play(circleSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void playDiceSound(Context context) {
    soundPool.play(diceSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void playWinSound(Context context) {
    soundPool.play(winSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void playBackgroundSound(Context context) {
    soundPool.play(backgroundSound, 1.0f, 1.0f, 1, 0, 1.0f);
  }
}
