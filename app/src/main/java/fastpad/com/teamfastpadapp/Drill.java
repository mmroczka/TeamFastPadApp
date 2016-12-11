package fastpad.com.teamfastpadapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JARVIS on 12/10/2016.
 */

public class Drill implements Parcelable{
    private long id;
    private long workoutElementId;
    private int difficulty;
    private String name;
    private String videoUrl;
    private String gifUrl;
    private String description;


    /*  CONSTRUCTORS  */
    public Drill(){

    }

    public Drill(long id, long workoutElementId, int difficulty, String name, String videoUrl, String gifUrl, String description) {
        this.id = id;
        this.workoutElementId = workoutElementId;
        this.difficulty = difficulty;
        this.name = name;
        this.videoUrl = videoUrl;
        this.gifUrl = gifUrl;
        this.description = description;
    }

    /*   IMPLEMENTING PARCELABLE    */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(workoutElementId);
        dest.writeInt(difficulty);
        dest.writeString(name);
        dest.writeString(videoUrl);
        dest.writeString(gifUrl);
        dest.writeString(description);
    }

    private Drill (Parcel in){
        this.id = in.readLong();
        this.workoutElementId = in.readLong();
        this.difficulty = in.readInt();
        this.name = in.readString();
        this.videoUrl = in.readString();
        this.gifUrl = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Drill> CREATOR = new Parcelable.Creator<Drill>(){
        public Drill createFromParcel(Parcel in){
            return new Drill(in);
        }

        public Drill[] newArray(int size){
            return new Drill[size];
        }
    };

    /*   GETTERS AND SETTERS   */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWorkoutElementId() {
        return workoutElementId;
    }

    public void setWorkoutElementId(long workoutElementId) {
        this.workoutElementId = workoutElementId;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
