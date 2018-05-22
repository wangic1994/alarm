package light.wangic.com;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ACITSEC on 2018/5/19.
 */

public class SetAlarm {
    private Context context;
    private AlarmManager alarmManager;
    private static final String TAG = "SetAlarm";

    public SetAlarm(Context context) {
        this.context = context;
    }

    public void setAlarm(Context context, long time) {
        if(time == 0){
            return;
        }
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent clock = new Intent(context, ClockActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, clock, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.e(TAG, "setAlarm: "+System.currentTimeMillis());
        if(time>=System.currentTimeMillis()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            Date currentDate = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Toast.makeText(context, format.format(currentDate) + " 闹钟已经设定", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "不能设置过去的时间", Toast.LENGTH_SHORT).show();
        }
    }


    public long getTimeStamp(String conversation) throws ParseException {
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer hour = null;
        Integer min = null;

        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。

        String[] split = conversation.split(":");
        if (split != null) {
            for (int i = 0; i < split.length; i++) {
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(split[i]);
                String trim = m.replaceAll("").trim();
                if(!trim.isEmpty()) {
                    Log.e(TAG, "getTimeStamp: " + i + Integer.parseInt(trim));
                    if (i == 0) {
                        hour = Integer.parseInt(trim);
                    } else if (i == 1) {
                        min = Integer.parseInt(trim);
                    }
                }
            }
        } else {
            Log.e(TAG, "getTimeStamp: split null");
        }
        long timeStamp = 0;
        if (hour != null) {
            int i1 = hour.intValue();
            Log.e(TAG, "onResult: " + i1);
            if (conversation.contains("明天")) {
//                cal.add(Calendar.DAY_OF_MONTH, +1);
                if (i1 <= 12) {
                    //12小时制
                    String str;
                    if (conversation.contains("下午") || conversation.contains("晚上")) {
                        if (min != null) {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, i1 + 12, min.intValue(), 0);
                        } else {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, i1 + 12, 0, 0);
                        }
                        str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                        Date date = format.parse(str);
                        Log.e(TAG, "getTimeStamp: " + date.getTime());
                        timeStamp = date.getTime();
                    } else {
                        if (min != null) {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, i1, min.intValue(), 0);
                        } else {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, i1, 0, 0);
                        }
                        str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                        Date date = format.parse(str);
                        Log.e(TAG, "getTimeStamp: " + date.getTime());
                        timeStamp = date.getTime();
                    }
                    Log.e(TAG, "getTimeStamp: " + str);

                } else {
                    //24小时制
                    String str;
                    if (min != null) {
                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, i1, min.intValue(), 0);
                    } else {
                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, i1, 0, 0);
                    }
                    str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                    Log.e(TAG, "getTimeStamp: " + str);
                    Date date = format.parse(str);
                    Log.e(TAG, "getTimeStamp: " + date.getTime());
                    timeStamp = date.getTime();
                }
                Log.e(TAG, "getTimeStamp: " + currentTime + "      " + cal.getTimeInMillis());
                return timeStamp;
            } else if (conversation.contains("后天")) {
//                cal.add(Calendar.DAY_OF_MONTH, +2);
                if (i1 <= 12) {
                    //12小时制
                    String str;
                    if (conversation.contains("下午") || conversation.contains("晚上")) {
                        if (min != null) {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2, i1 + 12, min.intValue(), 0);
                        } else {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2, i1 + 12, 0, 0);
                        }
                        str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                        Log.e(TAG, "getTimeStamp: " + str);
                        Date date = format.parse(str);
                        Log.e(TAG, "getTimeStamp: " + date.getTime());
                        timeStamp = date.getTime();
                    } else {
                        if (min != null) {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2, i1, min.intValue(), 0);
                        } else {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2, i1, 0, 0);
                        }
                        str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                    }
                    Log.e(TAG, "getTimeStamp: " + str);
                    Date date = format.parse(str);
                    Log.e(TAG, "getTimeStamp: " + date.getTime());
                    timeStamp = date.getTime();

                } else {
                    //24小时制
                    String str;
                    if (min != null) {
                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2, i1, min.intValue(), 0);
                    } else {
                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2, i1, 0, 0);
                    }
                    str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                    Log.e(TAG, "getTimeStamp: " + str);
                    Date date = format.parse(str);
                    Log.e(TAG, "getTimeStamp: " + date.getTime());
                    timeStamp = date.getTime();

                }
                Log.e(TAG, "getTimeStamp: " + currentTime + "      " + cal.getTimeInMillis());

                return timeStamp;
            } else {
                if (i1 <= 12) {
                    //12小时制
                    String str;
                    if (conversation.contains("下午") || conversation.contains("晚上")) {
                        if (min != null) {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) , i1 + 12, min.intValue(), 0);
                        } else {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) , i1 + 12, 0, 0);
                        }
                        str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                        Log.e(TAG, "getTimeStamp: " + str);
                        Date date = format.parse(str);
                        Log.e(TAG, "getTimeStamp: " + date.getTime());
                        timeStamp = date.getTime();
                    } else {
                        if (min != null) {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) , i1, min.intValue(), 0);
                        } else {
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) , i1, 0, 0);
                        }
                        str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                        Date date = format.parse(str);
                        Log.e(TAG, "getTimeStamp: " + date.getTime());
                        timeStamp = date.getTime();
                    }
                    Log.e(TAG, "getTimeStamp: " + str);

                } else {
                    //24小时制
                    String str;
                    if (min != null) {
                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2, i1, min.intValue(), 0);
                    } else {
                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2, i1, 0, 0);
                    }
                    str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime());
                    Log.e(TAG, "getTimeStamp: " + str);
                    Date date = format.parse(str);
                    Log.e(TAG, "getTimeStamp: " + date.getTime());
                    timeStamp = date.getTime();

                }
                return timeStamp;
            }
        } else {
            return 0;
        }

    }


}
