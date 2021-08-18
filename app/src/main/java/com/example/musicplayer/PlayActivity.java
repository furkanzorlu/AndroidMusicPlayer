package com.example.musicplayer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class PlayActivity extends Activity   {
    ArrayList<File> mySongs;
    int position;

     MediaPlayer mediaPlayer ;

    ImageView play,stop,forward,back,next,songback,hareket ;
    double starttime,finaltime=0;
    SeekBar seekBar;
    Button sec;
    static int oneTimeOnly = 0;
    Handler myHandler = new Handler();;
    TextView finaltime_textview,starttime_textview,name;
    String songName;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a);
        play=findViewById(R.id.bt_play);
        sec=findViewById(R.id.sec);
        back=findViewById(R.id.bt_rew);

        hareket=findViewById(R.id.hareket);
        stop=findViewById(R.id.bt_pause);
        next=findViewById(R.id.next);
        songback=findViewById(R.id.back);
        forward=findViewById(R.id.bt_ff);
        seekBar=findViewById(R.id.seek_bar);


        name=findViewById(R.id.name);
        starttime_textview=findViewById(R.id.player_position);
        finaltime_textview=findViewById(R.id.player_duration);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getIntegerArrayList("songs");
        String sName = intent.getStringExtra("songname");
        position = bundle.getInt("pos");
        name.setSelected(true);

        //Extracting the fileName form the ArrayList
        Uri uri = Uri.parse(mySongs.get(position).toString());
        songName = mySongs.get(position).getName();
        name.setText(songName);






        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        //CreateNotification.createNotification(PlayActivity.this,songName,R.drawable.ic_launcher_background,position,mySongs.size()-1);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        // updateSeekBar.start();
        //totTime.setText(createTimeLabel(mediaPlayer.getDuration()));
        // curTime.setText(getTimeString(position));

        // myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try {

                            position = ((position + 1) % mySongs.size());
                            Uri uri = Uri.parse(mySongs.get(position).toString());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                            songName = mySongs.get(position).getName();
                            //name.setText(songName);
                            mediaPlayer.start();

                            //totTime.setText(createTimeLabel(mediaPlayer.getDuration()));
                            // curTime.setText(getTimeString(position));
                            seekBar.setMax(mediaPlayer.getDuration());
                           // updateSeekBar.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        sürealma();
        if (mediaPlayer.isPlaying()) {

            play.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
        }
        startAnimation(hareket, 360f);






        songback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {

                    play.setVisibility(View.GONE);
                    stop.setVisibility(View.VISIBLE);
                }
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position-1) % mySongs.size());
                if (position < 0)
                    position = mySongs.size() - 1;
                Uri uri1 = Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri1);
                songName=mySongs.get(position).getName();
                name.setText(songName);
                mediaPlayer.start();
                sürealma();
                startAnimation(hareket, -360f);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {

                    play.setVisibility(View.GONE);
                    stop.setVisibility(View.VISIBLE);
                }
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position + 1) % mySongs.size());
                Uri uri1 = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
                songName = mySongs.get(position).getName();
                name.setText(songName);
                mediaPlayer.start();
                startAnimation(hareket, 360f);



                sürealma();

            }
        });
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         Intent intent=new Intent(getApplicationContext(),MainActivity.class);
         startActivity(intent);
       mediaPlayer.stop();





            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {

                    mediaPlayer.seekTo(i);
                }



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                //check condition
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    //when media is playing and current position is greater than 5 seconds
                    // Rewind for 5 seconds
                    currentPosition = currentPosition - 5000;
                    //Get current position on text view

                    //Set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                //Get duration of media player
                int duration = mediaPlayer.getDuration();
                //Check condition
                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    //when media is playing and duration is not equal to current position

                    // Fast forward for 5 seconds
                    currentPosition = currentPosition + 5000;
                    //set current position on text view

                    //set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                play.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);


            }
        });

       play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               stop.setVisibility(View.VISIBLE);
               play.setVisibility(View.GONE);
               mediaPlayer.start();
               sürealma();



           }

       });

    }
    public void sarkı_sec(){











    }
    public void sürealma(){
        finaltime = mediaPlayer.getDuration();
        starttime = mediaPlayer.getCurrentPosition();
        if(oneTimeOnly == 0){
            seekBar.setMax((int) finaltime);
            oneTimeOnly = 1;
        }
        finaltime_textview.setText(String.format("0%d : %d ",
                TimeUnit.MILLISECONDS.toMinutes((long) finaltime),
                TimeUnit.MILLISECONDS.toSeconds((long) finaltime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) finaltime))));
        starttime_textview.setText(String.format("%d : %d ",
                TimeUnit.MILLISECONDS.toMinutes((long) starttime),
                TimeUnit.MILLISECONDS.toSeconds((long) starttime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) starttime)))
        );
        seekBar.setProgress((int)starttime);
        myHandler.postDelayed(runnable,100);}
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                starttime = mediaPlayer.getCurrentPosition();
                starttime_textview.setText(String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes((long) starttime),
                        TimeUnit.MILLISECONDS.toSeconds((long) starttime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) starttime)))
                );
                seekBar.setProgress((int)starttime);
                myHandler.postDelayed(this, 100);

            }
        };
    public void startAnimation(View view, Float degree) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(hareket, "rotation", 0f, degree);
        objectAnimator.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();

    }

}
