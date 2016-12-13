package fastpad.com.teamfastpadapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

import fastpad.com.teamfastpadapp.R;
import fastpad.com.teamfastpadapp.objects.Drill;
import fastpad.com.teamfastpadapp.objects.Workout;

public class WorkoutActivity extends AppCompatActivity {

    // variables for catching parcelable workout from WorkoutListActivity
    private Workout workout;
    private Drill currentDrill;
    private ArrayList<Drill> drills = new ArrayList<>();
    public int drill_num_index = 0;
    public int numberOfWorkSeconds = 0;
    public int numberOfRestSeconds = 0;

    // variables for keeping track of current state of the workout
    public CountDownTimer timerForWork = null;
    public CountDownTimer timerForRest = null;

    // all other buttons, textviews and other things in this activity
    private TextView drillNameLabel;
    private TextView drillNumberLabel;
    private TextView workTimerLabel;
    private TextView restTimerLabel;
    private EditText numberOfRepsBox;
    private Button addRepetition;
    private Button subtractRepetition;
    private Button nextExercise;
    private Button prevExercise;
    private VideoView drillVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_videos);

        Intent intent = getIntent();
        // get parcelable workout and drills from that workout
        this.workout = intent.getParcelableExtra(WorkoutListActivity.WORKOUT);
        drills = this.workout.getDrills();
        currentDrill = drills.get(drill_num_index);

        // set timer periods from workout object


        // set textviews and labels accordingly
        drillNameLabel = (TextView) findViewById(R.id.textViewDrillName);
        drillNumberLabel = (TextView) findViewById(R.id.textViewDrillNumber);
        workTimerLabel = (TextView) findViewById(R.id.textViewWorkTimer);
        restTimerLabel = (TextView) findViewById(R.id.textViewRestTimer);
        numberOfRepsBox = (EditText) findViewById(R.id.editTextNumberOfRepetitions);
        numberOfRepsBox.setText("0");
        addRepetition = (Button) findViewById(R.id.btnAddRepetition);
        subtractRepetition = (Button) findViewById(R.id.btnSubtractRepetition);
        nextExercise = (Button) findViewById(R.id.btnNextExercise);
        prevExercise = (Button) findViewById(R.id.btnPreviousExercise);
        drillVideo = (VideoView) findViewById(R.id.videoView);
        updateDisplay();

        // set onclick listeners
        addRepetition.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String repetitionsBox = numberOfRepsBox.getText().toString();
                int currentReps = 0;
                if(isInteger(repetitionsBox)) {
                    currentReps = Integer.valueOf(repetitionsBox);
                }
                if(currentReps >= 10000){
                    numberOfRepsBox.setText(Integer.toString(10000));
                }
                else{
                    numberOfRepsBox.setText(Integer.toString(++currentReps));
                }
            }
        });

        subtractRepetition.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String repetitionsBox = numberOfRepsBox.getText().toString();
                int currentReps = 0;
                if(isInteger(repetitionsBox)) {
                    currentReps = Integer.valueOf(repetitionsBox);
                }
                if(currentReps <= 0){
                    numberOfRepsBox.setText(Integer.toString(0));
                }
                else{
                    numberOfRepsBox.setText(Integer.toString(--currentReps));
                }
            }
        });

        nextExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPrevExercise();
            }
        });

        prevExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextExercise();
            }
        });


    }

    private void goToPrevExercise() {
        if(drill_num_index+1 < drills.size()){
            drill_num_index++;
        }
        cancelTimers();
        updateDisplay();
    }

    private void goToNextExercise() {
        if(drill_num_index > 0){
            drill_num_index--;
        }
        cancelTimers();
        updateDisplay();
    }

    private void cancelTimers() {
        if(timerForWork != null) {
            timerForWork.cancel();
        }
        if(timerForRest != null) {
            timerForRest.cancel();
        }
    }

    private void updateDisplay() {
        updateDrill();
        drillNameLabel.setText(currentDrill.getName());
        drillNumberLabel.setText("Drill: " + (drill_num_index+1) + " of " + drills.size());
        if(currentDrill.getDrillDuration() != 0){
            numberOfWorkSeconds = currentDrill.getDrillDuration();
        }
        if(currentDrill.getRestDuration() != 0){
            numberOfRestSeconds = currentDrill.getRestDuration();
        }
        restTimerLabel.setText("Rest: " + numberOfRestSeconds);

        if(timerForWork != null){
            timerForWork.cancel();
        }

        timerForWork = new CountDownTimer(1000*numberOfWorkSeconds+1000, 1000){
            public void onTick(long millisecondsUntilFinished){
                workTimerLabel.setText("Work: " + millisecondsUntilFinished / 1000);
            }
            public void onFinish(){
                workTimerLabel.setText("Work: --");
                if(timerForRest != null){
                    timerForRest.cancel();
                }
                timerForRest = new CountDownTimer(1000*numberOfRestSeconds+1000, 1000) {
                    @Override
                    public void onTick(long millisecondsUntilFinished) {
                        restTimerLabel.setText("Rest: " + millisecondsUntilFinished / 1000);
                    }

                    @Override
                    public void onFinish() {
                        restTimerLabel.setText("Rest: --");

                    }
                }.start();
            }
        }.start();

    }


    private void toast(String toast){
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }

    public boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void updateDrill(){
        currentDrill = drills.get(drill_num_index);
        numberOfWorkSeconds = 0;
        numberOfRestSeconds = 0;
        if(currentDrill.getDrillDuration() != 0){
            numberOfWorkSeconds = currentDrill.getDrillDuration();
        }
        if(currentDrill.getRestDuration() != 0){
            numberOfRestSeconds = currentDrill.getRestDuration();
        }
    }
}
