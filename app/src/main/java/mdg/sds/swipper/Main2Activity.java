package mdg.sds.swipper;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.swipper.library.ViewManager;

public class Main2Activity extends ViewManager {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        videoView = (VideoView) findViewById(R.id.videoView);
        Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView.setVideoURI(video1);
        videoView.start();
        set(this, videoView);
        Brightness("vertical");
        Volume("circular");
        Seek("horizontal", videoView);
    }
}
