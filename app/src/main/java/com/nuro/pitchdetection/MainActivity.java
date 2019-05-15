package com.nuro.pitchdetection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        relativeLayout = findViewById(R.id.container);
        textView = findViewById(R.id.notes);
        mic();
    }

    public void mic()
    {

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
        if (pitchInHz >= 110 && pitchInHz < 123.47) {
            Log.d("tone","A");
            relativeLayout.setBackgroundColor(Color.parseColor("#27ae60"));
            textView.setText("A");
        } else if (pitchInHz >= 123.47 && pitchInHz < 130.81) {
            Log.d("tone","B");
            textView.setText("B");
            relativeLayout.setBackgroundColor(Color.parseColor("#16a085"));
        } else if (pitchInHz >= 130.81 && pitchInHz < 146.83) {
            Log.d("tone","C");
            textView.setText("C");
            relativeLayout.setBackgroundColor(Color.parseColor("#f1c40f"));
        } else if (pitchInHz >= 146.83 && pitchInHz < 164.81) {
            Log.d("tone","D");
            textView.setText("D");
            relativeLayout.setBackgroundColor(Color.parseColor("#f39c12"));
        } else if (pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            Log.d("tone","E");
            textView.setText("E");
            relativeLayout.setBackgroundColor(Color.parseColor("#d35400"));
        } else if (pitchInHz >= 174.61 && pitchInHz < 185) {
            Log.d("tone","F");
            textView.setText("F");
            relativeLayout.setBackgroundColor(Color.parseColor("#c0392b"));
        } else if (pitchInHz >= 185 && pitchInHz < 196) {
            Log.d("tone","G");
            textView.setText("G");
            relativeLayout.setBackgroundColor(Color.parseColor("#8e44ad"));
        }else{
            textView.setText(" ");
            relativeLayout.setBackgroundColor(Color.parseColor("#ecf0f1"));
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
}
