package venture.cs414.android.monopoly2.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import venture.cs414.android.monopoly2.R;
import venture.cs414.android.monopoly2.backend.Board;
import venture.cs414.android.monopoly2.backend.GameFacade;


public class MainActivity extends AppCompatActivity {

    private GameFacade gameFacade;

    TextView timerText;

    TextView turnInfo;
    TextView currentPlayerInfo;
    TextView otherPlayerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*int numPlayers = getIntent().getIntExtra("numPlayers", 2);
        int numMinutes = getIntent().getIntExtra("numMinutes", 5);*/

        gameFacade = GameFacade.getInstance();

        turnInfo = (TextView)findViewById(R.id.CurrentTurnInfoField);
        currentPlayerInfo = (TextView)findViewById(R.id.CurrentPlayerInfoField);
        otherPlayerInfo = (TextView)findViewById(R.id.AllPlayerInfoField);
        updateAllInfo();
        turnInfo.setMovementMethod(new ScrollingMovementMethod());
        currentPlayerInfo.setMovementMethod(new ScrollingMovementMethod());
        otherPlayerInfo.setMovementMethod(new ScrollingMovementMethod());

        /*int numMiliSeconds = (numMinutes * 60 * 1000);
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
        }.start();*/
    }

    public void updateAllInfo(){
        turnInfo.setText(gameFacade.getCurrentMessage());
        currentPlayerInfo.setText(gameFacade.getCurrentPlayerInfo());
        otherPlayerInfo.setText(gameFacade.getOtherPlayerInfo());
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
                    gameFacade.moveCurrentPlayer();
                    updateAllInfo();
                    //Todo Implement property buying popup

                    Toast.makeText(getApplicationContext(), "Testing toast works", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error rolling", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_sell_property:
                try {
                    //implement call
                    Intent intent = new Intent(this, SellProperty.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_unmortgage:
                try {
                    //implement call
                    Intent intent = new Intent(this, UnmortgageProperty.class);
                    //Todo need to pass the serializable object when it is created
                    //intent.putExtra("difficulty", 1);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_end_turn:
                try {
                    //implement call
                    gameFacade.advanceTurn();
                    updateAllInfo();
                    //Toast.makeText(getApplicationContext(), "Testing to make sure call works", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_quit:
                try {
                    //implement call
                    String message = gameFacade.removeCurrentPlayerFromGame();
                    updateAllInfo();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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
