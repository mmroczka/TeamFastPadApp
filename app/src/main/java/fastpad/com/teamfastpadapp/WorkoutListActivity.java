package fastpad.com.teamfastpadapp;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WorkoutListActivity extends ListActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workouts);

        String[] workouts = {"FastPad - Beginner Workout", "FastPad - Intermediate Workout", "FastPad - Advanced Workout"};
        TextView tv = new TextView(getApplicationContext());
        tv.setText("My Workouts");

        listView = getListView();
        listView.addHeaderView(tv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(), android.R.layout.simple_list_item_1, workouts);
        this.setListAdapter(adapter);

//        getListView().setAdapter(adapter);

    }
}
