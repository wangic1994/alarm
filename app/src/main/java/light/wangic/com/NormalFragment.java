package light.wangic.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wangic.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ACITSEC on 2018/5/22.
 */

@SuppressLint("ValidFragment")
public class NormalFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private View view;
    private Button choose_date;
    private Button choose_time;
    private Button choose_ring;
    private static final String TAG = "NormalFragment";
    private AlarmManager alarmManager;
    private Button confirm;
    private final int REQUEST_CODE_PICK_RINGTONE = 1;

    private Calendar calendar = Calendar.getInstance();


    public NormalFragment() {
    }

    public NormalFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(context).inflate(R.layout.normal_fragment, null);
        choose_date = (Button) view.findViewById(R.id.choose_date);
        choose_time = (Button) view.findViewById(R.id.choose_time);
        confirm = (Button) view.findViewById(R.id.confirm);
        choose_ring = (Button) view.findViewById(R.id.choose_ring);
        choose_ring.setOnClickListener(this);
        confirm.setOnClickListener(this);
        choose_date.setOnClickListener(this);
        choose_time.setOnClickListener(this);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_date:
                getTime(0);
                break;
            case R.id.choose_time:
                getTime(1);
                break;
            case R.id.confirm:
                confirmTime();
                break;
            case R.id.choose_ring:
                chooseRing();
                break;
            default:
                break;


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getTime(int type) {
        if (type == 0) {
            Dialog dialog = null;
            if (dialog == null) {
                dialog = new Dialog(getActivity());
            }
            dialog.setContentView(R.layout.dialog_date);
            DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            final Dialog finalDialog = dialog;
//            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP ) {
//                datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        Log.e(TAG, "onDateChanged: " + year + monthOfYear + dayOfMonth);
//                        calendar.set(Calendar.YEAR, year);
//                        calendar.set(Calendar.MONTH, monthOfYear);
//                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        choose_date.setText("选择日期 : " + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
//                        finalDialog.dismiss();
//                    }
//                });
//            }else{
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.e(TAG, "onDateChanged: " + year + monthOfYear + dayOfMonth);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        choose_date.setText("选择日期 : " + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                        finalDialog.dismiss();
                    }
                });
//            }
            dialog.show();

        } else {
            Dialog dialog = null;
            if (dialog == null) {
                dialog = new Dialog(getActivity());
            }
            dialog.setContentView(R.layout.dialog_time);
            TimePicker datePicker = (TimePicker) dialog.findViewById(R.id.time_picker);
            Button confirm = (Button) dialog.findViewById(R.id.confirm);
            final Dialog finalDialog = dialog;
            datePicker.setIs24HourView(true);
            datePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    Log.e(TAG, "onTimeChanged: " + hourOfDay + minute);
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choose_time.setText("选择时间 : " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                    finalDialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void confirmTime() {
        SetAlarm setAlarm = new SetAlarm(getActivity());


        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent clock = new Intent(context, ClockActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, clock, PendingIntent.FLAG_UPDATE_CURRENT);

        Date time = calendar.getTime();
        setAlarm.setAlarm(getActivity(),time.getTime());
//        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTime(), pendingIntent);
//        Date currentDate = new Date(time.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Toast.makeText(context, format.format(currentDate) + " 闹钟已经设定", Toast.LENGTH_SHORT).show();
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
                Cursor cursor = getActivity().getContentResolver().query(uri, new String[] { MediaStore.Audio.Media.DATA }, null, null, null);

                if (cursor != null) {
                    if(cursor.moveToFirst()){
                        String mCurrentPath = cursor.getString(0);
                        String[] split = mCurrentPath.split("/");
                        if(split!=null){
                            String name = split[split.length - 1];
                            Log.e(TAG, "onActivityResult: " + name);
                            choose_ring.setText("选择铃声 : "+name);
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
