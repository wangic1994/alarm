package light.wangic.com;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.wangic.myapplication.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import bean.RecordBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private ViewPager viewpager;
    private TextView header_voice;
    private TextView header_normal;

    private NormalFragment normalFragment;
    private VoiceFragment voiceFragment;
    private ArrayList<Fragment> list = new ArrayList<>();
    private FragmentAdapter fragmentAdapter;
    private int blue = Color.parseColor("#5371e8");
    private int white = Color.parseColor("#ffffff");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        header_voice = (TextView) findViewById(R.id.header_voice);
        header_normal = (TextView) findViewById(R.id.header_normal);
        voiceFragment = new VoiceFragment(this);
        normalFragment = new NormalFragment(this);
        list.add(voiceFragment);
        list.add(normalFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager,list);
        viewpager.setAdapter(fragmentAdapter);
        header_voice.setOnClickListener(this);
        header_normal.setOnClickListener(this);
        header_voice.setBackgroundColor(blue);
        header_voice.setTextColor(white);
        header_normal.setBackgroundColor(white);
        header_normal.setTextColor(blue);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    header_voice.setBackgroundColor(blue);
                    header_voice.setTextColor(white);
                    header_normal.setBackgroundColor(white);
                    header_normal.setTextColor(blue);
                }else{
                    header_normal.setBackgroundColor(blue);
                    header_normal.setTextColor(white);
                    header_voice.setBackgroundColor(white);
                    header_voice.setTextColor(blue);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_voice:
                viewpager.setCurrentItem(0);
                break;
            case R.id.header_normal:
                viewpager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;
        public FragmentAdapter(FragmentManager fm,    ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
    }
}
