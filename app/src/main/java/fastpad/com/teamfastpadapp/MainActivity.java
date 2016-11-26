package fastpad.com.teamfastpadapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mMyWorkoutsButton = (Button) findViewById(R.id.btnMyWorkouts);
        mMyWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workoutList();
            }
        });
    }
    private void workoutList(){
        Intent intent = new Intent(this, WorkoutListActivity.class);
        startActivity(intent);
    }



}
