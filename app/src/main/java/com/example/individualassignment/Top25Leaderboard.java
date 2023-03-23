package com.example.individualassignment;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Top25Leaderboard extends AppCompatActivity {
    private SQLiteAdapter mySQLiteAdapter;
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top25_leaderboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        music = MediaPlayer.create(this, R.raw.music);
        music.setLooping(true);
        music.start();

        Intent intent = getIntent();
        int point = intent.getIntExtra("point", 0);
        String message = intent.getStringExtra("result");

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();
        String finalresult = mySQLiteAdapter.listAllName();
        String finalPoint = mySQLiteAdapter.listAllPoint();

        TextView playerPoint = findViewById(R.id.playerPoint);
        playerPoint.setText("Score \n\n"+finalPoint);
        playerPoint.setTypeface(null, Typeface.BOLD);

        TextView playerName = findViewById(R.id.playerName);
        playerName.setText("Name \n\n" + finalresult);
        playerName.setTypeface(null, Typeface.BOLD);

        TextView playerMessage = findViewById(R.id.playerMessage);
        playerMessage.setText(message + " You have find out total " + point + " different colour in this game!");

        Button bckHome = findViewById(R.id.bckHome);
        bckHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Top25Leaderboard.this, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        mySQLiteAdapter.close();
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
