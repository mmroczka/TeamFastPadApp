package fastpad.com.teamfastpadapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import fastpad.com.teamfastpadapp.R;
import fastpad.com.teamfastpadapp.objects.Drill;
import fastpad.com.teamfastpadapp.objects.DrillStatistic;
import fastpad.com.teamfastpadapp.objects.Workout;
import fastpad.com.teamfastpadapp.objects.WorkoutStatistic;

public class WorkoutActivity extends AppCompatActivity {
    public static final String WORKOUTSTATISTIC = "WORKOUTSTATISTIC";
    public static final String WORKOUT = "WORKOUT";
    // perferences variables
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String VideoOrGif = "VideoOrGif";
    public static final String IsMediaControlsOn = "IsMediaControlsOn";
    public static final String IsSkipOn = "IsSkipOn";
    public SharedPreferences sharedPreferences;
    public boolean isSkipToNextExerciseOn = true;
    public boolean isMediaControlsOn = true;
    public boolean isVideoChecked = true;

    // variables for catching parcelable workout from WorkoutListActivity
    private Workout workout;
    private Drill currentDrill;
    private ArrayList<Drill> drills = new ArrayList<>();
    public int drill_num_index = 0;
    public int numberOfWorkSeconds = 0;
    public int numberOfRestSeconds = 0;
    public String currentUrl;
    public String startDateTime = getCurrentDateTimeISOAsString();
    public String endDateTime;
    public ArrayList<DrillStatistic> drillStats = new ArrayList<>();
    private String domainName = "http://www.teamfastpad.xyz";

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
        workout = intent.getParcelableExtra(WorkoutListActivity.WORKOUT);
        drills = this.workout.getDrills();
        currentDrill = drills.get(drill_num_index);

        setPreferenceVariables();
        connectLabelsAndButtonsToVariables();
        updateDisplay();
        createOnclickListenersForUI();
    }

    public void createOnclickListenersForUI() {
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
                goToNextExercise();
            }
        });

        prevExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPrevExercise();
            }
        });
    }

    public void connectLabelsAndButtonsToVariables() {
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
        if(isMediaControlsOn) {
            drillVideo.setMediaController(new MediaController(this));
        }
    }

    public void setPreferenceVariables() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String savedSetting = sharedPreferences.getString(VideoOrGif, null);
        if (savedSetting != null || savedSetting.equalsIgnoreCase("gif")){
            isVideoChecked = false;
        }
        isSkipToNextExerciseOn = sharedPreferences.getBoolean(IsSkipOn, true);
        isMediaControlsOn = sharedPreferences.getBoolean(IsMediaControlsOn, false);
    }

    private void goToPrevExercise() {
        updateDrillStatistic();
        if(drill_num_index > 0){
            drill_num_index--;
            cancelTimers();
            updateDisplay();
        } else{
            Toast.makeText(this, "No previous drills!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToNextExercise() {
        updateDrillStatistic();
        if(drill_num_index+1 < drills.size()){
            drill_num_index++;
            cancelTimers();
            updateDisplay();
        }
        else{
            launchSummaryActivity();
        }
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
        // update the drill data
        updateDrillData();

        // update the display of the new drill
        drillNameLabel.setText(currentDrill.getName());
        drillNumberLabel.setText("Drill: " + (drill_num_index+1) + " of " + drills.size());
        setRepsInRepetitionsBox();

        drillVideo.setVideoPath(currentUrl);
        drillVideo.start();

        // figure out time to set timers to
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

        // actually create timers
        timerForWork = new CountDownTimer(1000*numberOfWorkSeconds+1000, 1000){
            public void onTick(long millisecondsUntilFinished){
                workTimerLabel.setText("Work: " + millisecondsUntilFinished / 1000);
            }
            public void onFinish(){
                workTimerLabel.setText("Work: --");
                if(timerForRest != null){
                    timerForRest.cancel();
                }
                drillVideo.pause();
                timerForRest = new CountDownTimer(1000*numberOfRestSeconds+1000, 1000) {
                    @Override
                    public void onTick(long millisecondsUntilFinished) {
                        restTimerLabel.setText("Rest: " + millisecondsUntilFinished / 1000);
                    }

                    @Override
                    public void onFinish() {
                        restTimerLabel.setText("Rest: --");
                        if(isSkipToNextExerciseOn){
                            goToNextExercise();
                        }
                    }
                }.start();
            }
        }.start();

    }

    private void setRepsInRepetitionsBox() {
        int savedRepetition = 0;
        try {
            // get the drill at the current drill index
            DrillStatistic preExistingDrill = drillStats.get(drill_num_index);
            // if the drill is not null then it already exist, so just replace that current drill...
            if (preExistingDrill != null) {
                savedRepetition = preExistingDrill.getCompletedRepetitions();
            }

        } catch ( IndexOutOfBoundsException e ) {
            // if we get an IndexOutOfBoundsException then that means the drill hasn't already been added so we just set the reps box to 0
            savedRepetition = 0;
        }
        numberOfRepsBox.setText(Integer.toString(savedRepetition));
    }

    public void updateDrillData(){
        currentDrill = drills.get(drill_num_index);
        numberOfWorkSeconds = 0;
        numberOfRestSeconds = 0;
        if(currentDrill.getDrillDuration() != 0){
            numberOfWorkSeconds = currentDrill.getDrillDuration();
        }
        if(currentDrill.getRestDuration() != 0){
            numberOfRestSeconds = currentDrill.getRestDuration();
        }
        currentUrl = domainName + currentDrill.getVideoUrl();
    }

    public void updateDrillStatistic(){
        // get the workoutelementid and repetitions so you can make a new drillstatistic...
        long workoutElementId = currentDrill.getWorkoutElementId();
        String repetitionsBox = numberOfRepsBox.getText().toString();
        int completedReps = 0;
        if(isInteger(repetitionsBox)) {
            completedReps = Integer.valueOf(repetitionsBox);
        }

        // make the new DrillStatistic...
        DrillStatistic stat = new DrillStatistic(workoutElementId, completedReps);

        try {
            // get the drill at the current drill index
            DrillStatistic drillExists = drillStats.get( drill_num_index );

            // if the drill is not null then it already exist, so just replace that current drill...
            if(drillExists != null){
                drillStats.set(drill_num_index, stat);
            }
        } catch ( IndexOutOfBoundsException e ) {
            // if we get an IndexOutOfBoundsException then that means the drill hasn't already been added, so we need to add it
            drillStats.add( stat );
        }
    }

    private void launchSummaryActivity(){
        WorkoutStatistic workoutStat = makeWorkoutStatistic();
        Intent intent = new Intent(this, WorkoutSummaryActivity.class);
        intent.putExtra(WORKOUTSTATISTIC, workoutStat);
        intent.putExtra(WORKOUT, workout);
        startActivity(intent);
    }

    public WorkoutStatistic makeWorkoutStatistic(){
        // record ending time for workoutstatistic
        endDateTime = getCurrentDateTimeISOAsString();
        WorkoutStatistic workoutStatistic = new WorkoutStatistic();
        workoutStatistic.setStartDateTime(startDateTime);
        workoutStatistic.setEndDateTime(endDateTime);
        workoutStatistic.setWorkoutId(workout.getId());
        Log.d("StartDateTime", startDateTime);
        Log.d("EndDateTime", endDateTime);
        workoutStatistic.setDrillStatistics(drillStats);
        return workoutStatistic;
    }

    public String getCurrentDateTimeISOAsString(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    public boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
