package venture.cs414.android.monopoly2.frontend;

import android.app.ActionBar;
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

import venture.cs414.android.monopoly2.R;

public class SellProperty extends AppCompatActivity {

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

    public void clickSellButton(View view){
        notify("Testing if its working");
    }

}
