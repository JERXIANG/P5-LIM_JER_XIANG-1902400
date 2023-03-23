package com.example.individualassignment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class HomePage extends AppCompatActivity {
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        music = MediaPlayer.create(this, R.raw.music);//music (bgm)
        music.setLooping(true);
        music.start();

        Button StartButton = findViewById(R.id.StartButton);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button ExitButton = findViewById(R.id.ExitButton);
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        music.release();
    }

    @Override
    protected void onPause() {
        music.stop();
        super.onPause();
    }
}
