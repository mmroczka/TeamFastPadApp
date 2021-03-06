package fastpad.com.teamfastpadapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import fastpad.com.teamfastpadapp.AlertDialogFragment;
import fastpad.com.teamfastpadapp.R;
import fastpad.com.teamfastpadapp.objects.Drill;
import fastpad.com.teamfastpadapp.objects.DrillStatistic;
import fastpad.com.teamfastpadapp.objects.Workout;
import fastpad.com.teamfastpadapp.objects.WorkoutStatistic;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WorkoutSummaryActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public Button saveWorkoutBtn;
    public TextView summaryTextView;
    public Workout workout;
    public WorkoutStatistic workoutStat;
    public String workoutAsString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d("WORKOUT SUMMARY", "You made it to the end of the workout");

        Intent intent = getIntent();
        // get parcelable workout and drills from that workout

        workoutStat = intent.getParcelableExtra(WorkoutActivity.WORKOUTSTATISTIC);
        workout = intent.getParcelableExtra(WorkoutActivity.WORKOUT);

        saveWorkoutBtn = (Button) findViewById(R.id.btnSaveWorkout);
        summaryTextView = (TextView) findViewById(R.id.textViewSummary);

        String summaryString = createWorkoutSummaryString();
        summaryTextView.setText(summaryString);
        workoutAsString = convertWorkoutToGSONToString(workoutStat);
//        postWorkoutToAPI(workoutToString);
        saveWorkoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new AttemptPostRequest(workoutAsString).execute();
            }
        });
    }

    private String createWorkoutSummaryString() {
        ArrayList<Drill> drills = workout.getDrills();
        ArrayList<DrillStatistic> drillStat = workoutStat.getDrillStatistics();
        Drill tempDrill;
        DrillStatistic tempDrillStatistic;
        String summaryString = "";
        for(int i = 0; i < drills.size(); i++){
            tempDrill = drills.get(i);
            tempDrillStatistic = drillStat.get(i);
            String formattedString = padString(tempDrill.getName(), 35) + "Reps: " + tempDrillStatistic.getCompletedRepetitions() + "\n\n";
            summaryString += formattedString;
        }
        return summaryString;
    }

    public class AttemptPostRequest extends AsyncTask<String, Void, String> {
        public String jsonObjectAsString;
        public AttemptPostRequest(String s){
            this.jsonObjectAsString = s;
        }
        @Override
        protected String doInBackground(String... params) {
            try{
                Log.d("DoInBackground", "Made okHttpclient");
                // 1. Create OkHttp Client object
                OkHttpClient client = new OkHttpClient();

                // 2. Define request being sent to the server
                RequestBody postData = RequestBody.create(JSON, jsonObjectAsString);


                // ** NOTE ** we use the playerId NOT the player number!
                Request request = new Request.Builder()
                        .url("http://www.teamfastpad.xyz.com/api/Players/4/AddWorkoutStatistic")
                        .post(postData)
                        .build();

                Log.d("DoInBackground", "Attempting to execute request");
                // 3. Transport the request and wait for response to process next
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                return result;
            } catch (IOException e) {
                alertUserAboutError();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Log.d("onPostExecute", "Received data: " + s);
        }
    }

    private String convertWorkoutToGSONToString(WorkoutStatistic workout) {
        Gson workoutToJSON = new Gson();
        String gSonObjectAsString = workoutToJSON.toJson(workout);

        Log.d("JSON TESTING", gSonObjectAsString);

        return gSonObjectAsString;
    }

    private void postWorkoutToAPI(String jsonObjectAsString) {
        //OkHTTP Connection
        int playerId = 7;
        String url = "http://www.teamfastpad.xyz.com/api/Players/" + playerId + "/AddWorkoutStatistic";

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, jsonObjectAsString);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Call failed", "Invalid API call! Error: " + e.getMessage(), e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseData = response.body().string();
                    if (response.isSuccessful()) {
                        Log.d("HTTP Success!", "200! Contents: " + responseData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WorkoutSummaryActivity.this, "Successful post!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        alertUserAboutError();
                        Log.e("HTTP Error!", "m mError! Contents: " + responseData);
                    }
                }
            });
        } else{
            Toast.makeText(getApplicationContext(), "Network unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    public static String padString(String str, int leng) {
        while (str.length() < leng){
            str += " ";
        }
        return str;
    }
}
