package venture.cs414.android.monopoly2.frontend;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import venture.cs414.android.monopoly2.R;
import venture.cs414.android.monopoly2.backend.GameFacade;


public class MainActivity extends AppCompatActivity {

    private GameFacade gameFacade;

    //TextView timerText;

    private TextView turnInfo;
    private TextView currentPlayerInfo;
    private TextView otherPlayerInfo;

    private PopupWindow notificationPopup;
    private PopupWindow blockerWindow;

    private RelativeLayout layout;
    private EditText costText;

    private int highestBid;
    private String highestBidder;
    private List<String> players;
    private int currentIndex;


    private List<Button> boardButtons;

    Handler timerHandler;
    Runnable timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*int numPlayers = getIntent().getIntExtra("numPlayers", 2);
        int numMinutes = getIntent().getIntExtra("numMinutes", 5);*/

        gameFacade = GameFacade.getInstance();

        layout = (RelativeLayout)findViewById(R.id.ContentMain);

        turnInfo = (TextView)findViewById(R.id.CurrentTurnInfoField);
        turnInfo.setMovementMethod(new ScrollingMovementMethod());
        currentPlayerInfo = (TextView)findViewById(R.id.CurrentPlayerInfoField);
        currentPlayerInfo.setMovementMethod(new ScrollingMovementMethod());
        otherPlayerInfo = (TextView)findViewById(R.id.AllPlayerInfoField);
        otherPlayerInfo.setMovementMethod(new ScrollingMovementMethod());


        Button button0 = (Button) findViewById(R.id.buttonspace0);
        Button button1 = (Button) findViewById(R.id.buttonspace1);
        Button button2 = (Button) findViewById(R.id.buttonspace2);
        Button button3 = (Button) findViewById(R.id.buttonspace3);
        Button button4 = (Button) findViewById(R.id.buttonspace4);
        Button button5 = (Button) findViewById(R.id.buttonspace5);
        Button button6 = (Button) findViewById(R.id.buttonspace6);
        Button button7 = (Button) findViewById(R.id.buttonspace7);
        Button button8 = (Button) findViewById(R.id.buttonspace8);
        Button button9 = (Button) findViewById(R.id.buttonspace9);
        Button button10 = (Button) findViewById(R.id.buttonspace10);
        Button button11 = (Button) findViewById(R.id.buttonspace11);
        Button button12 = (Button) findViewById(R.id.buttonspace12);
        Button button13 = (Button) findViewById(R.id.buttonspace13);
        Button button14 = (Button) findViewById(R.id.buttonspace14);
        Button button15 = (Button) findViewById(R.id.buttonspace15);
        Button button16 = (Button) findViewById(R.id.buttonspace16);
        Button button17 = (Button) findViewById(R.id.buttonspace17);
        Button button18 = (Button) findViewById(R.id.buttonspace18);
        Button button19 = (Button) findViewById(R.id.buttonspace19);
        Button button20 = (Button) findViewById(R.id.buttonspace20);
        Button button21 = (Button) findViewById(R.id.buttonspace21);
        Button button22 = (Button) findViewById(R.id.buttonspace22);
        Button button23 = (Button) findViewById(R.id.buttonspace23);
        Button button24 = (Button) findViewById(R.id.buttonspace24);
        Button button25 = (Button) findViewById(R.id.buttonspace25);
        Button button26 = (Button) findViewById(R.id.buttonspace26);
        Button button27 = (Button) findViewById(R.id.buttonspace27);
        Button button28 = (Button) findViewById(R.id.buttonspace28);
        Button button29 = (Button) findViewById(R.id.buttonspace29);
        Button button30 = (Button) findViewById(R.id.buttonspace30);
        Button button31 = (Button) findViewById(R.id.buttonspace31);
        Button button32 = (Button) findViewById(R.id.buttonspace32);
        Button button33 = (Button) findViewById(R.id.buttonspace33);
        Button button34 = (Button) findViewById(R.id.buttonspace34);
        Button button35 = (Button) findViewById(R.id.buttonspace35);
        Button button36 = (Button) findViewById(R.id.buttonspace36);
        Button button37 = (Button) findViewById(R.id.buttonspace37);
        Button button38 = (Button) findViewById(R.id.buttonspace38);
        Button button39 = (Button) findViewById(R.id.buttonspace39);



        boardButtons = new ArrayList<>();
        boardButtons.add(button0);
        boardButtons.add(button1);
        boardButtons.add(button2);
        boardButtons.add(button3);
        boardButtons.add(button4);
        boardButtons.add(button5);
        boardButtons.add(button6);
        boardButtons.add(button7);
        boardButtons.add(button8);
        boardButtons.add(button9);
        boardButtons.add(button10);
        boardButtons.add(button11);
        boardButtons.add(button12);
        boardButtons.add(button13);
        boardButtons.add(button14);
        boardButtons.add(button15);
        boardButtons.add(button16);
        boardButtons.add(button17);
        boardButtons.add(button18);
        boardButtons.add(button19);
        boardButtons.add(button20);
        boardButtons.add(button21);
        boardButtons.add(button22);
        boardButtons.add(button23);
        boardButtons.add(button24);
        boardButtons.add(button25);
        boardButtons.add(button26);
        boardButtons.add(button27);
        boardButtons.add(button28);
        boardButtons.add(button29);
        boardButtons.add(button30);
        boardButtons.add(button31);
        boardButtons.add(button32);
        boardButtons.add(button33);
        boardButtons.add(button34);
        boardButtons.add(button35);
        boardButtons.add(button36);
        boardButtons.add(button37);
        boardButtons.add(button38);
        boardButtons.add(button39);

        updateAllInfo();

       /* timerHandler = new Handler();
        timerRunnable = new Runnable(){
            @Override
            public void run(){
                MainActivity.this.setTitle(gameFacade.getTimerString());
                timerHandler.postDelayed(this, 500);
                if(gameFacade.gameIsOver()){
                    timerHandler.removeCallbacks(this);
                    //timerHandler.postDelayed(this, 0);
                    endGame();
                }
            }
        };

        timerRunnable.run();*/

        /*new CountDownTimer(numMiliSeconds, 1000){

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
        turnInfo.scrollTo(0,0);
        currentPlayerInfo.setText(gameFacade.getCurrentPlayerInfo());
        currentPlayerInfo.scrollTo(0,0);
        otherPlayerInfo.setText(gameFacade.getOtherPlayerInfo());
        otherPlayerInfo.scrollTo(0,0);
        for(Button button: boardButtons){
            button.setText(gameFacade.getSpaceInfo(boardButtons.indexOf(button)));
        }


        if(gameFacade.gameIsOver()){
            endGame();
        }
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
                    if(gameFacade.currentPlayerInJail()){
                        displayJailWindow();
                    }else {
                        gameFacade.moveCurrentPlayer();
                    }
                    updateAllInfo();

                    checkBuyProperty();

                    //Toast.makeText(getApplicationContext(), "Testing toast works", Toast.LENGTH_LONG).show();

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

    public void clickSpace0(View view){
        getSpaceInfoForToast(0);
    }
    public void clickSpace1(View view){
        getSpaceInfoForToast(1);
    }
    public void clickSpace2(View view){
        getSpaceInfoForToast(2);
    }
    public void clickSpace3(View view){
        getSpaceInfoForToast(3);
    }
    public void clickSpace4(View view){
        getSpaceInfoForToast(4);
    }
    public void clickSpace5(View view){
        getSpaceInfoForToast(5);
    }
    public void clickSpace6(View view){
        getSpaceInfoForToast(6);
    }
    public void clickSpace7(View view){
        getSpaceInfoForToast(7);
    }
    public void clickSpace8(View view){
        getSpaceInfoForToast(8);
    }
    public void clickSpace9(View view){
        getSpaceInfoForToast(9);
    }
    public void clickSpace10(View view){
        getSpaceInfoForToast(10);
    }
    public void clickSpace11(View view){
        getSpaceInfoForToast(11);
    }
    public void clickSpace12(View view){
        getSpaceInfoForToast(12);
    }
    public void clickSpace13(View view){
        getSpaceInfoForToast(13);
    }
    public void clickSpace14(View view){
        getSpaceInfoForToast(14);
    }
    public void clickSpace15(View view){
        getSpaceInfoForToast(15);
    }
    public void clickSpace16(View view){
        getSpaceInfoForToast(16);
    }
    public void clickSpace17(View view){
        getSpaceInfoForToast(17);
    }
    public void clickSpace18(View view){
        getSpaceInfoForToast(18);
    }
    public void clickSpace19(View view){
        getSpaceInfoForToast(19);
    }
    public void clickSpace20(View view){
        getSpaceInfoForToast(20);
    }
    public void clickSpace21(View view){
        getSpaceInfoForToast(21);
    }
    public void clickSpace22(View view){
        getSpaceInfoForToast(22);
    }
    public void clickSpace23(View view){
        getSpaceInfoForToast(23);
    }
    public void clickSpace24(View view){
        getSpaceInfoForToast(24);
    }
    public void clickSpace25(View view){
        getSpaceInfoForToast(25);
    }
    public void clickSpace26(View view){
        getSpaceInfoForToast(26);
    }
    public void clickSpace27(View view){
        getSpaceInfoForToast(27);
    }
    public void clickSpace28(View view){
        getSpaceInfoForToast(28);
    }
    public void clickSpace29(View view){
        getSpaceInfoForToast(29);
    }
    public void clickSpace30(View view){
        getSpaceInfoForToast(30);
    }
    public void clickSpace31(View view){
        getSpaceInfoForToast(31);
    }
    public void clickSpace32(View view){
        getSpaceInfoForToast(32);
    }
    public void clickSpace33(View view){
        getSpaceInfoForToast(33);
    }
    public void clickSpace34(View view){
        getSpaceInfoForToast(34);
    }
    public void clickSpace35(View view){
        getSpaceInfoForToast(35);
    }
    public void clickSpace36(View view){
        getSpaceInfoForToast(36);
    }
    public void clickSpace37(View view){
        getSpaceInfoForToast(37);
    }
    public void clickSpace38(View view){
        getSpaceInfoForToast(38);
    }
    public void clickSpace39(View view){
        getSpaceInfoForToast(39);
    }

    public void getSpaceInfoForToast(int spaceNumber){
        try {
            //implement call
            String message = gameFacade.getClickedSpaceInfo(spaceNumber);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }


    /*public void sellProperty(View view){
        Intent intent = new Intent(this, SellProperty.class);
        //intent.putExtra("difficulty", 1);
        startActivity(intent);
        finish();
    }*/

    public void checkBuyProperty(){
        String propBuy = gameFacade.getCurrentPropertySale();
        //turnInfo.setText(propBuy);
        if(propBuy != null){
            //Create layouts and views for popup window
            LinearLayout popLayout = new LinearLayout(this);
            TextView salesText = new TextView(this);
            notificationPopup = new PopupWindow(this);

            //Set layout orientation
            popLayout.setOrientation(LinearLayout.VERTICAL);

            //Set the text for the popup
            salesText.setText(propBuy);

            //Create Button to dismiss
            Button but = new Button(this);
            but.setText("Yes");
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blockerWindow.dismiss();
                    notificationPopup.dismiss();
                    gameFacade.currentPlayerBuyCurrentProperty();
                    updateAllInfo();
                }
            });

            Button but2 = new Button(this);
            but2.setText("No");
            but2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blockerWindow.dismiss();
                    notificationPopup.dismiss();
                    startAuction();
                    updateAllInfo();
                }
            });

            //Place layout in the popup
            popLayout.addView(salesText);
            popLayout.setBackgroundColor(Color.WHITE);
            popLayout.addView(but);
            popLayout.addView(but2);
            notificationPopup.setContentView(popLayout);

            placeBlocker();
            notificationPopup.showAtLocation(layout, Gravity.CENTER, 10, 10);
        }
    }

    public void startAuction(){
        //Create layouts and views for popup window
        LinearLayout popLayout = new LinearLayout(this);
        final TextView notificationText = new TextView(this);
        notificationPopup = new PopupWindow(this);
        costText = new EditText(this);
        costText.setInputType(InputType.TYPE_CLASS_NUMBER);

        //Set layout orientation
        popLayout.setOrientation(LinearLayout.VERTICAL);

        //Set the text for the popup
        players = gameFacade.getPlayerNames();
        highestBid = 0;
        highestBidder = null;
        currentIndex = 0;
        notificationText.setText(players.get(0) + ": Place a bid for: " + gameFacade.getCurrentPropertyName() + "\n"
        + "Current Highest Bid: " + highestBid + " Rupees");

        //Place layout in the popup
        popLayout.addView(notificationText);
        costText.setText("0");
        popLayout.addView(costText);
        popLayout.setBackgroundColor(Color.WHITE);

        Button bidButton = new Button(this);
        bidButton.setText("Bid");
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*blockerWindow.dismiss();
                notificationPopup.dismiss();
                highestBid = Integer.parseInt(costText.getText().toString());*/
                String bidString = costText.getText().toString();
                if(bidString.length() > 5){
                    notificationText.setText(players.get(currentIndex) + ": Place a bid for: " + gameFacade.getCurrentPropertyName() + "\n"
                            + "Current Highest Bid: " + highestBid + " Rupees\n"
                            + "You cannot afford that bid");
                }else {
                    int bid = Integer.parseInt(bidString);
                    if (gameFacade.playerCanAfford(players.get(currentIndex), bid)) {
                        if (bid > highestBid) {
                            highestBid = bid;
                            highestBidder = players.get(currentIndex);
                            currentIndex++;
                            if (currentIndex >= players.size()) {
                                currentIndex = 0;
                            }
                            notificationText.setText(players.get(currentIndex) + ": Place a bid for: " + gameFacade.getCurrentPropertyName() + "\n"
                                    + "Current Highest Bid: " + highestBid + " Rupees");

                            if(players.size() == 1){
                                try {
                                    if(highestBidder != null) {
                                        gameFacade.playerBuyProperty(highestBidder, gameFacade.getCurrentPropertyName(), highestBid);
                                        blockerWindow.dismiss();
                                        notificationPopup.dismiss();
                                        updateAllInfo();
                                        Toast.makeText(getApplicationContext(), highestBidder + " won the auction!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                notificationText.setText(players.get(currentIndex) + ": Place a bid for: " + gameFacade.getCurrentPropertyName() + "\n"
                                        + "Current Highest Bid: " + highestBid + " Rupees");
                            }
                        }
                    } else {
                        notificationText.setText(players.get(currentIndex) + ": Place a bid for: " + gameFacade.getCurrentPropertyName() + "\n"
                                + "Current Highest Bid: " + highestBid + " Rupees\n"
                                + "You cannot afford that bid");
                    }
                }
            }
        });
        popLayout.addView(bidButton);

        Button dontBidButton = new Button(this);
        dontBidButton.setText("Don't Bid");
        dontBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                players.remove(currentIndex);
                if(currentIndex >= players.size()){
                    currentIndex = 0;
                }
                if(players.size() == 1){
                    try {
                        if(highestBidder != null) {
                            gameFacade.playerBuyProperty(highestBidder, gameFacade.getCurrentPropertyName(), highestBid);
                            blockerWindow.dismiss();
                            notificationPopup.dismiss();
                            updateAllInfo();
                            Toast.makeText(getApplicationContext(), highestBidder + " won the auction!", Toast.LENGTH_LONG).show();
                        }else{
                            currentIndex++;
                            if (currentIndex >= players.size()) {
                                currentIndex = 0;
                            }
                            notificationText.setText(players.get(currentIndex) + ": Place a bid for: " + gameFacade.getCurrentPropertyName() + "\n"
                                    + "Current Highest Bid: " + highestBid + " Rupees");

                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }else {
                    if(players.size() == 0){
                        try {
                            blockerWindow.dismiss();
                            notificationPopup.dismiss();
                            updateAllInfo();
                            Toast.makeText(getApplicationContext(), "The property went back to the bank", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        notificationText.setText(players.get(currentIndex) + ": Place a bid for: " + gameFacade.getCurrentPropertyName() + "\n"
                                + "Current Highest Bid: " + highestBid + " Rupees");
                    }
                }
            }
        });
        popLayout.addView(dontBidButton);

        notificationPopup.setContentView(popLayout);
        notificationPopup.setFocusable(true);

        placeBlocker();
        notificationPopup.showAtLocation(layout, Gravity.CENTER, 10, 10);
    }

    public void displayJailWindow(){
        //Create layouts and views for popup window
        LinearLayout popLayout = new LinearLayout(this);
        TextView text = new TextView(this);
        notificationPopup = new PopupWindow(this);

        //Set layout orientation
        popLayout.setOrientation(LinearLayout.VERTICAL);

        //Set the text for the popup
        text.setText("You are in jail!  How would you like to get out?");

        //Create Button to dismiss
        Button but = new Button(this);
        but.setText("Roll");
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameFacade.rollForJail();
                blockerWindow.dismiss();
                notificationPopup.dismiss();
                updateAllInfo();
            }
        });

        Button but2 = new Button(this);
        but2.setText("Pay 50 Rupees");
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameFacade.payJail();
                blockerWindow.dismiss();
                notificationPopup.dismiss();
                updateAllInfo();
            }
        });

        Button but3 = new Button(this);
        but3.setText("Use 'Get out of jail free' card");
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockerWindow.dismiss();
                notificationPopup.dismiss();
                gameFacade.useJailCard();
                updateAllInfo();
            }
        });

        //Place layout in the popup
        popLayout.addView(text);
        popLayout.setBackgroundColor(Color.WHITE);
        popLayout.addView(but);
        popLayout.addView(but2);
        popLayout.addView(but3);
        notificationPopup.setContentView(popLayout);

        placeBlocker();
        notificationPopup.showAtLocation(layout, Gravity.CENTER, 10, 10);
    }

    public void endGame(){
        if(notificationPopup != null) {
            notificationPopup.dismiss();
            ;
        }
        if(blockerWindow != null) {
            blockerWindow.dismiss();
        }
        String endGameText = gameFacade.endGame();
        //game controller return winner
        //Create layouts and views for popup window
        LinearLayout popLayout = new LinearLayout(this);
        TextView notificationText = new TextView(this);
        ImageView lossImage = new ImageView(this);
        notificationPopup = new PopupWindow(this);

        //Set layout orientation
        popLayout.setOrientation(LinearLayout.VERTICAL);

        //Set the text for the popup
        notificationText.setText(endGameText);

        //Create Button to dismiss
        Button but = new Button(this);
        but.setText("Dismiss");
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockerWindow.dismiss();
                notificationPopup.dismiss();
                Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                startActivity(intent);
                finish();
            }
        });

        //Place layout in the popup
        popLayout.addView(notificationText);
        popLayout.setBackgroundColor(Color.WHITE);
        popLayout.addView(but);
        notificationPopup.setContentView(popLayout);

        placeBlocker();
        notificationPopup.showAtLocation(layout, Gravity.CENTER, 10, 10);
    }

    public void placeBlocker(){
        RelativeLayout blocker = new RelativeLayout(this);
        blocker.setVisibility(View.INVISIBLE);
        blockerWindow = new PopupWindow(blocker, ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        blockerWindow.showAtLocation(layout, Gravity.CENTER, 10, 10);
    }
}
