package fastpad.com.teamfastpadapp;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

public class MyWorkoutsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workouts);

        String[] workouts = {"FastPad - Beginner", "FastPad - Intermediate", "FastPad - Advanced"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, workouts);
        getListView().setAdapter(adapter);

    }
}
