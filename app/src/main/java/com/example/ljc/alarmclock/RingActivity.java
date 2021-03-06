package com.example.ljc.alarmclock;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.ljc.alarmclock.database.AlarmDataHelper;

/**
 * Created by ljc on 17-10-19.
 */

public class RingActivity extends Activity {
    private Ringtone ringtone;
    private Vibrator vibrator;
    private Button cancel_ring;
    private AlarmDataHelper dbHelper;


    private static final String TAG = "RingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        //设置闹钟响铃界面在锁屏状态下可见，唤起屏幕
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        cancel_ring = (Button) findViewById(R.id.cancel_ring);
        Bundle bundle = this.getIntent().getExtras();
        final int id = bundle.getInt("_id");
        final int daysofweek = bundle.getInt("daysofweek");
        final int isVibrate = bundle.getInt("vibrate");
        final int isRing = bundle.getInt("ring");

        //更改闹钟状态
        if (daysofweek == 0) {
            dbHelper = new AlarmDataHelper(this, "alarm.db", null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("alarms", null, null, null, null, null, null);
            ContentValues values = new ContentValues();
            values.put("state", 0);
            db.update("alarms", values, "_id =" + id, null);
            cursor.close();
            db.close();
        }
        Log.d(TAG, "alarm ring id = " + Integer.toString(id));

        //获取振动
        if (isVibrate == 1) {
            vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{1000, 3000, 1000}, 0);
        }

        //获取铃声
        if (isRing == 1) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, notification);
            ringtone.play();
        }

        //退出，释放资源
        cancel_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVibrate == 1)
                    vibrator.cancel();
                if (isRing == 1)
                    ringtone.stop();
                RingActivity.this.finish();
            }
        });
    }
}