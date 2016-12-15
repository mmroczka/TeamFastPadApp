package fastpad.com.teamfastpadapp.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import fastpad.com.teamfastpadapp.R;

public class SettingsActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String VideoOrGif = "VideoOrGif";
    public static final String IsMediaControlsOn = "IsMediaControlsOn";
    public static final String IsSkipOn = "IsSkipOn";

    SharedPreferences sharedPreferences;
    public String videoOrGif;
    public boolean isMediaControls;
    boolean autoSkipToNextExercise;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox checkBoxForMediaControls;
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

                if(checkBoxForMediaControls.isChecked()){
                    isMediaControls = true;
                } else{
                    isMediaControls = false;
                }

                if(checkBoxAutoSkipToNextExercise.isChecked()){
                    autoSkipToNextExercise = true;
                } else {
                    autoSkipToNextExercise = false;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(VideoOrGif, videoOrGif);
                editor.putBoolean(String.valueOf(IsMediaControlsOn), isMediaControls);
                editor.putBoolean(String.valueOf(IsSkipOn), autoSkipToNextExercise);
                editor.commit();

                // let user know settings are saved
                Toast.makeText(SettingsActivity.this, "Settings Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUIVariablesAndDisplaySettings() {
        radioButton = (RadioButton) findViewById(R.id.radioBtnUseVideos);
        checkBoxForMediaControls = (CheckBox) findViewById(R.id.checkBoxMediaControls);
        checkBoxAutoSkipToNextExercise = (CheckBox) findViewById(R.id.checkBoxSkipExercise);

        String vG = sharedPreferences.getString(VideoOrGif, null);
        if (vG != null && vG.equalsIgnoreCase("Video")){
            radioButton.setChecked(true);
        } else{
            Toast.makeText(this, vG, Toast.LENGTH_SHORT).show();
            radioButton = (RadioButton) findViewById(R.id.radioBtnUseGifs);
            radioButton.setChecked(true);
        }
        
        boolean isMediaControls = sharedPreferences.getBoolean(IsMediaControlsOn, false);
        if (isMediaControls){
            checkBoxForMediaControls.setChecked(true);
        }

        if(sharedPreferences.getBoolean(IsSkipOn, false)){
            checkBoxAutoSkipToNextExercise.setChecked(true);
        }
    }
}
