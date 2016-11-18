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

import venture.cs414.android.monopoly2.R;
import venture.cs414.android.monopoly2.backend.GameFacade;

public class PlayerSelect extends AppCompatActivity {

    private Spinner numPlayersDrop;
    private Spinner numMinutesDrop;
    private GameFacade gameFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numPlayersDrop = (Spinner)findViewById(R.id.spinnerNumPlayers);
        Integer dropDownOptions[] = new Integer[]{2, 3, 4};
        ArrayAdapter<Integer> numPlayersAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, dropDownOptions);
        numPlayersDrop.setAdapter(numPlayersAdapter);

        numMinutesDrop = (Spinner)findViewById(R.id.spinnerTime);
        Integer dropDownOptionsTime[] = new Integer[]{1, 5, 10, 20};
        ArrayAdapter<Integer> numMinutesAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, dropDownOptionsTime);
        numMinutesDrop.setAdapter(numMinutesAdapter);

        gameFacade = GameFacade.getInstance();
    }

    public void clickStartGame(View view){
        int numPlayers = (int)numPlayersDrop.getSelectedItem();
        int numMinutes = (int)numMinutesDrop.getSelectedItem();
        Intent intent = new Intent(this, MainActivity.class);
        /*intent.putExtra("numPlayers", numPlayers);
        intent.putExtra("numMinutes", numMinutes);*/
        gameFacade.setUp(numPlayers, numMinutes, PlayerSelect.this);
        startActivity(intent);
        finish();
    }

}
