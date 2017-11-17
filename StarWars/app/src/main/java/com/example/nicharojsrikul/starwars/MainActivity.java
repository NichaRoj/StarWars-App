package com.example.nicharojsrikul.starwars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/*
    Nicha Rojsrikul 5931259621 I intend for the ships to continue running till it runs out of the view
 */
public class MainActivity extends AppCompatActivity {
    private DrawClass viewDraw;
    private Timer timer1;
    private Timer timer2;
    private int delay;
    private boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewDraw = findViewById(R.id.viewDraw);
        if (savedInstanceState == null) {
            delay = 20;
            paused = false;

            startTextAnimate(delay);
        } else {
            delay = savedInstanceState.getInt("delay");
            paused = savedInstanceState.getBoolean("paused");
            if (!paused) {
                startTextAnimate(delay);
            }
        }
    }

    //When I tested, the app continued after switching in and out.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save variables using outState.put<>(String name, Object value)
        outState.putInt("delay", this.delay);
        outState.putBoolean("paused", this.paused);

    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        //re initialize variables with inState.get<>(String name)
        this.delay = inState.getInt("delay");
        this.paused = inState.getBoolean("paused");
    }

    private void startTextAnimate(int d) {
        timer1 = null;
        timer1 = new Timer();
        TimerTask textTask = new TimerTask() {
            @Override
            public void run() {
                viewDraw.dTextSize += 0.12f;
                viewDraw.dTextY += 4;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewDraw.invalidate();
                    }
                });
                if (!viewDraw.textRunning) {
                    timer1.cancel();
                    timer1.purge();
                    startShipAnimate(delay);
                    startBulletAnimate(delay / 2);
                }
            }
        };
        timer1.schedule(textTask, d, d);
    }

    private void startShipAnimate(int d) {
        timer1 = null;
        timer1 = new Timer();
        TimerTask shipTask = new TimerTask() {
            @Override
            public void run() {
                viewDraw.dShip2X += 2;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewDraw.invalidate();
                    }
                });
                if (viewDraw.ship1X + 120 < 0 || viewDraw.ship1Y > viewDraw.viewHeight) {
                    viewDraw.ended = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewDraw.invalidate();
                        }
                    });
                    timer1.cancel();
                    timer1.purge();
                    timer2.cancel();
                    timer2.purge();
                }else{
                    viewDraw.ended=false;
                }
            }
        };
        timer1.schedule(shipTask, d, d);
    }

    private void startBulletAnimate(int d) {
        timer2 = null;
        timer2 = new Timer();
        TimerTask bulletTask = new TimerTask() {
            @Override
            public void run() {
                viewDraw.dBulletX += 2;
                if (viewDraw.bulletX < viewDraw.ship2X) {
                    viewDraw.dBulletX = 0;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewDraw.invalidate();
                    }
                });
            }
        };
        timer2.schedule(bulletTask, d, d);
    }

    public void pause(View view) {
        Button btnPause = (Button) view;
        if (!viewDraw.ended) {
            if (paused) {
                paused = false;
                btnPause.setText("pause");
                if (viewDraw.textRunning) {
                    startTextAnimate(delay);
                } else {
                    startShipAnimate(delay);
                    startBulletAnimate(delay / 2);
                }
            } else {
                paused = true;
                btnPause.setText("start");
                timer1.cancel();
                timer1.purge();
                if (!viewDraw.textRunning) {
                    timer2.cancel();
                    timer2.purge();
                }
            }
        }
    }

    public void speed(View view) {
        if (!paused) {
            delay /= 2;
            timer1.cancel();
            timer1.purge();
            if (!viewDraw.textRunning) {
                timer2.cancel();
                timer2.purge();
            }
            if (viewDraw.textRunning) {
                startTextAnimate(delay);
            } else {
                startShipAnimate(delay);
                startBulletAnimate(delay / 2);
            }
        }
    }

    public void slow(View view) {
        if (!paused) {
            delay *= 2;
            timer1.cancel();
            timer1.purge();
            if (!viewDraw.textRunning) {
                timer2.cancel();
                timer2.purge();
            }
            if (viewDraw.textRunning) {
                startTextAnimate(delay);
            } else {
                startShipAnimate(delay);
                startBulletAnimate(delay / 2);
            }
        }
    }

    public void reset(View view) {
        if (paused) {
            pause(findViewById(R.id.btnPause));
        }
        if (!viewDraw.ended) {
            timer1.cancel();
            timer1.purge();
            if (!viewDraw.textRunning) {
                timer2.cancel();
                timer2.purge();
            }
        }
        viewDraw.dShip2X = 0;
        viewDraw.dTextY = 0;
        viewDraw.dBulletX = 0;
        viewDraw.textRunning = true;
        viewDraw.dTextSize = 0;
        viewDraw.ended = false;
        delay = 20;
        startTextAnimate(delay);
    }
}
