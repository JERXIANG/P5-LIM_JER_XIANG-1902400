package com.example.individualassignment;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int total = 0;
    private SQLiteAdapter mySQLiteAdapter;
    private ImageButton currentHighlightedButton;
    private int currentLevel = 1;
    private int totalNumButton = 4;
    private ImageButton[] buttonsToHighlight;
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        music = MediaPlayer.create(this, R.raw.music);//background music
        music.setLooping(true);
        music.start();
        setLevel(currentLevel);
    }

    private void setLevel(int level) {
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();
        int lowestPointinTable = mySQLiteAdapter.selectLowestRank();
        long totalRowinTable = mySQLiteAdapter.totalRow();
        mySQLiteAdapter.close();

        currentLevel = level;
        totalNumButton = (int) Math.pow(level + 1, 2);
        buttonsToHighlight = new ImageButton[totalNumButton];

        LinearLayout ll = findViewById(R.id.linearLayout);

        TextView lefttime = findViewById(R.id.textTime);
        TextView showlevel = findViewById(R.id.textLevel);
        TextView showpoint = findViewById(R.id.textPoint);

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(level + 1);
        grid.setRowCount(level + 1);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        //set alignment
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.setMargins(0, 120, 0, 0);

        ConstraintLayout cl = new ConstraintLayout(this);
        for (int i = 0; i < totalNumButton; i=i+1) {
            buttonsToHighlight[i] = new ImageButton(this);
            buttonsToHighlight[i].setImageResource(R.drawable.magenta);
            buttonsToHighlight[i].setLayoutParams(new ConstraintLayout.LayoutParams(175, 175));
            grid.addView(buttonsToHighlight[i]);
        }
        for (ImageButton button : buttonsToHighlight) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == currentHighlightedButton) {
                        total++;
                        showpoint.setText("Point: " + total);
                        highlightAnotherRandomButton();
                    }
                }
            });
        }

        new Handler().post(new Runnable() {
            int seconds = 5;
            @Override
            public void run() {
                int secs = seconds % 60;
                String time = String.format("%2d", secs);
                if (seconds >= 0) {
                    seconds--;
                    lefttime.setText("Time: " + time);
                }
                new Handler().postDelayed(this, 1000);
            }
        });

        showpoint.setText("Point: " + total);
        showlevel.setText("Level: " + currentLevel);

        cl.addView(grid, layoutParams);
        ll.addView(cl);
        AlertDialog.Builder builderA = new AlertDialog.Builder(this);
        AlertDialog.Builder builderB = new AlertDialog.Builder(this);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                currentHighlightedButton.setOnClickListener(null);
                builderA.setTitle("Complete Level " + currentLevel);
                builderA.setMessage("Do you want to challenge level " + (currentLevel + 1) + "?");
                builderA.setCancelable(false);
                builderA.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ll.removeView(cl);
                        currentLevel = currentLevel + 1;
                        setLevel(currentLevel);
                    }
                });
                builderA.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (totalRowinTable < 25) {
                            Intent intent = new Intent(MainActivity.this, RegisterRank.class);
                            intent.putExtra("total", total);
                            finish();
                            startActivity(intent);
                        } else {
                            if (total > lowestPointinTable) {

                                mySQLiteAdapter.openToWrite();
                                mySQLiteAdapter.deleteLast();
                                mySQLiteAdapter.close();
                                Intent intent = new Intent(MainActivity.this, RegisterRank.class);
                                intent.putExtra("total", total);
                                finish();
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, Top25Leaderboard.class);
                                intent.putExtra("Point", total);
                                intent.putExtra("result", "Unfortunately, You didn't reach the top 25 ranking :(");
                                finish();
                                startActivity(intent);
                            }
                        }
                    }
                });
                AlertDialog dialog = builderA.create();
                Toast.makeText(getApplicationContext(), "Level Completed", Toast.LENGTH_SHORT).show();
                Handler handlerDialog = new Handler();
                handlerDialog.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                }, 1000);
            }
        };
        if (currentLevel < 5) {
            handler.postDelayed(runnable, 5000);
        } else if (currentLevel == 5) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentHighlightedButton.setOnClickListener(null);
                    builderB.setTitle("Complete Level " + currentLevel);
                    builderB.setMessage("Congratulation! You complete this game!");
                    builderB.setCancelable(false);
                    builderB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (totalRowinTable < 25) {
                                Intent intent = new Intent(MainActivity.this, RegisterRank.class);
                                intent.putExtra("total", total);
                                finish();
                                startActivity(intent);
                            } else {
                                if (total > lowestPointinTable) {
                                    mySQLiteAdapter.openToWrite();
                                    mySQLiteAdapter.deleteLast();
                                    mySQLiteAdapter.close();
                                    Intent intent = new Intent(MainActivity.this, RegisterRank.class);
                                    intent.putExtra("total", total);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(MainActivity.this, Top25Leaderboard.class);
                                    intent.putExtra("point", total);
                                    intent.putExtra("result", "Unfortunately, You didn't reach the top 25 ranking :(");
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        }
                    });

                    AlertDialog dialog2 = builderB.create();
                    Toast.makeText(getApplicationContext(), "Game End", Toast.LENGTH_SHORT).show();
                    Handler handlerDialog2 = new Handler();
                    handlerDialog2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog2.show();
                        }
                    }, 500);

                }
            }, 5000);
        }
        highlightRandomButton();
    }

    private void highlightRandomButton() {
        int randomIndex = new Random().nextInt(totalNumButton);


        ImageButton buttonToHighlight = buttonsToHighlight[randomIndex];

        buttonToHighlight.setImageResource(R.drawable.purple);
        if (buttonToHighlight == currentHighlightedButton) {
            highlightAnotherRandomButton();
        } else {
            currentHighlightedButton = buttonToHighlight;
        }
    }

    private void highlightAnotherRandomButton() {
        currentHighlightedButton.setImageResource(R.drawable.magenta);
        highlightRandomButton();

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
