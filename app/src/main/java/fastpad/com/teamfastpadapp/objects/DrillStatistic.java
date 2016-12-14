package fastpad.com.teamfastpadapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JARVIS on 12/13/2016.
 */

public class DrillStatistic implements Parcelable{

    private long WorkoutElementId;
    private int CompletedRepetitions;

    public DrillStatistic(long workoutElementId, int completedRepetitions) {
        this.WorkoutElementId = workoutElementId;
        this.CompletedRepetitions = completedRepetitions;
    }

    protected DrillStatistic(Parcel in) {
        WorkoutElementId = in.readInt();
        CompletedRepetitions = in.readInt();
    }

    public static final Creator<DrillStatistic> CREATOR = new Creator<DrillStatistic>() {
        @Override
        public DrillStatistic createFromParcel(Parcel in) {
            return new DrillStatistic(in);
        }

        @Override
        public DrillStatistic[] newArray(int size) {
            return new DrillStatistic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(WorkoutElementId);
        dest.writeInt(CompletedRepetitions);
    }

    public long getWorkoutElementId() {
        return WorkoutElementId;
    }


    public void setWorkoutElementId(long workoutElementId) {
        WorkoutElementId = workoutElementId;

    }

    public int getCompletedRepetitions() {
        return CompletedRepetitions;
    }

    public void setCompletedRepetitions(int completedRepetitions) {
        CompletedRepetitions = completedRepetitions;
    }

}
