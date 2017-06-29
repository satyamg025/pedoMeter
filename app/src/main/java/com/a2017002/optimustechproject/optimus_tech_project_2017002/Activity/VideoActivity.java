package com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.VideoView;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.swipper.library.Swipper;

public class VideoActivity extends Swipper {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        videoView=(VideoView)findViewById(R.id.videoView);

       // String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        String vidAddress=getIntent().getExtras().getString("videoFetchUrl");
        Uri vidUri = Uri.parse(vidAddress);

        videoView.setVideoURI(vidUri);

        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(videoView);
        videoView.setMediaController(vidControl);

        videoView.start();
        Brightness(Orientation.CIRCULAR);
        Volume(Orientation.VERTICAL);
        Seek(Orientation.HORIZONTAL, videoView);
        set(this);
    }

}
