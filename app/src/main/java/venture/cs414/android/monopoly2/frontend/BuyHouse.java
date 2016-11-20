package venture.cs414.android.monopoly2.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import venture.cs414.android.monopoly2.R;
import venture.cs414.android.monopoly2.backend.GameFacade;

public class BuyHouse extends AppCompatActivity {

    private GameFacade gameFacade;

    private Spinner propertySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_house);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameFacade = GameFacade.getInstance();

        propertySpinner = (Spinner)findViewById(R.id.SelectPropertyDropdownBuyHouse);
        ArrayAdapter<String> propertyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gameFacade.getStreetsCanBuyHouses());
        propertySpinner.setAdapter(propertyAdapter);

        Thread t = new Thread(){

            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(500);
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                setTitle(gameFacade.getTimerString());
                            }
                        });
                    }
                }catch(InterruptedException e){

                }
            }
        };

        t.start();
    }

    public void clickBuyButtonBuyHouse(View view){
        if(propertySpinner.getSelectedItem() != null){
            String propertyName = propertySpinner.getSelectedItem().toString();
            gameFacade.buyAHouse(propertyName);
            notify("You bought a house on " + propertyName);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void clickCancelButtonBuyHouse(View view){
        //Todo implement code
        try {
            //implement call
            Intent intent = new Intent(this, MainActivity.class);
            //Todo need to pass the serializable object when it is created
            //intent.putExtra("difficulty", 1);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void notify(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
