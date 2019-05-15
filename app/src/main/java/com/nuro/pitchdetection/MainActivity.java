package com.nuro.pitchdetection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity {
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

        //pitchText.setText("" + pitchInHz);
        Log.d("tone",pitchInHz+"");
        if (pitchInHz >= 110 && pitchInHz < 123.47) {
            //A
           // noteText.setText("A");
            Log.d("tone","A");
        } else if (pitchInHz >= 123.47 && pitchInHz < 130.81) {
            //B
           // noteText.setText("B");
            Log.d("tone","B");
        } else if (pitchInHz >= 130.81 && pitchInHz < 146.83) {
            //C
           // noteText.setText("C");
            Log.d("tone","C");
        } else if (pitchInHz >= 146.83 && pitchInHz < 164.81) {
            //D
           // noteText.setText("D");
            Log.d("tone","D");
        } else if (pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            //E
           // noteText.setText("E");
            Log.d("tone","E");
        } else if (pitchInHz >= 174.61 && pitchInHz < 185) {
            //F
           // noteText.setText("F");
            Log.d("tone","F");
        } else if (pitchInHz >= 185 && pitchInHz < 196) {
            //G
           // noteText.setText("G");
            Log.d("tone","G");
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
