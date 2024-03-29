package com.example.myaudioplayer;

import static com.example.myaudioplayer.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements Runnable {
    TextView song_name, artist_name,duration_played,duration_total;
    ImageView cover_art,nextBtn, prevBtn,backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position=-1;

    static ArrayList<MusicFiles> listSongs=new ArrayList<>();

    static  Uri uri;
    static MediaPlayer mediaPlayer;

    private Handler handler=new Handler();

    private Thread playThread, prevThread, nextThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null&&fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        PlayerActivity.this.runOnUiThread(() -> {
            if (mediaPlayer != null) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                seekBar.setProgress(mCurrentPosition);
                duration_played.setText(formattedTime(mCurrentPosition));
            }
            handler.postDelayed( this, 1000);
        });

    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {

    }

    private void nextThreadBtn() {

    }

    private void playThreadBtn() {
        playThread = new Thread()
        {
            @Override
            public void run(){
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClickes();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClickes() {
    }
    private String formattedTime(int mCurrentPosition) {
        String totalout ="";
        String totalNew="";
        String seconds=String.valueOf(mCurrentPosition%60);
        String minutes=String.valueOf(mCurrentPosition/60);
        totalout=minutes+":"+seconds;
        totalNew=minutes+":"+"0"+seconds;
        if(seconds.length()==1){
            return totalNew;
        }
        else {
           return totalout;
        }
    }

    private void getIntentMethod() {
        position=getIntent().getIntExtra("position",-1);
        listSongs= musicFiles;
        if(listSongs!=null)
        {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri= Uri.parse(listSongs.get(position).getPath());
        }
         if (mediaPlayer!=null){
             mediaPlayer.stop();
             mediaPlayer.release();
             mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
             mediaPlayer.start();
         }
         else {
             mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
             mediaPlayer.start();
         }
         seekBar.setMax(mediaPlayer.getDuration()/1000);
         metaData(uri);
    }

    private void initViews(){
        song_name=findViewById(R.id.song_name);
        artist_name=findViewById(R.id.song_artist);
        duration_played=findViewById(R.id.durationPlayed);
        duration_total=findViewById(R.id.durationTotal);
        cover_art=findViewById(R.id.cover_art);
        nextBtn=findViewById(R.id.id_next);
        prevBtn=findViewById(R.id.id_prev);
        backBtn=findViewById(R.id.back_btn);
        shuffleBtn=findViewById(R.id.id_shuffle);
        repeatBtn=findViewById(R.id.id_repeat);
        playPauseBtn=findViewById(R.id.play_pause);
        seekBar=findViewById(R.id.seekBar);
    }

    private  void metaData (Uri uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal=Integer.parseInt(listSongs.get(position).getDuration())/1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art=retriever.getEmbeddedPicture();
        if (art != null){
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(cover_art);
        }
        else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.bewedoc)
                    .into(cover_art);
        }
    }

    @Override
    public void run() {

    }
}