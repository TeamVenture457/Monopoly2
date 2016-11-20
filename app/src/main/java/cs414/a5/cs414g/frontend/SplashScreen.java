package cs414.a5.cs414g.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import venture.cs414.android.cs414g.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void prepPlayers(View view){
        Intent intent = new Intent(this, PlayerSelect.class);
        startActivity(intent);
        finish();
    }

    public void quitGame(View view){
        finish();
    }

}
