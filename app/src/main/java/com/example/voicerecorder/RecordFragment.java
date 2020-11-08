package com.example.voicerecorder;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ImageButton listBtn;
    private ImageButton recordBtn;
    private Chronometer timer;
    private TextView fileName;

    private boolean isRecording = false;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    private String recordFile;

    private MediaRecorder mediaRecorder;

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        listBtn = view.findViewById(R.id.record_fragment_iv_audio_list);
        recordBtn = view.findViewById(R.id.record_fragment_iv_record_button);
        timer = view.findViewById(R.id.record_fragment_record_timer);
        fileName = view.findViewById(R.id.record_fragment_tv_file_name);
        listBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v)
    {
     switch (v.getId())
     {
         case R.id.record_fragment_iv_audio_list:
             if (isRecording)
             {
                 AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                 alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         navController.navigate(R.id.action_recordFragment2_to_audioListFragment2);
                         isRecording =false;

                     }
                 });
                 alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                     }
                 });
                 alertDialog.setTitle("Audio still recording");
                 alertDialog.setMessage("Are you sure, you want to stop recording ?");
                 alertDialog.show();

             }else
             {
                 navController.navigate(R.id.action_recordFragment2_to_audioListFragment2);
             }
             break;
         case R.id.record_fragment_iv_record_button:
                if (isRecording)
                {
                    // stop recording
                    stopRecording();
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_stopped_record,null));
                    isRecording = false;
                }else
                {
                    // start recording
                    if (checkPremissions()) {
                        startRecording();
                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_recording, null));
                        isRecording = true;
                    }
                }
             break;

     }
    }

    private void stopRecording()
    {
        fileName.setText("Record stoped , file saved : "+recordFile);
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void startRecording()
    {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss", Locale.US);
        Date now = new Date();
        recordFile = "Record"+simpleDateFormat.format(now)+".3gp";

        fileName.setText("Record Name : "+recordFile);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath+"/"+recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private boolean checkPremissions()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), recordPermission)== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{recordPermission},PERMISSION_CODE);
            return false;
        }
    }


    @Override
    public void onStop()
    {
        super.onStop();
        if (isRecording)
        stopRecording();
    }
}
