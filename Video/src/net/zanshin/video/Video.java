package net.zanshin.video;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;

public class Video extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // fill view from resource
        setContentView(R.layout.main);
        VideoView video = (VideoView) findViewById(R.id.video);

        // load and start the movie
        video.setVideoPath("/data/samplevideo.3gp");
        video.start();
    }
}
