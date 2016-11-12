package venture.cs414.android.monopoly2.frontend;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import venture.cs414.android.monopoly2.R;



public class MainActivity extends AppCompatActivity {

    TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        int numMiliSeconds = (10 * 60 * 1000);
        new CountDownTimer(numMiliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                int minutes = (((int)millisUntilFinished / 1000) / 60);
                int seconds = (((int)millisUntilFinished / 1000) % 60);
                String timerString = "Game Time:\n" + minutes + ":";
                if(seconds < 10){
                    timerString += "0";
                }
                timerString += seconds;
                setTitle(timerString);
            }

            public void onFinish() {
                //timerText.setText("done!");
                setTitle("Game Time:\n" + "0:00");
                //endGame();
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
