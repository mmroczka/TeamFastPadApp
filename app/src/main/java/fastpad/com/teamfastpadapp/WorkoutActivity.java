package fastpad.com.teamfastpadapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class WorkoutActivity extends AppCompatActivity {
    private Workout workout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        Intent intent = getIntent();
        this.workout = intent.getParcelableExtra(WorkoutListActivity.WORKOUT);
        toast(workout.getDescription());
    }
    private void toast(String toast){
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
