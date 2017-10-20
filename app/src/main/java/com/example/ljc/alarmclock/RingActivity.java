package com.example.ljc.alarmclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        cancel_ring = (Button) findViewById(R.id.cancel_ring);
        Bundle bundle = this.getIntent().getExtras();
        final int id = bundle.getInt("_id");
        final int isVibrate = bundle.getInt("vibrate");
        final int isRing = bundle.getInt("ring");

        Log.d("asd", "alarm ring id = " + Integer.toString(id));

        if (isVibrate == 1) {
            vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{1000, 3000, 1000}, 0);
        }

        if (isRing == 1) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, notification);
            ringtone.play();
        }

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