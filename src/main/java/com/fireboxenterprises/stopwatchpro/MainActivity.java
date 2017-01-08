package com.fireboxenterprises.stopwatchpro;

import android.icu.text.DateFormat;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainActivity extends AppCompatActivity {

    public double time = 0;
    public int lapNumber = 0;
    public final int maxLaps = 9;
    public long currentTime;
    private Handler mHandler;
    public TextView timeText;

    private AdView mAdView;

    final int delay = 0; // delay for 0 sec.
    final int period = 1;

    public boolean running;

    TimerTask increment = new TimerTask() {
        @Override
        public void run() {
            //do work

            runOnUiThread(new Runnable(){
                public void run() {

                    if(running)
                    {
                        time += System.currentTimeMillis() - currentTime;
                        String timeVal = getTimeText();

                        timeText.setText(timeVal);

                        currentTime = System.currentTimeMillis();
                    }
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("6C67750F5C28165E4B84A6218F6FCCF8").build();
        mAdView.loadAd(adRequest);

        timeText = (TextView) findViewById(R.id.timeText);

        running = false;

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor( 1 );

        final Button startButton = (Button) findViewById(R.id.startButton);
        final Button lapButton = (Button) findViewById(R.id.lapButton);
        final TableLayout lapContainer = (TableLayout) findViewById(R.id.lapContainer);

        lapButton.setVisibility(lapButton.INVISIBLE);

        executor.scheduleAtFixedRate(increment, delay, period, MILLISECONDS );

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(running)
                {
                    running = false;
                    startButton.setText("Start");
                    lapButton.setVisibility(lapButton.INVISIBLE);
                    time = 0;
                }
                else
                {
                    running = true;
                    startButton.setText("Stop");
                    lapButton.setVisibility(lapButton.VISIBLE);
                    currentTime = System.currentTimeMillis();
                }
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(running)
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView)((TableRow)lapContainer.getChildAt(lapNumber)).getChildAt(0)).setText(Integer.toString(lapNumber) +"\t\t");
                            ((TextView)((TableRow)lapContainer.getChildAt(lapNumber)).getChildAt(1)).setText(getTimeText());
                            lapNumber++;

                            if(lapNumber >= maxLaps)
                            {
                                lapNumber = 0;
                            }
                        }
                    });
                }
            }
        });

    }

    private String getTimeText()
    {
        String timeVal = String.valueOf(time / 1000.0);

        while(timeVal.substring(timeVal.indexOf('.')).length() < 4)
        {
            timeVal += "0";
        }

        return timeVal;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
