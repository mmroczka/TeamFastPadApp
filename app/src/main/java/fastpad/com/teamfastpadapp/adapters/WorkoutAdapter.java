package fastpad.com.teamfastpadapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fastpad.com.teamfastpadapp.R;
import fastpad.com.teamfastpadapp.objects.WorkoutListObject;

/**
 * Created by JARVIS on 12/10/2016.
 */

public class WorkoutAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<WorkoutListObject> workoutList;

    public WorkoutAdapter(Context context, ArrayList<WorkoutListObject> workoutList){
        this.context = context;
        this.workoutList = workoutList;
    }
    @Override
    public int getCount() {
        return workoutList.size();
    }

    @Override
    public Object getItem(int position) {
        return workoutList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            // it's brand new
            convertView = LayoutInflater.from(context).inflate(R.layout.workout_list_item, null);
            holder = new ViewHolder();
//            holder.workoutIdLabel = (TextView) convertView.findViewById(R.id.workoutIdLabel);
            holder.workoutNameLabel = (TextView) convertView.findViewById(R.id.workoutNameLabel);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        WorkoutListObject workout = workoutList.get(position);
//        String id = Long.toString(workout.getId());
//        holder.workoutIdLabel.setText(id);
        holder.workoutNameLabel.setText(workout.getName());
        return convertView;
    }


    private static class ViewHolder {
        TextView workoutNameLabel;
//        TextView workoutIdLabel;
    }
}
