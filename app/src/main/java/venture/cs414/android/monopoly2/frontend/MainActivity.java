package venture.cs414.android.monopoly2.frontend;

import android.content.Intent;
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
import android.widget.Toast;

import venture.cs414.android.monopoly2.R;



public class MainActivity extends AppCompatActivity {

    TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int numPlayers = getIntent().getIntExtra("numPlayers", 2);
        int numMinutes = getIntent().getIntExtra("numMinutes", 5);

        int numMiliSeconds = (numMinutes * 60 * 1000);
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
        switch(id) {
            case R.id.menu_roll:
                try {
                    //implement call
                    //Toast.makeText(getApplicationContext(), "You rolled a " + die1 + " and a " + die2, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error rolling", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_sell_property:
                try {
                    //implement call
                    Intent intent = new Intent(this, SellProperty.class);
                    //Todo need to pass the serializable object when it is created
                    //intent.putExtra("difficulty", 1);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_buy_house:
                try {
                    //implement call
                    Intent intent = new Intent(this, BuyHouse.class);
                    //Todo need to pass the serializable object when it is created
                    //intent.putExtra("difficulty", 1);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_sell_house:
                try {
                    //implement call
                    Intent intent = new Intent(this, SellHouse.class);
                    //Todo need to pass the serializable object when it is created
                    //intent.putExtra("difficulty", 1);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_buy_hotel:
                try {
                    //implement call
                    Intent intent = new Intent(this, BuyHotel.class);
                    //Todo need to pass the serializable object when it is created
                    //intent.putExtra("difficulty", 1);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_sell_hotel:
                try {
                    //implement call
                    Intent intent = new Intent(this, SellHotel.class);
                    //Todo need to pass the serializable object when it is created
                    //intent.putExtra("difficulty", 1);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_mortgage:
                try {
                    //implement call
                    Intent intent = new Intent(this, MortgageProperty.class);
                    //Todo need to pass the serializable object when it is created
                    //intent.putExtra("difficulty", 1);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_end_turn:
                try {
                    //implement call
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_quit:
                try {
                    //implement call
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void sellProperty(View view){
        Intent intent = new Intent(this, SellProperty.class);
        //Todo need to pass the serializable object when it is created
        //intent.putExtra("difficulty", 1);
        startActivity(intent);
        finish();
    }*/


}
