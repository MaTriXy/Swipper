package mdg.sds.swipper;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.example.library.MainActivity;

public class Main2Activity extends MainActivity {

    VideoView vv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        vv=(VideoView)findViewById(R.id.videoView);
        Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        vv.setVideoURI(video1);
        vv.start();
        set(this,vv);
    }
}
