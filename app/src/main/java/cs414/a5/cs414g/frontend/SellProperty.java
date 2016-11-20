package cs414.a5.cs414g.frontend;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cs414.a5.cs414g.backend.GameFacade;
import venture.cs414.android.cs414g.R;

public class SellProperty extends AppCompatActivity {

    private GameFacade gameFacade;

    private PopupWindow popup;
    private PopupWindow blocker;
    private RelativeLayout layout;

    private Spinner propertySpinner;
    private Spinner playerSpinner;
    private EditText textAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_property);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout = (RelativeLayout) findViewById(R.id.content);

        gameFacade = GameFacade.getInstance();

        propertySpinner = (Spinner)findViewById(R.id.SelectPropertyDropDownSellProperty);
        ArrayAdapter<String> propertyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gameFacade.getMortgagableProperties());
        propertySpinner.setAdapter(propertyAdapter);

        playerSpinner = (Spinner)findViewById(R.id.SelectPlayerDropDownSellProperty);
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gameFacade.getOtherPlayerNames());
        playerSpinner.setAdapter(playerAdapter);

        textAmount = (EditText)findViewById(R.id.amountText);
        textAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        textAmount.setText("0");
    }

    public void notify(final String propertyName, final String playerName, final int cost){
        LinearLayout popLayout = new LinearLayout(this);
        TextView notificationText =new TextView(this);

        popup = new PopupWindow(this);
        popLayout.setOrientation(LinearLayout.VERTICAL);
        notificationText.setText(playerName + ", would you like to buy " + propertyName + " for " + cost + " Rupees?");
        Button yesButton = new Button(this);
        yesButton.setText("YES");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blocker.dismiss();
                popup.dismiss();

                gameFacade.sellAProperty(propertyName, playerName, cost);
                Intent intent = new Intent(SellProperty.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button noButton = new Button(this);
        noButton.setText("NO");
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blocker.dismiss();
                popup.dismiss();
                Intent intent = new Intent(SellProperty.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        popLayout.addView(notificationText);
        popLayout.setBackgroundColor(Color.WHITE);
        popLayout.addView(yesButton);
        popLayout.addView(noButton);
        popup.setContentView(popLayout);

        placeBlocker();
        popup.showAtLocation(layout, Gravity.CENTER, 10, 10);

    }

    public void placeBlocker() {
        RelativeLayout blockerLayout = new RelativeLayout(this);
        blockerLayout.setVisibility(View.INVISIBLE);
        blocker = new PopupWindow(blockerLayout, ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        blocker.showAtLocation(layout, Gravity.CENTER, 10, 10);
    }

    public void clickSellButtonSellProperty(View view){
        //Todo you must get the 3 fields that the user chose. These 3 fields need to be flushed out. They are not hooked up right now.
        //notify("Testing if its working");

        if(propertySpinner.getSelectedItem() != null && playerSpinner.getSelectedItem() != null) {
            String propName = propertySpinner.getSelectedItem().toString();
            String playerName = playerSpinner.getSelectedItem().toString();
            int cost = Integer.parseInt(textAmount.getText().toString());
            if(gameFacade.playerIsAI(playerName)){
                gameFacade.sellAPropertyToAI(propName, playerName, cost);
                Intent intent = new Intent(SellProperty.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                notify(propName, playerName, cost);
            }
        }
    }

    public void clickCancelButtonSellProperty(View view){
        //Todo implement code, should pass object back to main and reininitialize that view
        try {
            //implement call
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

}
