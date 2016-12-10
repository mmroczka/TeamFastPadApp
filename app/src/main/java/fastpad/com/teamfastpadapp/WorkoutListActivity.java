package fastpad.com.teamfastpadapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import fastpad.com.teamfastpadapp.adapters.WorkoutAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WorkoutListActivity extends ListActivity {
    private ListView listView;
    private TextView workoutLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workouts);
        getWorkoutsFromAPI();


    }

    private void getWorkoutsFromAPI() {
        //OkHTTP Connection

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            String root = "http://www.teamfastpad.xyz";
            String url = root + "/" + "api/Workouts";
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
            workoutList.add(new WorkoutListObject(1, "Workout A"));
            workoutList.add(new WorkoutListObject(2, "Workout B"));
            workoutList.add(new WorkoutListObject(3, "Workout C"));
        }
        else if(workoutList == null){
            workoutList = new ArrayList<WorkoutListObject>();
            workoutList.add(new WorkoutListObject(1, "Workout D"));
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
                WorkoutListObject w = (WorkoutListObject) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), w.name, Toast.LENGTH_SHORT).show();
                launchWorkoutActivity();
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

    private void launchWorkoutActivity(){
        Intent intent = new Intent(this, WorkoutActivity.class);
        startActivity(intent);
    }
}