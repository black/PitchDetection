package com.nuro.pitchdetection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private TextView textView;
    private RadioGroup radioGroup;
    private String chrod="A";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mainView = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(mainView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sound Bulb");
        setSupportActionBar(toolbar);
        //Permission Check
        int PERMISSIONS_ALL = 1;
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO
        };
        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this,permissions, PERMISSIONS_ALL);
        }
        relativeLayout = mainView.findViewById(R.id.container);
        textView = findViewById(R.id.notes);
        mic();

        radioGroup = findViewById(R.id.musicselector);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checked) {
                RadioButton radioButton = findViewById(checked);
                chrod = (String)radioButton.getText();
                Log.d("checkedin",chrod);
            }
        });
    }

//    public void checkButton(View v){
//        int radioId = radioGroup.getCheckedRadioButtonId();
//        Log.d("id",radioId+"");
//    }


    public void mic() {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPitch(pitchInHz);
                    }
                });
            }
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    public void processPitch(float pitchInHz) {
        switch (chrod){
            case "A":
                if (pitchInHz >= 110 && pitchInHz < 123.47) {
                    Log.d("tone","A");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb));
                    textView.setText("SA");
                }else{
                    textView.setText(" ");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb_off));
                }
                break;
            case "B":
                if (pitchInHz >= 123.47 && pitchInHz < 130.81) {
                    Log.d("tone","B");
                    textView.setText("B");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb));
                }else{
                    textView.setText(" ");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb_off));
                }
                break;
            case "C":
                if (pitchInHz >= 130.81 && pitchInHz < 146.83) {
                    Log.d("tone","C");
                    textView.setText("RE");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb));
                }else{
                    textView.setText(" ");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb_off));
                }
                break;
            case "D":
                if (pitchInHz >= 146.83 && pitchInHz < 164.81) {
                    Log.d("tone","D");
                    textView.setText("D");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb));
                }else{
                    textView.setText(" ");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb_off));
                }
                break;
            case "E":
                if (pitchInHz >= 164.81 && pitchInHz <= 174.61) {
                    Log.d("tone","E");
                    textView.setText("E");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb));
                }else{
                    textView.setText(" ");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb_off));
                }
                break;
            case "F":
                if (pitchInHz >= 174.61 && pitchInHz < 185) {
                    Log.d("tone","F");
                    textView.setText("F");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb));
                }else{
                    textView.setText(" ");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb_off));
                }
                break;
            case "G":
                if (pitchInHz >= 185 && pitchInHz < 196) {
                    Log.d("tone","G");
                    textView.setText("G");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb));
                }else{
                    textView.setText(" ");
                    relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_light_bulb_off));
                }
                break;

        }
    }

    public static boolean hasPermissions(Context context, String... permissions){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for (String permission:permissions){
                if (ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }
    // Toolbar Menu ------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.eog_battery:
                View v = findViewById(R.id.musicselector);
                setVisibility(v);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

}
