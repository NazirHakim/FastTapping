package com.nazir.fasttapping;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView iv_tap;
    TextView tv_result, tv_info;

    int currentTaps = 0;
    boolean gameStarted = false;

    CountDownTimer timer;

    int bestResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_tap = findViewById(R.id.iv_tap);
        tv_result = findViewById(R.id.tv_result);
        tv_info = findViewById(R.id.tv_info);

        //get and display the best result
        final SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        bestResult = preferences.getInt("highScore", 0);

        tv_result.setText("Best Result " + bestResult);

        iv_tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameStarted){
                    //count the taps if the game is started
                currentTaps++;
                } else {
                    //start the game if it is not started
                    tv_info.setText("Tap, tap, tap....");
                    gameStarted = true;
                    timer.start();
                }
            }
        });

        //timer for 10 seconds with interval 1 second
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //display the remaing time
                long timeTillEnd = (millisUntilFinished / 1000) + 1;
                tv_result.setText("Time Remaining " + timeTillEnd);

            }

            @Override
            public void onFinish() {
                //the game is over
                iv_tap.setEnabled(false);
                gameStarted = false;
                tv_info.setText("Game Over!");

                //check the high score and save the new result if  better
                if (currentTaps > bestResult) {
                    bestResult = currentTaps;

                    SharedPreferences preferences1 = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("hightScore", bestResult);
                    editor.apply();
                }

                //display the best result and the current one
                    tv_result.setText("Best Result: " + bestResult + "\nCurrent Taps: " + currentTaps);

                //prepare for new game after 2 seconds 
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_tap.setEnabled(true);
                        tv_info.setText("Start Tapping");
                        currentTaps = 0;

                    }
                }, 200);

            }
        };
    }
}
