package light.wangic.com;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by ACITSEC on 2018/5/22.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this.getApplicationContext(), SpeechConstant.APPID + "=5aff99ce");
    }
}
