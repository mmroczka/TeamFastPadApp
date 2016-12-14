package fastpad.com.teamfastpadapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import fastpad.com.teamfastpadapp.AlertDialogFragment;
import fastpad.com.teamfastpadapp.R;
import fastpad.com.teamfastpadapp.objects.DrillStatistic;
import fastpad.com.teamfastpadapp.objects.WorkoutStatistic;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WorkoutSummaryActivity extends AppCompatActivity {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d("WORKOUT SUMMARY", "You made it to the workout");

        Intent intent = getIntent();
        // get parcelable workout and drills from that workout
        WorkoutStatistic workoutStat = intent.getParcelableExtra(WorkoutActivity.WORKOUTSTATISTIC);
        String workoutToString = convertWorkoutToGSONToString(workoutStat);

    }

    private String convertWorkoutToGSONToString(WorkoutStatistic workout) {
        Gson workoutToJSON = new Gson();
        String gSonObjectAsString = workoutToJSON.toJson(workout);

        Log.d("JSON TESTING", gSonObjectAsString);

        return null;
    }

    private void postWorkoutToAPI() {
        //OkHTTP Connection
        int playerId = 7;
        String url = "http://www.teamfastpad.xyz.com/api/Players/" + playerId + "/AddWorkoutStatistic";
        JSONObject jsonObject = new JSONObject();
        String jsonObjectAsString = jsonObject.toString();

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
}
