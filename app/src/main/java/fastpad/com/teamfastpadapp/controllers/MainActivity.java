package fastpad.com.teamfastpadapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fastpad.com.teamfastpadapp.R;

public class MainActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String VideoOrGif = "VideoOrGif";
    public static final String IsAutoPlayOn = "IsMediaControlsOn";
    public static final String IsSkipOn = "IsSkipOn";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myWorkoutsButton = (Button) findViewById(R.id.btnMyWorkouts);
        myWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchWorkoutList();
            }
        });

        Button settingsButton = (Button) findViewById(R.id.btnSettings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSettings();
            }
        });

        Button logoutButton = (Button) findViewById(R.id.btnLogOut);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                boolean autoPlay = sharedPreferences.getBoolean(IsAutoPlayOn, false);
                Toast.makeText(MainActivity.this, String.valueOf(autoPlay), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchWorkoutList(){
        Intent intent = new Intent(this, WorkoutListActivity.class);
        startActivity(intent);
    }

    private void launchSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
