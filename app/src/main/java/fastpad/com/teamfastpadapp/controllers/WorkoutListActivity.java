package fastpad.com.teamfastpadapp.controllers;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import fastpad.com.teamfastpadapp.AlertDialogFragment;
import fastpad.com.teamfastpadapp.R;
import fastpad.com.teamfastpadapp.adapters.WorkoutAdapter;
import fastpad.com.teamfastpadapp.objects.Drill;
import fastpad.com.teamfastpadapp.objects.Workout;
import fastpad.com.teamfastpadapp.objects.WorkoutListObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WorkoutListActivity extends ListActivity {
    public static final String WORKOUT = "WORKOUT";
    private ListView listView;
    private TextView workoutLabel;
    private String domainName = "http://www.teamfastpad.xyz/";
    private String allWorkouts = "api/Workouts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts_list);
        getWorkoutsFromAPI();


    }

    private void getWorkoutsFromAPI() {
        //OkHTTP Connection

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            String url = domainName + allWorkouts;
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Call failed", "Invalid API call! Error: " + e.getMessage(), e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();

                    if (response.isSuccessful()) {
                        Log.d("HTTP Success!", "200! Contents: " + responseData);
                        final ArrayList<WorkoutListObject> workoutList = parseJSONForWorkoutList(responseData);
                        if(workoutList != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    displayList(workoutList);
                                }
                            });
                        }
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

    private void displayList(ArrayList<WorkoutListObject> workoutList) {
        if(workoutList.isEmpty()) {
            workoutList = new ArrayList<WorkoutListObject>();
            workoutList.add(new WorkoutListObject(1, "something wrong in displayList function"));
            workoutList.add(new WorkoutListObject(2, "Workout B"));
            workoutList.add(new WorkoutListObject(3, "Workout C"));
        }
        else if(workoutList == null){
            workoutList = new ArrayList<WorkoutListObject>();
            workoutList.add(new WorkoutListObject(1, "error in displayList function"));
            workoutList.add(new WorkoutListObject(2, "Workout E"));
            workoutList.add(new WorkoutListObject(3, "Workout F"));
        }
        TextView tv = new TextView(getApplicationContext());
        tv.setText("My Workouts");
        listView = getListView();
        listView.addHeaderView(tv);

//        Parcelable parcelables = getIntent().getParcelableArrayExtra(MainActivity.DAILY_FORCAST);
//        workoutList = Arrays.copyOf(parcelables, parcelables.length, WorkoutListObject[].class);

        // you can get rid of the listView variable and just pass in "this" if you don't want the header anymore
        final WorkoutAdapter adapter = new WorkoutAdapter(this, workoutList);
        setListAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                WorkoutListObject selectedWorkout = (WorkoutListObject) parent.getItemAtPosition(position);
                getWorkoutByIdFromAPI(selectedWorkout.id);
            }
        });
    }

    private ArrayList<WorkoutListObject> parseJSONForWorkoutList(String responseData) {
        // if the request JSON string isn't empty...
        ArrayList<WorkoutListObject> workoutList = null;
        if (responseData != null) {
            try {
                JSONArray workouts = new JSONArray(responseData);
                workoutList = new ArrayList<WorkoutListObject>();
                for (int i = 0; i < workouts.length(); i++) {
                    JSONObject w = workouts.getJSONObject(i);

                    // convert JSON data to Java variables
                    long id = Long.parseLong(w.getString("Id"));
                    String name = w.getString("Name");

                    // put java variables into new workout object and add workout object to the workoutlist
                    WorkoutListObject workout = new WorkoutListObject(id, name);
                    workoutList.add(workout);
                }
            } catch (final JSONException e) {
                Log.e("OnResponse error tag", "Json parsing error: " + e.getMessage());
            }
        }
        return workoutList;
    }

    private void getWorkoutByIdFromAPI(long workoutId){
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            String url = domainName + allWorkouts + Long.toString(workoutId);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Call failed", "Invalid API call! Error: " + e.getMessage(), e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();

                    if (response.isSuccessful()) {
                        Log.d("HTTP Success!", "200! Contents: " + responseData);
                        final Workout workout = parseJSONForWorkout(responseData);
                        if(workout != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(getApplicationContext(), workout.getDescription(), Toast.LENGTH_SHORT).show();
                                    launchWorkoutActivity(workout);
                                }
                            });
                        }
                    } else {
                        alertUserAboutError();
                        Log.e("HTTP Error!", "m mError! Contents: " + responseData);
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Network unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private Workout parseJSONForWorkout(String responseData) {
        // if the request JSON string isn't empty...
        Workout workout = null;
        ArrayList<Drill> drill = new ArrayList<Drill>();
        if (responseData != null) {
            try {
                workout = new Workout();
                JSONObject root = new JSONObject(responseData);
                // set workout attributes
                workout.setId(root.getLong("Id"));
                workout.setName(root.getString("Name"));
                workout.setDescription(root.getString("Description"));

                JSONArray workoutElements = root.getJSONArray("WorkoutElements");
                ArrayList<Drill> drills = new ArrayList<>();
                for(int i = 0; i < workoutElements.length(); i++){
                    JSONObject d = workoutElements.getJSONObject(i);
                    long drillId = d.getLong("DrillId");
                    long workoutElementId = d.getLong("Id");
                    int difficulty = d.getInt("Difficulty");
                    String name = d.getString("Name");
                    String videoUrl = d.getString("VideoUrl");
                    String gifUrl = d.getString("AnimationUrl");
                    String description = d.getString("Description");

                    Drill newDrill = new Drill(drillId, workoutElementId, difficulty, name, videoUrl, gifUrl, description);
                    drills.add(newDrill);
                }
                workout.setDrills(drills);
            } catch (final JSONException e) {
                Log.e("OnResponse error tag", "Json parsing error: " + e.getMessage());
            }
        }
        return workout;
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

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void launchWorkoutActivity(Workout workout){
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra(WORKOUT, workout);
        startActivity(intent);
    }
}