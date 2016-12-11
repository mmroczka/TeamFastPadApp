package fastpad.com.teamfastpadapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    // variables for catching parcelable workout from WorkoutListActivity
    private Workout workout;
    private Drill currentDrill;

    // variables for keeping track of current state of the workout
    private int displayedRepetitions = 0;
    public long displayedWorkSeconds = 30;
    public long displayedRestSeconds = 30;

    // all other buttons, textviews and other things in this activity
    private TextView drillName;
    private TextView drillNumber;
    private TextView workTimer;
    private TextView restTimer;
    private EditText numberOfRepetitions;
    private Button addRepetition;
    private Button subtractRepetition;
    private Button nextExercise;
    private Button prevExercise;
    private VideoView drillVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent intent = getIntent();
        // get parcelable workout and drills from that workout
        this.workout = intent.getParcelableExtra(WorkoutListActivity.WORKOUT);
        ArrayList<Drill> drills = this.workout.getDrills();
        currentDrill = drills.get(0);

        // set timer periods from workout object


        // set textviews and labels accordingly
        drillName = (TextView) findViewById(R.id.textViewDrillName);
        drillNumber = (TextView) findViewById(R.id.textViewDrillNumber);
        workTimer = (TextView) findViewById(R.id.textViewWorkTimer);
        restTimer = (TextView) findViewById(R.id.textViewRestTimer);
        numberOfRepetitions = (EditText) findViewById(R.id.editTextNumberOfRepetitions);
        addRepetition = (Button)findViewById(R.id.btnAddRepetition);
        subtractRepetition = (Button) findViewById(R.id.btnSubtractRepetition);
        nextExercise = (Button) findViewById(R.id.btnNextExercise);
        prevExercise = (Button) findViewById(R.id.btnPreviousExercise);
        drillVideo = (VideoView) findViewById(R.id.videoView);

        // set onclick listeners
        addRepetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfRepetitions.setText(++displayedRepetitions);
            }
        });

        subtractRepetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfRepetitions.setText(--displayedRepetitions);
            }
        });

        nextExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long millisecondsToRun = 30000; // run for 30 seconds
                long millisecondsInterval = 1000; // pause interval
                new CountDownTimer(millisecondsToRun, millisecondsInterval){
                    public void onTick(long millisecondsUntilFinished){
                        workTimer.setText("Work: " + millisecondsUntilFinished / 1000);
                        displayedWorkSeconds--;
                    }

                    public void onFinish(){
                        workTimer.setText("--");
                    }
                }.start();

                new CountDownTimer(millisecondsToRun, millisecondsInterval){
                    public void onTick(long millisecondsUntilFinished){
                        restTimer.setText("Rest: " + millisecondsUntilFinished / 1000);
                        displayedRestSeconds--;
                    }

                    public void onFinish(){
                        restTimer.setText("--");
                    }
                }.start();
            }
        });

        prevExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("You clicked previous exercise");
            }
        });

        toast(workout.getDescription());
    }


    private void toast(String toast){
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }

}
