package fastpad.com.teamfastpadapp.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Map;

import fastpad.com.teamfastpadapp.R;

public class SettingsActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String VideoOrGif = "VideoOrGif";
    public static final String IsAutoPlayOn = "IsAutoPlayOn";
    public static final String IsSkipOn = "IsSkipOn";

    SharedPreferences sharedPreferences;
    public String videoOrGif;
    public boolean autoPlay;
    boolean autoSkipToNextExercise;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox checkBoxForVideos;
    private CheckBox checkBoxAutoSkipToNextExercise;
    private Button btnSaveSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup = (RadioGroup) findViewById(R.id.radioMedia);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        initUIVariablesAndDisplaySettings();

        btnSaveSettings = (Button) findViewById(R.id.btnSaveSettings);
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // put them in variables
                radioButton = (RadioButton) findViewById(R.id.radioBtnUseVideos);
                if (radioButton.isChecked()){
                    videoOrGif = "Video";
                }else{
                    videoOrGif = "Gif";
                }

                if(checkBoxForVideos.isChecked()){
                    autoPlay = true;
                } else{
                    autoPlay = false;
                }

                if(checkBoxAutoSkipToNextExercise.isChecked()){
                    autoSkipToNextExercise = true;
                } else {
                    autoSkipToNextExercise = false;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(VideoOrGif, videoOrGif);
                editor.putBoolean(String.valueOf(IsAutoPlayOn), autoPlay);
                editor.putBoolean(String.valueOf(IsSkipOn), autoSkipToNextExercise);
                editor.commit();

            }
        });
    }

    private void initUIVariablesAndDisplaySettings() {
        radioButton = (RadioButton) findViewById(R.id.radioBtnUseVideos);
        checkBoxForVideos = (CheckBox) findViewById(R.id.checkBoxAutoplayVideos);
        checkBoxAutoSkipToNextExercise = (CheckBox) findViewById(R.id.checkBoxSkipExercise);

        String vG = sharedPreferences.getString(VideoOrGif, null);
        if (vG != null && vG.equalsIgnoreCase("Video")){
            radioButton.setChecked(true);
        } else{
            Toast.makeText(this, vG, Toast.LENGTH_SHORT).show();
            radioButton = (RadioButton) findViewById(R.id.radioBtnUseGifs);
            radioButton.setChecked(true);
        }
        
        boolean isAutoPlayOn = sharedPreferences.getBoolean(IsAutoPlayOn, false);
        if (isAutoPlayOn){
            checkBoxForVideos.setChecked(true);
        }

        if(sharedPreferences.getBoolean(IsSkipOn, false)){
            checkBoxAutoSkipToNextExercise.setChecked(true);
        }
    }
}
