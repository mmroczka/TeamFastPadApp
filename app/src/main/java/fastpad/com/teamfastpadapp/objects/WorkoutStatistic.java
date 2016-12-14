package fastpad.com.teamfastpadapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by JARVIS on 12/13/2016.
 */

public class WorkoutStatistic implements Parcelable{
    private long WorkoutId;
    private String StartDateTime;
    private String EndDateTime;
    private ArrayList<DrillStatistic> DrillStatistics = new ArrayList<>();

    public WorkoutStatistic(){

    }


    protected WorkoutStatistic(Parcel in) {
        WorkoutId = in.readLong();
        StartDateTime = in.readString();
        EndDateTime = in.readString();
        in.readTypedList(DrillStatistics, DrillStatistic.CREATOR);
    }

    public static final Parcelable.Creator<WorkoutStatistic> CREATOR = new Parcelable.Creator<WorkoutStatistic>() {
        @Override
        public WorkoutStatistic createFromParcel(Parcel in) {
            return new WorkoutStatistic(in);
        }

        @Override
        public WorkoutStatistic[] newArray(int size) {
            return new WorkoutStatistic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(WorkoutId);
        dest.writeString(StartDateTime);
        dest.writeString(EndDateTime);
        dest.writeTypedList(DrillStatistics);
    }

    public long getWorkoutId() {
        return WorkoutId;
    }

    public void setWorkoutId(long workoutId) {
        WorkoutId = workoutId;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }
    public ArrayList<DrillStatistic> getDrillStatistics() {
        return DrillStatistics;
    }

    public void setDrillStatistics(ArrayList<DrillStatistic> drillStatistics) {
        DrillStatistics = drillStatistics;
    }
}
