package fastpad.com.teamfastpadapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by JARVIS on 12/10/2016.
 */

public class Workout implements Parcelable{
    private long id;
    private String name;
    private String description;
    private ArrayList<Drill> drills = new ArrayList<>();

    /*  CONSTRUCTORS  */
    public Workout(){

    }

    private Workout (Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        in.readTypedList(drills, Drill.CREATOR);
    }

    /*   IMPLEMENTING PARCELABLE    */
    public static final Parcelable.Creator<Workout> CREATOR = new Parcelable.Creator<Workout>(){
        public Workout createFromParcel(Parcel in){
            return new Workout(in);
        }

        public Workout[] newArray(int size){
            return new Workout[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeTypedList(drills);
    }

    /*   GETTERS AND SETTERS   */

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Drill> getDrills(){
        return this.drills;
    }

    public void setDrills(ArrayList<Drill> drills){
        this.drills = drills;
    }
}
