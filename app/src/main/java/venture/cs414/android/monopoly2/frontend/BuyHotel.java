package venture.cs414.android.monopoly2.frontend;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import venture.cs414.android.monopoly2.R;

public class BuyHotel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_hotel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
    }

    public void clickBuyButtonBuyHotel(View view){
        notify("Testing if its working");
    }

    public void clickCancelButtonBuyHotel(View view){
        //Todo implement code
    }

    public void notify(String message){

    }

}
