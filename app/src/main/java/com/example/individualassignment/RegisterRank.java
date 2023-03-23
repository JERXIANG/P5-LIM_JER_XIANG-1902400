package com.example.individualassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterRank extends AppCompatActivity {
    private SQLiteAdapter mySQLiteAdapter;
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_rank);
        music = MediaPlayer.create(this, R.raw.music);
        music.setLooping(true);
        music.start();
        mySQLiteAdapter = new SQLiteAdapter(this);

        EditText playerName = findViewById(R.id.editTextName);
        TextView playerPoint = findViewById(R.id.playerPoint);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Haiyahhhh!");
        builder.setMessage("Don't Forget to put your Name.");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();

        int point = getIntent().getIntExtra("total", 0);
        playerPoint.setText("Point: " + point);

        Button SubmitButton = findViewById(R.id.SubmitButton);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(playerName.getText())) {
                    alert.show();
                } else {
                    mySQLiteAdapter.openToWrite();
                    mySQLiteAdapter.insert(String.valueOf(playerName.getText()), point);

                    mySQLiteAdapter.close();
                    Intent i = new Intent(RegisterRank.this, Top25Leaderboard.class);
                    i.putExtra("result", "Congrazzzz, You reach the top 25 ranking!");
                    i.putExtra("point", point);
                    startActivity(i);
                    finish();
                }
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

