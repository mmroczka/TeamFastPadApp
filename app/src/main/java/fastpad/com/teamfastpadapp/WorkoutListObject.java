package fastpad.com.teamfastpadapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JARVIS on 12/10/2016.
 */

public class WorkoutListObject implements Parcelable{
    public long id;
    public String name;

    public WorkoutListObject(){

    }

    public WorkoutListObject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }

    private WorkoutListObject(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<WorkoutListObject> CREATOR = new Creator<WorkoutListObject>() {
        @Override
        public WorkoutListObject createFromParcel(Parcel source) {
            return new WorkoutListObject(source);
        }

        @Override
        public WorkoutListObject[] newArray(int size) {
            return new WorkoutListObject[size];
        }
    };
}
