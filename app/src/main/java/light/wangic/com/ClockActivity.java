package light.wangic.com;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wangic.myapplication.R;

import java.io.IOException;

/**
 * Created by wangic on 2018/5/21.
 */
public class ClockActivity extends Activity {

    private AssetManager assetManager;
    private MediaPlayer mediaPlayer;
    private TextView stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clockactivity);
        stop = (TextView) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
            }
        });
        try {
            openAssetMusics();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openAssetMusics() throws IOException {
        //打开Asset目录
        assetManager = getAssets();
        mediaPlayer = new MediaPlayer();

        try {
            //打开音乐文件shot.mp3
            mediaPlayer.reset();
            //设置媒体播放器的数据资源
            mediaPlayer.setLooping(true);
            if( Common.RING_URI!=null) {
                mediaPlayer.setDataSource(this, Common.RING_URI);
            }else{
                mediaPlayer.setDataSource(this, RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("GsonUtils", "IOException" + e.toString());
        }
    }
}
