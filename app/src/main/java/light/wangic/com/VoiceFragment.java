package light.wangic.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.wangic.myapplication.R;

import java.text.ParseException;
import java.util.List;

import bean.RecordBean;

/**
 * Created by ACITSEC on 2018/5/22.
 */

@SuppressLint("ValidFragment")
public class VoiceFragment extends Fragment {
    private Context context;
    private View view;
    private Button btn;
    private static final String TAG = "VoiceFragment";
    private SpeechRecognizer mlat;
    private final int REQUEST_CODE_PICK_RINGTONE = 1;
    private Button choose_ring;

    public VoiceFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(context).inflate(R.layout.voice_fragment, null);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });
        choose_ring = (Button) view.findViewById(R.id.choose_ring);
        choose_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRing();
            }
        });
        return view;
    }


    private void initDialog() {
        RecognizerDialog mDialog = new RecognizerDialog(getActivity(), new InitListener() {
            @Override
            public void onInit(int code) {
                Log.d(TAG, "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    System.out.println("初始化失败，错误码：" + code);
                }
            }
        });
        mDialog.setParameter(SpeechConstant.DOMAIN, "iat");
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        final StringBuffer stringBuffer = new StringBuffer();
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String content = recognizerResult.getResultString();
                System.out.println(content);
                Gson g = new Gson();
                RecordBean data = g.fromJson(content, RecordBean.class);
                List<RecordBean.WsBean> words = data.getWs();
                for (RecordBean.WsBean ws : words) {
                    for (RecordBean.WsBean.CwBean word : ws.getCw()) {
                        stringBuffer.append(word.getW());
                    }
                }

                String s = stringBuffer.toString();
                Log.d(TAG, "onResult: " + s);
                s = s.replace("一点", "1点");
                s = s.replace("二点", "2点");
                s = s.replace("三点", "3点");
                s = s.replace("四点", "4点");
                s = s.replace("五点", "5点");
                s = s.replace("六点", "6点");
                s = s.replace("七点", "7点");
                s = s.replace("八点", "8点");
                s = s.replace("九点", "9点");
                s = s.replace("十点", "10点");
                s = s.replace("十一点", "11点");
                s = s.replace("十二点", "12点");
                s = s.replace("十三点", "13点");
                s = s.replace("十四点", "14点");
                s = s.replace("十五点", "15点");
                s = s.replace("十六点", "16点");
                s = s.replace("十七点", "17点");
                s = s.replace("十八点", "18点");
                s = s.replace("十九点", "19点");
                s = s.replace("二十点", "20点");
                s = s.replace("二十一点", "21点");
                s = s.replace("二十二点", "22点");
                s = s.replace("二十三点", "23点");
                s = s.replace("二十四点", "24点");
                Log.d(TAG, "onResult: " + s);
                SetAlarm setAlarm = new SetAlarm(getActivity());
                try {
                    setAlarm.setAlarm(getActivity(), setAlarm.getTimeStamp(s));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        mDialog.show();
    }


    private RecognizerDialogListener mRecoListener = new RecognizerDialogListener() {
        //听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        //关于解析Json的代码可参见MscDemo中JsonParser类；
        //isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d("Result:", results.getResultString());
        }

        //会话发生错误回调接口
        public void onError(SpeechError error) {
            error.getPlainDescription(true); //获取错误码描述
        }

        //开始录音
        public void onBeginOfSpeech() {
        }

        //音量值0~30
        public void onVolumeChanged(int volume) {
        }

        //结束录音
        public void onEndOfSpeech() {
        }

        //扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };


    private void initSpeechRecognizer() {
        mlat = SpeechRecognizer.createRecognizer(getActivity(), new InitListener() {
            @Override
            public void onInit(int code) {
                Log.d(TAG, "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    System.out.println("初始化失败，错误码：" + code);
                }
            }
        });
        if (mlat == null) {
            Log.d(TAG, "initSpeechRecognizer: ");
            return;
        }
        mlat.setParameter(SpeechConstant.DOMAIN, "iat");
        mlat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mlat.setParameter(SpeechConstant.ACCENT, "mandarin");
        mlat.startListening(new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String content = recognizerResult.getResultString();
                System.out.println(content);
                Gson g = new Gson();
                Log.d(TAG, "onResult: " + recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {
                System.out.println(speechError.getErrorCode());
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });

    }
    private void chooseRing() {
        getNotificationTone();
    }

    private void getNotificationTone() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "这里填写上你想显示的标题");

        Uri ringtoneUri;
        if (Common.RING_URI != null) { //这个是你之前选择好的标题，在选择铃声的时候会把它默认选上
            ringtoneUri = Common.RING_URI;
        } else { // mCustomRingtone如果为空，择默认的铃声会被选中
            ringtoneUri = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //选择audio的类型，一般有铃声 通知音 闹铃音
        }

        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtoneUri);

        startActivityForResult(intent, REQUEST_CODE_PICK_RINGTONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_PICK_RINGTONE:
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Common.RING_URI = uri;
                Cursor cursor = getActivity().getContentResolver().query(uri, new String[]{MediaStore.Audio.Media.DATA}, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        String mCurrentPath = cursor.getString(0);
                        String[] split = mCurrentPath.split("/");
                        if (split != null) {
                            String name = split[split.length - 1];
                            Log.e(TAG, "onActivityResult: " + name);
                            choose_ring.setText("选择铃声 : " + name);
                        }
                    }
                    cursor.close();
                }
                break;
            default:
                break;
        }
    }


}
