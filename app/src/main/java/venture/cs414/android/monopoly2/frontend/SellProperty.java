package venture.cs414.android.monopoly2.frontend;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import venture.cs414.android.monopoly2.R;
import venture.cs414.android.monopoly2.backend.GameFacade;

public class SellProperty extends AppCompatActivity {

    private GameFacade gameFacade;

    private PopupWindow popup;
    private PopupWindow blocker;
    private RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_property);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout = (RelativeLayout) findViewById(R.id.content);

        gameFacade = GameFacade.getInstance();
    }

    public void notify(String message){
        LinearLayout popLayout = new LinearLayout(this);
        TextView notificationText =new TextView(this);

        popup = new PopupWindow(this);
        popLayout.setOrientation(LinearLayout.VERTICAL);
        notificationText.setText(message);
        Button yesButton = new Button(this);
        yesButton.setText("YES");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo this is where the game facade will take care of the sale, and then pass back to main screen.
                blocker.dismiss();
                popup.dismiss();
            }
        });
        Button noButton = new Button(this);
        noButton.setText("NO");
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo this is where the game facade will take care of the sale, and then pass back to main screen.
                blocker.dismiss();
                popup.dismiss();
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
        notify("Testing if its working");
    }

    public void clickCancelButtonSellProperty(View view){
        //Todo implement code, should pass object back to main and reininitialize that view
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

}
